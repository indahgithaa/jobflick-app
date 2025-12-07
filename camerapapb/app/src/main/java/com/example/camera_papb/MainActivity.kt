package com.example.camera_papb

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.resume

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CameraScreen()
            }
        }
    }
}

@Composable
fun CameraScreen() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    var hasPermission by remember { mutableStateOf(false) }
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var camera by remember { mutableStateOf<Camera?>(null) }
    var isBackCamera by remember { mutableStateOf(true) }
    var isTorchOn by remember { mutableStateOf(false) }
    var flashMode by remember { mutableStateOf(ImageCapture.FLASH_MODE_OFF) }
    var savedImageUri by remember { mutableStateOf<Uri?>(null) }
    var thumbnailBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var message by remember { mutableStateOf<String?>(null) }

    val cameraPermission = Manifest.permission.CAMERA
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (!granted) {
            message = "Izin kamera diperlukan"
        }
    }

    LaunchedEffect(Unit) {
        launcher.launch(cameraPermission)
    }

    // Bind kamera ketika previewView siap atau user ganti kamera
    LaunchedEffect(previewView, isBackCamera) {
        previewView?.let { view ->
            val provider = getProcessCameraProvider(context)
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(view.surfaceProvider)
            }

            val selector = if (isBackCamera) {
                CameraSelector.DEFAULT_BACK_CAMERA
            } else {
                CameraSelector.DEFAULT_FRONT_CAMERA
            }

            val (cam, ic) = bindWithImageCapture(provider, lifecycleOwner, preview, selector)

            ic.targetRotation = view.display?.rotation ?: Surface.ROTATION_0
            ic.flashMode = flashMode

            camera = cam
            imageCapture = ic
            isTorchOn = false
        }
    }

    Scaffold(
        topBar = {
            TopStatusBar(
                flashMode = flashMode,
                isBackCamera = isBackCamera
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (hasPermission) {
                // PREVIEW PENUH
                CameraPreview(
                    modifier = Modifier.fillMaxSize()
                ) { view ->
                    previewView = view
                }

                // PANEL KONTROL TRANSPARAN DI BAWAH
                BottomControlPanel(
                    flashMode = flashMode,
                    onFlashModeChange = { new ->
                        flashMode = new
                        imageCapture?.flashMode = new
                    },
                    onCaptureClick = {
                        imageCapture?.let { ic ->
                            scope.launch {
                                val uri = takePhotoAndSave(context, ic)
                                if (uri != null) {
                                    savedImageUri = uri
                                    message = "Foto tersimpan"
                                    thumbnailBitmap = loadThumbnail(context, uri)
                                } else {
                                    message = "Gagal mengambil foto"
                                }
                            }
                        }
                    },
                    onSwitchCamera = {
                        isBackCamera = !isBackCamera
                    },
                    thumbnailBitmap = thumbnailBitmap,
                    thumbnailLabel = savedImageUri?.lastPathSegment ?: "-",
                    // ⬇⬇⬇ align dipindah ke sini (di dalam BoxScope)
                    modifier = Modifier.align(Alignment.BottomCenter)
                )

                // TOGGLE TORCH DI SUDUT KANAN ATAS
                if (isBackCamera && camera?.cameraInfo?.hasFlashUnit() == true) {
                    TorchChip(
                        isTorchOn = isTorchOn,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 56.dp, end = 16.dp),
                        onToggle = {
                            val newState = !isTorchOn
                            isTorchOn = newState
                            camera?.cameraControl?.enableTorch(newState)
                        }
                    )
                }

                // SNACKBAR PESAN
                message?.let { msg ->
                    Snackbar(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(msg, color = Color.White)
                            TextButton(onClick = { message = null }) {
                                Text("OK", color = Color.White)
                            }
                        }
                    }
                }
            } else {
                PermissionExplanation(
                    onRequestAgain = { launcher.launch(cameraPermission) }
                )
            }
        }
    }
}

/* ---------- Composables UI ---------- */

@Composable
fun TopStatusBar(
    flashMode: Int,
    isBackCamera: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x66000000)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Simple CameraX",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = if (isBackCamera) "Kamera belakang" else "Kamera depan",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
            Text(
                text = when (flashMode) {
                    ImageCapture.FLASH_MODE_ON -> "Flash: ON"
                    ImageCapture.FLASH_MODE_AUTO -> "Flash: AUTO"
                    else -> "Flash: OFF"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun BottomControlPanel(
    flashMode: Int,
    onFlashModeChange: (Int) -> Unit,
    onCaptureClick: () -> Unit,
    onSwitchCamera: () -> Unit,
    thumbnailBitmap: Bitmap?,
    thumbnailLabel: String,
    modifier: Modifier = Modifier   // ✅ tambah modifier di sini
) {
    Box(
        modifier = modifier   // ✅ pakai modifier dari parameter, TANPA align di sini
            .fillMaxWidth()
            .background(Color(0x66000000))
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Baris atas: flash + switch kamera
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        val newMode = when (flashMode) {
                            ImageCapture.FLASH_MODE_OFF -> ImageCapture.FLASH_MODE_ON
                            ImageCapture.FLASH_MODE_ON -> ImageCapture.FLASH_MODE_AUTO
                            else -> ImageCapture.FLASH_MODE_OFF
                        }
                        onFlashModeChange(newMode)
                    }
                ) {
                    Text(
                        text = when (flashMode) {
                            ImageCapture.FLASH_MODE_ON -> "Flash ON"
                            ImageCapture.FLASH_MODE_AUTO -> "Flash AUTO"
                            else -> "Flash OFF"
                        }
                    )
                }

                OutlinedButton(onClick = onSwitchCamera) {
                    Text(if (thumbnailBitmap == null) "Ganti kamera" else "Swap kamera")
                }
            }

            // Tombol capture besar di tengah
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(4.dp, Color.LightGray, CircleShape)
                        .clickable { onCaptureClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text("SHOT")
                }
            }

            // Thumbnail baris bawah
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                thumbnailBitmap?.let { bmp ->
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.White, CircleShape)
                    ) {
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = "Foto terakhir",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Foto terakhir",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = thumbnailLabel,
                            color = Color.LightGray,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1
                        )
                    }
                } ?: run {
                    Text(
                        text = "Belum ada foto",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun TorchChip(
    isTorchOn: Boolean,
    modifier: Modifier = Modifier,
    onToggle: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = if (isTorchOn) Color(0xFFFFC107) else Color(0x66000000)
        )
    ) {
        Text(
            text = if (isTorchOn) "Torch ON" else "Torch OFF",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            color = if (isTorchOn) Color.Black else Color.White,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun PermissionExplanation(onRequestAgain: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Aplikasi membutuhkan izin kamera.",
            color = Color.White
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onRequestAgain) {
            Text("Beri izin")
        }
    }
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onPreviewReady: (PreviewView) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PreviewView(context).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
                post { onPreviewReady(this) }
            }
        }
    )
}

/* ---------- CameraX helpers ---------- */

suspend fun getProcessCameraProvider(context: Context): ProcessCameraProvider {
    return suspendCancellableCoroutine { cont ->
        val future = ProcessCameraProvider.getInstance(context)
        future.addListener(
            { cont.resume(future.get()) },
            ContextCompat.getMainExecutor(context)
        )
    }
}

fun bindWithImageCapture(
    provider: ProcessCameraProvider,
    owner: LifecycleOwner,
    preview: Preview,
    selector: CameraSelector
): Pair<Camera, ImageCapture> {
    val ic = ImageCapture.Builder()
        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
        .build()
    provider.unbindAll()
    val cam = provider.bindToLifecycle(owner, selector, preview, ic)
    return cam to ic
}

suspend fun takePhotoAndSave(context: Context, imageCapture: ImageCapture): Uri? {
    return withContext(Dispatchers.IO) {
        try {
            val photoFile = File.createTempFile(
                "IMG_${System.currentTimeMillis()}",
                ".jpg",
                context.cacheDir
            )

            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            val result = suspendCancellableCoroutine<ImageCapture.OutputFileResults?> { cont ->
                imageCapture.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(results: ImageCapture.OutputFileResults) {
                            cont.resume(results)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            android.util.Log.e("CameraX", "Failed to capture", exception)
                            cont.resume(null)
                        }
                    }
                )
            }

            if (result != null && photoFile.exists()) {
                val uri = saveToMediaStore(context, photoFile)
                photoFile.delete()
                uri
            } else {
                null
            }
        } catch (e: Exception) {
            android.util.Log.e("CameraX", "Error taking photo", e)
            null
        }
    }
}

fun saveToMediaStore(context: Context, file: File): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/KameraKu")
        }
    }

    val resolver = context.contentResolver
    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    return uri?.let {
        resolver.openOutputStream(it)?.use { outputStream ->
            file.inputStream().use { inputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        it
    }
}

fun loadThumbnail(context: Context, uri: Uri): Bitmap? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { stream ->
            val options = BitmapFactory.Options().apply {
                inSampleSize = 4
            }
            BitmapFactory.decodeStream(stream, null, options)
        }
    } catch (e: Exception) {
        android.util.Log.e("CameraX", "Failed to load thumbnail: ${e.message}")
        null
    }
}
