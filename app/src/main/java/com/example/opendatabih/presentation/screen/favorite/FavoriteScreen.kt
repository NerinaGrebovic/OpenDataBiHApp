package com.example.opendatabih.presentation.screen.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.opendatabih.data.local.entity.FavoriteEntity

@Composable
fun FavoriteScreen(
    favoriteViewModel: FavoriteViewModel,
    onLostDocumentClick: (FavoriteEntity) -> Unit,
    onValidDocumentClick: (FavoriteEntity) -> Unit,
    padding: PaddingValues
) {
    val favorites = favoriteViewModel.favorites.collectAsState().value

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF000814), Color(0xFF001D3D))
                )
            )
            .padding(padding)
    ) {
        val maxContentWidth = if (this.maxWidth < 600.dp) this.maxWidth else 600.dp

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\ud83d\udcec Nema spremljenih favorita.",
                    color = Color.LightGray,
                    fontSize = 18.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = maxContentWidth)
                    .align(Alignment.Center)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favorites) { favorite ->
                    FavoriteItem(
                        favorite = favorite,
                        onDelete = { favoriteViewModel.deleteFavorite(favorite) },
                        onItemClick = {
                            if (favorite.type == "lost") {
                                onLostDocumentClick(favorite)
                            } else if (favorite.type == "valid") {
                                onValidDocumentClick(favorite)
                            }
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}

@Composable
fun FavoriteItem(
    favorite: FavoriteEntity,
    onDelete: () -> Unit,
    onItemClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF003566)),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = favorite.institution,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = favorite.details,
                    fontSize = 14.sp,
                    color = Color(0xFFBFD7ED)
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Obri\u0161i",
                    tint = Color(0xFFFFC300)
                )
            }
        }
    }
}
