package com.example.opendatabih.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.verticalScroll

@Composable
fun LostDocumentsFloatingModal(
    onConfirm: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val modalWidth = if (screenWidth < 360) 280.dp else if (screenWidth < 600) 320.dp else 400.dp
    val titleFontSize = if (screenWidth < 360) 18.sp else if (screenWidth < 600) 22.sp else 26.sp
    val optionFontSize = if (screenWidth < 360) 14.sp else if (screenWidth < 600) 18.sp else 22.sp
    val buttonFontSize = if (screenWidth < 360) 14.sp else if (screenWidth < 600) 16.sp else 18.sp

    val viewOptions = listOf(
        "Prikaži sve dokumente",
        "Prikaži samo preko 100 izgubljenih",
        "Prikaži samo manje od 100 izgubljenih"
    )

    val cantons = listOf(
        "Svi kantoni",
        "Unsko-sanski kanton",
        "Posavski kanton",
        "Tuzlanski kanton",
        "Zeničko-dobojski kanton",
        "Bosansko-podrinjski kanton",
        "Srednjobosanski kanton",
        "Hercegovačko-neretvanski kanton",
        "Zapadnohercegovački kanton",
        "Kanton Sarajevo",
        "Kanton 10"
    )

    val entities = listOf(
        "Svi entiteti",
        "Federacija Bosne i Hercegovine",
        "Republika Srpska",
        "Brčko Distrikt Bosne i Hercegovine"
    )

    var selectedViewType by remember { mutableStateOf(viewOptions[0]) }
    var selectedCanton by remember { mutableStateOf(cantons[0]) }
    var selectedEntity by remember { mutableStateOf(entities[0]) }

    val isCantonVisible = selectedEntity == "Federacija Bosne i Hercegovine"

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF001D3D)),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier
                .padding(32.dp)
                .width(modalWidth)
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(scrollState)
            ) {
                Text(
                    text = "Filtriraj dokumente",
                    fontSize = titleFontSize,
                    color = Color(0xFFBFD7ED),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                viewOptions.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedViewType = option }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (selectedViewType == option),
                            onClick = { selectedViewType = option },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF005BBB),
                                unselectedColor = Color.LightGray
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(option, fontSize = optionFontSize, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (isCantonVisible) {
                    Text("Kanton", fontSize = optionFontSize, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))
                    DropdownMenuBox(cantons, selectedCanton) { selectedCanton = it }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Text("Entitet", fontSize = optionFontSize, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                DropdownMenuBox(entities, selectedEntity) { selectedEntity = it }
                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onDismiss) {
                        Text("Otkaži", fontSize = buttonFontSize, color = Color(0xFF8DA9C4))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { onConfirm(selectedViewType, selectedCanton, selectedEntity) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF005BBB)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Primijeni", fontSize = buttonFontSize, color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuBox(items: List<String>, selectedItem: String, onItemSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent)
        ) {
            Text(selectedItem, color = Color.White)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF003566))
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, color = Color.White) },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
