package project.hackmol.hackmolinstafix.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import project.hackmol.hackmolinstafix.tflite.ImageClassifier
import project.hackmol.hackmolinstafix.ui.screens.components.BottomNavigationBar
import project.hackmol.hackmolinstafix.ui.theme.primaryColor
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Data class to hold the diagnosis result.
 * (The cost values are mocked for demonstration purposes.)
 */
data class DiagnosisResult(
    val diagnosis: String,
    val repair_cost: Int,
    val handyman_cost: Int,
    val total_cost: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIDiagnosisScreen(
    navController: NavHostController
) {
    // Hide status bar
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = false)
        systemUiController.isStatusBarVisible = false
    }

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var hasPhoto by remember { mutableStateOf(false) }
    var diagnosisResult by remember { mutableStateOf<DiagnosisResult?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Check permissions for camera and gallery
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasGalleryPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    // Launcher for camera capture
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            hasPhoto = true
            showBottomSheet = false
        }
    }

    // Launcher for gallery selection
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            hasPhoto = true
            showBottomSheet = false
        }
    }

    // Request camera permission launcher
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasCameraPermission = isGranted
        if (isGranted) {
            val photoFile = createImageFile(context)
            val photoURI = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                photoFile
            )
            imageUri = photoURI
            cameraLauncher.launch(photoURI)
        }
    }

    // Request gallery permission launcher
    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasGalleryPermission = isGranted
        if (isGranted) {
            galleryLauncher.launch("image/*")
        }
    }

    // Function to perform local image analysis via the TFLite model
    suspend fun analyzeImageLocally(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        if (inputStream == null) {
            throw Exception("Could not open InputStream for the image URI.")
        }
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        val classifier = ImageClassifier(context)
        val result = classifier.classify(bitmap)
        classifier.close()

        return result
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Diagnosis") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                primaryColor = primaryColor,
                modifier = Modifier,
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title text
            Text(
                text = "Upload a photo for instant diagnosis",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Image upload card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (hasPhoto && imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Selected Image",
                        modifier = Modifier.fillMaxSize(),
                        alignment = Alignment.Center
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        FloatingActionButton(
                            onClick = { showBottomSheet = true },
                            containerColor = MaterialTheme.colorScheme.primary
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Upload Photo",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Diagnosis instructions component
            DiagnosisInstructionsComponent()

            Spacer(modifier = Modifier.height(32.dp))

            // Button to start diagnosis
            Button(
                onClick = {
                    if (hasPhoto && imageUri != null) {
                        coroutineScope.launch {
                            try {
                                val localResult = analyzeImageLocally(context, imageUri!!)
                                diagnosisResult = DiagnosisResult(
                                    diagnosis = localResult,
                                    repair_cost = 1200,
                                    handyman_cost = 500,
                                    total_cost = 1700
                                )
                                errorMessage = null
                            } catch (e: Exception) {
                                diagnosisResult = null
                                errorMessage = "Error during diagnosis: ${e.localizedMessage}"
                                e.printStackTrace()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = hasPhoto
            ) {
                Text("Start Diagnosis", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Show error message, if any
            errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Display diagnosis result, if available
            diagnosisResult?.let { result ->
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Diagnosis: ${result.diagnosis}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Repair Cost: ₹${result.repair_cost}",
                    fontSize = 16.sp
                )
                Text(
                    text = "Handyman Fee: ₹${result.handyman_cost}",
                    fontSize = 16.sp
                )
                Text(
                    text = "Total Cost: ₹${result.total_cost}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Bottom sheet for selecting image source (Camera or Gallery)
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = rememberModalBottomSheetState()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Choose an option",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Option: Take Photo
                    ListItem(
                        headlineContent = { Text("Take Photo") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Outlined.CameraAlt,
                                contentDescription = "Camera"
                            )
                        },
                        modifier = Modifier.clickable {
                            if (hasCameraPermission) {
                                val photoFile = createImageFile(context)
                                val photoURI = FileProvider.getUriForFile(
                                    context,
                                    context.packageName + ".provider",
                                    photoFile
                                )
                                imageUri = photoURI
                                cameraLauncher.launch(photoURI)
                            } else {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    )

                    // Option: Choose from Gallery
                    ListItem(
                        headlineContent = { Text("Choose from Gallery") },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Outlined.PhotoLibrary,
                                contentDescription = "Gallery"
                            )
                        },
                        modifier = Modifier.clickable {
                            if (hasGalleryPermission) {
                                galleryLauncher.launch("image/*")
                            } else {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                    galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                                } else {
                                    galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun DiagnosisInstructionsComponent() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Instructions",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "• Take a clear photo of the device or part that needs repair\n" +
                        "• Ensure good lighting for best results\n" +
                        "• Include any visible damage in the photo\n" +
                        "• Our AI will analyze and suggest repair options",
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

// Helper function to create a file for storing the camera image
private fun createImageFile(context: Context): File {
    return try {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = context.getExternalFilesDir("Images")
        File.createTempFile(imageFileName, ".jpg", storageDir)
    } catch (e: Exception) {
        val file = File(context.getExternalFilesDir("Images"), "temp_image.jpg")
        if (!file.exists()) {
            file.parentFile?.mkdirs()
            file.createNewFile()
        }
        file
    }
}

@Preview
@Composable
fun AiDiagnosisScreenPreview() {
    AIDiagnosisScreen(rememberNavController())
}
