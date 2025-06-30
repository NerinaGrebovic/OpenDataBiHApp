package com.example.opendatabih.presentation.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.opendatabih.R
import com.example.opendatabih.presentation.navigation.NavRoutes
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    val alphaAnim = remember { Animatable(0f) }
    val scaleAnim = remember { Animatable(0.85f) }
    val glowAnim = rememberInfiniteTransition()

    val glowAlpha by glowAnim.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        alphaAnim.animateTo(1f, animationSpec = tween(1200))
        scaleAnim.animateTo(1f, animationSpec = tween(1200, easing = FastOutSlowInEasing))
        delay(2800)
        navController.navigate(NavRoutes.HOME) {
            popUpTo(NavRoutes.SPLASH) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(Color(0xFF000814), Color(0xFF001D3D))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .background(
                    color = Color(0xFF00BFFF).copy(alpha = glowAlpha),
                    shape = RoundedCornerShape(50)
                )
                .blur(50.dp)
        )


        Image(
            painter = painterResource(id = R.drawable.logo_opendatabih),
            contentDescription = "OpenDataBiH logo",
            modifier = Modifier
                .size(140.dp)
                .scale(scaleAnim.value)
                .alpha(alphaAnim.value)
        )
    }
}
