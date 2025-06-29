package com.example.opendatabih.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.opendatabih.presentation.components.LostDocumentsFloatingModal
import com.example.opendatabih.presentation.components.ValidDocumentsFloatingModal
import com.example.opendatabih.presentation.navigation.NavRoutes
import com.example.opendatabih.presentation.screen.lost.LostDocumentsViewModel
import com.example.opendatabih.presentation.screen.valid.ValidTravelDocumentsViewModel

@Composable
fun HomeScreen(
    navController: NavHostController,
    padding: PaddingValues,
    lostViewModel: LostDocumentsViewModel,
    validViewModel: ValidTravelDocumentsViewModel
) {
    var showLostModal by remember { mutableStateOf(false) }
    var showValidModal by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF000814), Color(0xFF001D3D))
                )
            )
    ) {
        val maxContentWidth = if (this.maxWidth < 600.dp) this.maxWidth else 600.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = maxContentWidth)
                .align(Alignment.Center)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "OpenDataBiH",
                color = Color(0xFFBFD7ED),
                fontSize = 40.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(6.dp))

            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFFFC300))
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Pristup javnim podacima BiH",
                color = Color(0xFF8DA9C4),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(40.dp))

            HomeMenuCard(
                title = "Izgubljeni dokumenti",
                onClick = { showLostModal = true }
            )

            Spacer(modifier = Modifier.height(20.dp))

            HomeMenuCard(
                title = "Važeće putne isprave",
                onClick = { showValidModal = true }
            )
        }

        if (showLostModal) {
            LostDocumentsFloatingModal(
                onConfirm = { selectedViewType, selectedCanton, selectedEntity ->
                    lostViewModel.updateSelectedViewType(selectedViewType)
                    lostViewModel.updateSelectedCanton(selectedCanton)
                    lostViewModel.updateSelectedEntity(selectedEntity)
                    lostViewModel.applyAllFilters()
                    showLostModal = false
                    navController.navigate(NavRoutes.LOST_DOCUMENTS)
                },
                onDismiss = { showLostModal = false }
            )
        }

        if (showValidModal) {
            ValidDocumentsFloatingModal(
                onConfirm = { selectedViewType, selectedCanton, selectedEntity ->
                    validViewModel.updateSelectedViewType(selectedViewType)
                    validViewModel.updateSelectedCanton(selectedCanton)
                    validViewModel.updateSelectedEntity(selectedEntity)
                    validViewModel.applyAllFilters()
                    showValidModal = false
                    navController.navigate(NavRoutes.VALID_TRAVEL)
                },
                onDismiss = { showValidModal = false }
            )
        }
    }
}

@Composable
fun HomeMenuCard(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF003566)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .border(
                    width = 1.5.dp,
                    color = Color(0xFFFFC300),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}
