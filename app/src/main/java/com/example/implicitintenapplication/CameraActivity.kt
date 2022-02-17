package com.example.implicitintenapplication

//import com.nabinbhandari.android.permissions.PermissionHandler
//import com.nabinbhandari.android.permissions.Permissions
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.implicitintenapplication.databinding.ActivityCameraBinding
import java.io.File
import java.io.FileDescriptor
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraActivity : AppCompatActivity() {

    lateinit var binding : ActivityCameraBinding
    lateinit var lokasiGambar : Uri

    var mimeType: String = ""
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE_WITHOUT_SCALE = 11
    var shareUri: Uri? = null
    var currentRequestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        checkPermissions()

        binding.btnToGaleri.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 100)
        }

        binding.btnToCamera.setOnClickListener {

            // konfigurasi folder
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder().build())

            // atur folder
            val namaFolder = "camera-kotlin-intermediate"
            val namaFolder2 = "FolderAsad"
            val path = Environment.getExternalStorageDirectory().absolutePath
            val folder = File(path, namaFolder)
            if (!folder.exists()){
                folder.mkdir()
            }

            val namaFile = File("$path/$namaFolder/image-${currentDate()}.jpg")
            lokasiGambar = Uri.fromFile(namaFile)

            // buka kamera
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, lokasiGambar)
            startActivityForResult(intent, 200)
        }


        binding.btnToCamera2.setOnClickListener {
            if(this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
                clickPhoto(REQUEST_IMAGE_CAPTURE_WITHOUT_SCALE)
            }else{
                Toast.makeText(this,"No camera available on this device.",Toast.LENGTH_LONG).show()
            }
        }
    }


    fun clickPhoto(requestCode: Int) {

        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureIntent.resolveActivity(packageManager)?.also {
            // Create the File where the photo should go
            val photoFile: File? = try {
                createImageFile()
            }
            catch (ex: IOException) {
                Toast.makeText(this, "${ex.message}", Toast.LENGTH_LONG).show()
                null
            }

            // Continue only if the File was successfully created
            photoFile?.also {

                val photoURI: Uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID  + ".provider", it)

                mimeType = "image/*"
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, getNewFileName())
                    put(MediaStore.Images.Media.MIME_TYPE, mimeType)
                    put(MediaStore.Images.Media.RELATIVE_PATH, getImageDirectoryPath())
                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
                else {
                    val imageUri = contentResolver.insert(MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL), values)
                    if (imageUri != null) {
                        currentPhotoPath = imageUri.toString()
                        shareUri = imageUri
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                }
                initRequestCode(takePictureIntent, requestCode)
            }
        }
    }

    private fun getNewFileName() : String {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return "JPEG_${timeStamp}_.jpg"
    }


    fun getImageDirectoryPath(): String{
        return Environment.DIRECTORY_PICTURES + File.separator + "AsadDemo"
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getAppSpecificAlbumStorageDir(this, Environment.DIRECTORY_PICTURES,"AsadDemo")
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }


    fun getAppSpecificAlbumStorageDir(context: Context, albumName: String,subAlbumName: String): File? {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        val file = File(context.getExternalFilesDir(albumName), subAlbumName
        )
        if (!file?.mkdirs()) {
            Log.e("fssfsf", "Directory not created")
        }

//        if (!file.exists()){
//            file.mkdirs()
//        }

        return file
    }

//    val startActivityForResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult())
//        { result: Instrumentation.ActivityResult ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                onActivityResultListener.onActivityResult(result, currentRequestCode)
//            }
//        }


    val startActivityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){

            //if (currentRequestCode == REQUEST_IMAGE_CAPTURE_WITHOUT_SCALE) {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                var file = File(currentPhotoPath)
                shareUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)
                binding.imgShow.setImageURI(Uri.parse(currentPhotoPath))
            }
            else {
                var image: Bitmap = getBitmapFromContentResolver(Uri.parse(currentPhotoPath))
                binding.imgShow.setImageURI(Uri.parse(currentPhotoPath))
            }
            //to show image in gallery
            addImageInGallery()
            //}
        }
    }


    private fun initRequestCode(takePictureIntent: Intent, requestImageCapture: Int) {
        currentRequestCode = requestImageCapture
        startActivityForResult.launch(takePictureIntent)
    }


    fun getBitmapFromContentResolver(shareUri: Uri): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor = contentResolver.openFileDescriptor(shareUri, "r")!!
        val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }


    fun addImageInGallery() {

        val file = File(currentPhotoPath)
        MediaScannerConnection.scanFile(this, arrayOf(file.absolutePath),
            null, MediaScannerConnection.OnScanCompletedListener { path, uri ->
                Log.i("ExternalStorage", "Scanned " + path + ":");
                Log.i("ExternalStorage", "-> uri=" + uri);
            })
    }


//    private fun checkPermissions(){
//
//        val permissions = arrayOf(Manifest.permission.CAMERA,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE)
//
//        val rationale = "Please provide camera permission so that you can ..."
//        val options: Permissions.Options = Permissions.Options()
//            .setRationaleDialogTitle("Info")
//            .setSettingsDialogTitle("Warning")
//
//        Permissions.check(this, permissions, rationale, options, object : PermissionHandler() {
//            override fun onGranted() {
//                // do your task.
//                Toast.makeText(this@CameraActivity, "permissions diaktifkan", Toast.LENGTH_SHORT).show()
//            }
//            override fun onDenied(context: Context?, deniedPermissions: ArrayList<String?>?) {
//                Toast.makeText(this@CameraActivity, "Perizinan ditolak, beberapa fitur mungkin tidak berfungsi sempurna", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    private fun currentDate() : String{
        val sdf = SimpleDateFormat("dd-MM-yyyy_HH:mm:ss", Locale.getDefault())
        val date = Date()
        return sdf.format(date)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100){
            if (resultCode == Activity.RESULT_OK){
                val uri = data?.data
                binding.imgShow.setImageURI(uri)
            }
            else if (resultCode == Activity.RESULT_CANCELED){
                binding.imgShow.setImageResource(R.drawable.ic_launcher_background)
            }
        }

        if (requestCode == 200){
            if (resultCode == Activity.RESULT_OK){
                binding.imgShow.setImageURI(lokasiGambar)
            }
            else if (resultCode == Activity.RESULT_CANCELED){
                binding.imgShow.setImageResource(R.drawable.ic_launcher_background)
            }
        }
    }
}