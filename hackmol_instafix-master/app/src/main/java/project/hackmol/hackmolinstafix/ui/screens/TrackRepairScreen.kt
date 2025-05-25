package project.hackmol.hackmolinstafix.ui.screens
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import project.hackmol.hackmolinstafix.ui.theme.primaryColor
import project.hackmol.hackmolinstafix.ui.screens.components.BottomNavigationBar
import project.hackmol.hackmolinstafix.ui.screens.components.RequestLocationPermissions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackRepairScreen(
    navController: NavHostController,
    // Example: pass in current step/stage and lat/long from your ViewModel or arguments
    currentStep: Int = 2, // 0=Requested,1=EnRoute,2=InProgress,3=Completed
    userLat: Double = 28.6139,
    userLng: Double = 77.2090,
    handymanLat: Double = 28.7041,
    handymanLng: Double = 77.1025,

) {


    Log.d("TrackRepairMap", "User location: $userLat, $userLng")
    Log.d("TrackRepairMap", "Handyman location: $handymanLat, $handymanLng")


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Track Repair") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(primaryColor = primaryColor,
                modifier = Modifier,
                navController = navController)
        }
    ) { paddingValues ->
        RequestLocationPermissions {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 1) Steps / Timeline Section
                StepsSection(currentStep = currentStep)

                // 2) Map Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Fill remaining space
                ) {
                    TrackRepairMap(
                        userLat = userLat,
                        userLng = userLng,
                        handymanLat = handymanLat,
                        handymanLng = handymanLng
                    )
                }
            }
        }
    }
}

@Composable
fun StepsSection(currentStep: Int) {
    val steps = listOf(
        "Requested",
        "Handyman En Route",
        "Repair In Progress",
        "Completed"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Repair Status",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))

        steps.forEachIndexed { index, stepName ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Indicate current or completed step with color or icon
                val stepColor = if (index <= currentStep) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Step",
                    tint = stepColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stepName,
                    color = stepColor
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}


@Composable
fun TrackRepairMap(
    userLat: Double,
    userLng: Double,
    handymanLat: Double,
    handymanLng: Double
) {

    val safeHandymanLat = handymanLat.takeIf { it != 0.0 } ?: 28.7041
    val safeHandymanLng = handymanLng.takeIf { it != 0.0 } ?: 77.1025

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(safeHandymanLat, safeHandymanLng), 12f)
    }

    // Starting camera position (e.g., center between user and handyman)
    val context = LocalContext.current
    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = hasLocationPermission
        )
        ) {
        // Markers
        Marker(
            state = MarkerState(position = LatLng(userLat, userLng)),
            title = "Your Location",
            snippet = "Waiting for repair"
        )
        Marker(
            state = MarkerState(position = LatLng(handymanLat, handymanLng)),
            title = "Handyman",
            snippet = "On the way"
        )

        // You can add polylines or route lines from user location to handyman location if you have route data


    }
}

