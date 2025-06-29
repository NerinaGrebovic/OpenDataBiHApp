package com.example.opendatabih.presentation.screen.valid

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.opendatabih.presentation.navigation.NavRoutes
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ValidTravelDocumentsScreen(
    viewModel: ValidTravelDocumentsViewModel,
    navController: NavHostController,
    padding: PaddingValues
) {
    val filtered by viewModel.filtered.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val sortOption by viewModel.sortOption.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val sortOptions = listOf("Naziv (A-Z)", "Naziv (Z-A)", "Najviše muških", "Najviše ženskih", "Najviše ukupno")
    var sortExpanded by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Važeće putne isprave", fontWeight = FontWeight.Bold, color = Color(0xFFBFD7ED)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF000814))
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colors = listOf(Color(0xFF000814), Color(0xFF001D3D))))
                .padding(paddingValues)
                .padding(16.dp)
        ) {


            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                label = { Text("Pretraži po instituciji") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF003566),
                    unfocusedContainerColor = Color(0xFF003566),
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color(0xFF8DA9C4),
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color(0xFF8DA9C4),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            // 🔽 Sortiranje
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                OutlinedButton(
                    onClick = { sortExpanded = !sortExpanded },
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFFFFC300)
                    )
                ) {
                    Text("Sortiraj: $sortOption")
                }

                DropdownMenu(
                    expanded = sortExpanded,
                    onDismissRequest = { sortExpanded = false },
                    modifier = Modifier.background(Color(0xFF003566))
                ) {
                    sortOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = Color.White) },
                            onClick = {
                                viewModel.updateSortOption(option)
                                viewModel.applyAllFilters()
                                sortExpanded = false
                            }
                        )
                    }
                }
            }

            SwipeRefresh(
                state = rememberSwipeRefreshState(isRefreshing),
                onRefresh = { isRefreshing = true }
            ) {
                LaunchedEffect(isRefreshing) {
                    if (isRefreshing) {
                        viewModel.fetchDocuments()
                        isRefreshing = false
                    }
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else if (error != null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Greška: $error", color = MaterialTheme.colorScheme.error)
                    }
                } else if (filtered.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("📭 Nema rezultata za prikaz.", color = Color.White)
                    }
                } else {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        itemsIndexed(filtered) { index, doc ->
                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + expandVertically()
                            ) {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFF003566)),
                                    elevation = CardDefaults.cardElevation(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .clickable {
                                            navController.navigate(NavRoutes.validTravelDetailRoute(doc.institution))
                                        }
                                        .animateContentSize()
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(80.dp)
                                                .height(4.dp)
                                                .background(Color(0xFFFFC300))
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = doc.institution,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "Muških: ${doc.maleTotal} | Ženskih: ${doc.femaleTotal} | Ukupno: ${doc.total}",
                                            fontSize = 14.sp,
                                            color = Color(0xFFBFD7ED)
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "Kliknite za više detalja",
                                            fontSize = 12.sp,
                                            color = Color.LightGray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
