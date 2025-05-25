package project.hackmol.hackmolinstafix.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack 
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import project.hackmol.hackmolinstafix.ui.screens.components.BottomNavigationBar
import project.hackmol.hackmolinstafix.ui.theme.primaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookProScreen(
    navController: NavHostController
) {

    val systemUiController = rememberSystemUiController()

    SideEffect {
        // Hide status bar and set background to match your theme
        systemUiController.setStatusBarColor(Color.Transparent, darkIcons = false)
        systemUiController.isStatusBarVisible = false
    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Book a Pro") },
                navigationIcon = {
                    IconButton (onClick = {
                        navController.navigateUp()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
               colors = TopAppBarDefaults.topAppBarColors(
                   containerColor = primaryColor
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
        ) {
            Text(
                text = "Find verified professionals nearby",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SearchFilterComponent()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Available Professionals",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            ProfessionalsList(professionals = getSampleProfessionals())
        }
    }
}

@Composable
fun SearchFilterComponent() {
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        placeholder = { Text("Search by repair type or location") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            text = "All",
            selected = true,
            onClick = { }
        )
        FilterChip(
            text = "Electronics",
            selected = false,
            onClick = { }
        )
        FilterChip(
            text = "Appliances",
            selected = false,
            onClick = { }
        )
    }
}

@Composable
fun FilterChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.3f),
        contentColor = if (selected) Color.White else Color.Black,
        modifier = Modifier.height(32.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(
                text = text,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun ProfessionalsList(professionals: List<Professional>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(professionals) { professional ->
            ProfessionalCard(professional = professional)
        }
    }
}

@Composable
fun ProfessionalCard(professional: Professional) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image Placeholder
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = professional.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Text(
                    text = professional.specialty,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    RatingBar(rating = professional.rating)
                    Text(
                        text = "(${professional.reviewCount})",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }

                Text(
                    text = "${professional.distance} miles away",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Button(
                onClick = { /* Navigate to booking */ },
                modifier = Modifier.width(90.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Book", fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun RatingBar(rating: Float) {
    Row {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = if (index < rating) Color(0xFFFFCC00) else Color.LightGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

data class Professional(
    val name: String,
    val specialty: String,
    val rating: Float,
    val reviewCount: Int,
    val distance: Double
)

fun getSampleProfessionals(): List<Professional> {
    return listOf(
        Professional("John Smith", "Electronics Repair", 4.8f, 124, 0.7),
        Professional("Sarah Johnson", "Appliance Technician", 4.9f, 87, 1.2),
        Professional("Mike Williams", "Computer Repair", 4.5f, 56, 2.3),
        Professional("Lisa Brown", "Phone Screen Expert", 4.7f, 93, 3.1)
    )
}


@Preview
@Composable
fun BookScreenPreview(){
    BookProScreen (rememberNavController( ))
}