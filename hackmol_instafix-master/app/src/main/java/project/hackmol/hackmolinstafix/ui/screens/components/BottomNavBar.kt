package project.hackmol.hackmolinstafix.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import project.hackmol.hackmolinstafix.navigation.Screen


@Composable
fun BottomNavigationBar(
    primaryColor: Color,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        color = Color.White,
        shadowElevation = 10.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = { Icons.FixIcon(tint = primaryColor) },
                label = "AI Fix",
                isSelected = true,
                primaryColor = primaryColor,
                onclick = {
                    navController.navigate(Screen.AIDiagnosis.route)
                }
            )

            BottomNavItem(
                icon = { Icons.ProIcon(tint = Color.Gray) },
                label = "Book Pro",
                isSelected = false,
                primaryColor = primaryColor,
                onclick = {
                    navController.navigate(Screen.BookPro.route)
                }

            )

            BottomNavItem(
                icon = { Icons.TrackIcon(tint = Color.Gray) },
                label = "Track",
                isSelected = false,
                primaryColor = primaryColor,
                onclick = {
                    navController.navigate(Screen.TrackRepair.route)
                }
            )

            BottomNavItem(
                icon = { Icons.ProfileIcon(tint = Color.Gray) },
                label = "Profile",
                isSelected = false,
                primaryColor = primaryColor,
                onclick = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: @Composable () -> Unit,
    label: String,
    isSelected: Boolean,
    primaryColor: Color,
    onclick : () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable(
            enabled = true,
            onClick = {
                onclick()
            }
        )
    ) {
        icon()

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = if (isSelected) primaryColor else Color.Gray,
            fontSize = 12.sp
        )
    }
}

object Icons {
    @Composable
    fun AIIcon(tint: Color = Color(0xFF8B5CF6)) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_add),
            contentDescription = "AI Diagnosis",
            tint = tint
        )
    }

    @Composable
    fun ProIcon(tint: Color = Color(0xFF8B5CF6)) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_manage),
            contentDescription = "Book a Pro",
            tint = tint
        )
    }

    @Composable
    fun TrackIcon(tint: Color = Color(0xFF8B5CF6)) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_mylocation),
            contentDescription = "Track Repair",
            tint = tint
        )
    }

    @Composable
    fun EcoIcon(tint: Color = Color(0xFF8B5CF6)) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_save),
            contentDescription = "Eco Impact",
            tint = tint
        )
    }

    @Composable
    fun FixIcon(tint: Color = Color(0xFF8B5CF6)) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_help),
            contentDescription = "AI Fix",
            tint = tint
        )
    }

    @Composable
    fun ProfileIcon(tint: Color = Color(0xFF8B5CF6)) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_myplaces),
            contentDescription = "Profile",
            tint = tint
        )
    }
}