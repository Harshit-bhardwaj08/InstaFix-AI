package project.hackmol.hackmolinstafix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import project.hackmol.hackmolinstafix.ui.screens.components.BottomNavigationBar
import project.hackmol.hackmolinstafix.ui.theme.primaryColor

@Composable
fun EcoImpactScreen(
    navController: NavHostController
) {
    val primaryPurple = Color(0xFF9575CD)
    val lightPurple = Color(0xFFE6E0F8)

    // Dummy data for carbon savings
    val savedCO2 = 45.8 // in kg
    val savedWater = 2380 // in liters
    val savedElectricity = 67.2 // in kWh
    val treesEquivalent = 2.1
    val repairsCompleted = 3

    val systemUiController = rememberSystemUiController()

    SideEffect {
        // Hide status bar and set background to match your theme
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = false)
        systemUiController.isStatusBarVisible = false
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .background(Color.White)
            ) {
                // Top App Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(primaryPurple)
                        .padding(vertical = 16.dp)
                ) {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Text(
                        text = "Eco Impact",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

            }
        },
        bottomBar = {
            BottomNavigationBar(
                primaryColor = primaryColor,
                modifier = Modifier,
                navController = navController
            )
        }
    ) {
            paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Your Impact Summary
            Text(
                text = "Your Environmental Impact",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "By completing $repairsCompleted repairs, you've saved:",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Stats Cards
            EcoStatCard(
                title = "CO₂ Emissions Saved",
                value = "$savedCO2 kg",
                description = "Equivalent to ${
                    treesEquivalent.toString().replace(".0", "")
                } trees absorbing CO₂ for a month"
            )

            Spacer(modifier = Modifier.height(16.dp))

            EcoStatCard(
                title = "Water Saved",
                value = "$savedWater L",
                description = "Water that would have been used in manufacturing new products"
            )

            Spacer(modifier = Modifier.height(16.dp))

            EcoStatCard(
                title = "Energy Saved",
                value = "$savedElectricity kWh",
                description = "Electricity that would have been used to produce new items"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Next Milestone Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = lightPurple
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Next Eco Milestone",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Complete 2 more repairs to earn your 'Planet Protector' badge",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )

                    LinearProgressIndicator(
                        progress = 0.6f,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .height(8.dp)
                            .fillMaxWidth(),
                        color = primaryPurple,
                        trackColor = Color.White
                    )

                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryPurple
                        )
                    ) {
                        Text("Share Your Impact")
                    }
                }
            }
        }
    }
}


@Composable
fun EcoStatCard(title: String, value: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(42.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EcoImpactScreenPreview() {
    EcoImpactScreen(rememberNavController())
}