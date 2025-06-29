package com.example.opendatabih.presentation.screen.valid

import android.content.Intent
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opendatabih.data.local.entity.FavoriteEntity
import com.example.opendatabih.presentation.screen.favorite.FavoriteViewModel
import com.example.opendatabih.presentation.screen.valid.ValidTravelDocumentsViewModel

@Composable
fun ValidTravelDetailScreen(
    institution: String,
    viewModel: ValidTravelDocumentsViewModel,
    favoriteViewModel: FavoriteViewModel,
    padding: PaddingValues
) {
    val documents = viewModel.documents.collectAsState().value
    val document = documents.find { it.institution == institution }
    val context = LocalContext.current

    val favorites = favoriteViewModel.favorites.collectAsState().value
    val isFavorite = favorites.any { it.institution == institution }

    var localIsFav by remember { mutableStateOf(isFavorite) }

    LaunchedEffect(isFavorite) {
        localIsFav = isFavorite
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF000814), Color(0xFF001D3D))
                )
            )
            .padding(padding)
    ) {

        val maxContentWidth = if (this.maxWidth < 600.dp) this.maxWidth else 600.dp

        if (document == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Podaci nisu pronađeni.",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        } else {

            val maxTotal = documents.maxOfOrNull { it.total } ?: 100
            val progress = document.total / maxTotal.toFloat()

            val animatedTotal by animateIntAsState(
                targetValue = document.total,
                animationSpec = tween(durationMillis = 1000),
                label = "Animacija broja"
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = maxContentWidth)
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item { ValidInfoCard("Institucija", document.institution) }
                item { ValidInfoCard("Muških", "${document.maleTotal}") }
                item { ValidInfoCard("Ženskih", "${document.femaleTotal}") }
                item { ValidInfoCard("Ukupno", "$animatedTotal") }
                item { ValidInfoCard("Entitet", document.entity) }
                item { ValidInfoCard("Kanton", document.canton) }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                    Text(
                        text = "Status važećih dokumenata",
                        color = Color(0xFFBFD7ED),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        color = Color(0xFFFFC300),
                        trackColor = Color(0xFF8DA9C4)
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "${document.total} od maksimalnih $maxTotal važećih dokumenata",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {
                                if (localIsFav) {
                                    favoriteViewModel.deleteFavoriteByInstitution(institution)
                                } else {
                                    favoriteViewModel.addToFavorites(
                                        FavoriteEntity(
                                            institution = document.institution,
                                            details = "Muških: ${document.maleTotal}, Ženskih: ${document.femaleTotal}, Ukupno: ${document.total}",
                                            type="valid"
                                        )
                                    )
                                }
                                localIsFav = !localIsFav
                            },
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF003566))
                        ) {
                            Icon(
                                imageVector = if (localIsFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorit",
                                tint = if (localIsFav) Color(0xFFFFC300) else Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(24.dp))

                        IconButton(
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    setType("text/plain")
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "Podaci o: ${document.institution}\nMuških: ${document.maleTotal}\nŽenskih: ${document.femaleTotal}\nUkupno: ${document.total}\nEntitet: ${document.entity}\nKanton: ${document.canton}"
                                    )
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Podijeli putem"))
                            },
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF003566))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Podijeli",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ValidInfoCard(label: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF003566)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = label,
                color = Color(0xFF8DA9C4),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
