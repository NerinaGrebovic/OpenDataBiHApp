import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.opendatabih.presentation.screen.lost.LostDocumentsViewModel
import com.example.opendatabih.presentation.screen.valid.ValidTravelDocumentsViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.delay

@Composable
fun StatisticsScreen(
    lostDocumentsViewModel: LostDocumentsViewModel,
    validTravelDocumentsViewModel: ValidTravelDocumentsViewModel,
    padding: PaddingValues
) {
    val lostDocuments = lostDocumentsViewModel.documents.collectAsState().value
    val validTravelDocuments = validTravelDocumentsViewModel.documents.collectAsState().value

    val totalLostPerInstitution = lostDocuments.mapIndexed { index, document ->
        BarEntry(index.toFloat(), document.lostCount.toFloat())
    }

    val institutionLabels = lostDocuments.map { it.institution }

    val totalMale = validTravelDocuments.sumOf { it.maleTotal }
    val totalFemale = validTravelDocuments.sumOf { it.femaleTotal }

    val years = listOf("2020", "2021", "2022", "2023", "2024")
    val issuedPerYear = listOf(5000f, 7000f, 8500f, 12000f, 14000f)
    val issuedEntries = issuedPerYear.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }

    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(300)
        isVisible = true
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

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = maxContentWidth)
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                ChartSection(
                    title = "Izgubljeni dokumenti po institucijama",
                    description = "Prikazuje broj izgubljenih dokumenata u različitim institucijama.",
                    chart = {
                        AndroidView(
                            factory = { context ->
                                BarChart(context).apply {
                                    val dataSet = BarDataSet(totalLostPerInstitution, "Broj izgubljenih")
                                    dataSet.colors = listOf(
                                        android.graphics.Color.rgb(0, 91, 187),
                                        android.graphics.Color.rgb(255, 213, 0)
                                    )
                                    dataSet.valueTextColor = android.graphics.Color.WHITE
                                    dataSet.valueTextSize = 14f

                                    data = BarData(dataSet)
                                    description.isEnabled = false
                                    legend.isEnabled = true
                                    legend.textColor = android.graphics.Color.WHITE
                                    legend.textSize = 12f

                                    xAxis.valueFormatter = IndexAxisValueFormatter(institutionLabels)
                                    xAxis.granularity = 1f
                                    xAxis.setDrawGridLines(false)
                                    xAxis.labelRotationAngle = -45f
                                    xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                                    xAxis.textColor = android.graphics.Color.WHITE
                                    xAxis.textSize = 12f

                                    axisLeft.textColor = android.graphics.Color.WHITE
                                    axisRight.textColor = android.graphics.Color.WHITE

                                    animateY(1000)
                                    invalidate()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                        )
                    },
                    isVisible = isVisible
                )
            }

            item {
                ChartSection(
                    title = "Polna struktura važećih dokumenata",
                    description = "Prikazuje omjer muških i ženskih korisnika važećih putnih isprava.",
                    chart = {
                        AndroidView(
                            factory = { context ->
                                PieChart(context).apply {
                                    val entries = listOf(
                                        PieEntry(totalMale.toFloat(), "Muški"),
                                        PieEntry(totalFemale.toFloat(), "Ženski")
                                    )

                                    val dataSet = PieDataSet(entries, "").apply {
                                        colors = listOf(
                                            android.graphics.Color.rgb(0, 91, 187),
                                            android.graphics.Color.rgb(255, 213, 0)
                                        )
                                        valueTextSize = 16f
                                        valueTextColor = android.graphics.Color.WHITE
                                        sliceSpace = 3f
                                    }

                                    data = PieData(dataSet)
                                    isDrawHoleEnabled = false
                                    description.isEnabled = false
                                    legend.isEnabled = true
                                    legend.textColor = android.graphics.Color.WHITE
                                    legend.textSize = 14f

                                    setEntryLabelColor(android.graphics.Color.WHITE)
                                    setEntryLabelTextSize(14f)

                                    animateY(1000)
                                    invalidate()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        )
                    },
                    isVisible = isVisible
                )
            }

            item {
                ChartSection(
                    title = "Broj izdatih dokumenata po godinama",
                    description = "Prikazuje godišnji trend izdavanja putnih dokumenata.",
                    chart = {
                        AndroidView(
                            factory = { context ->
                                BarChart(context).apply {
                                    val dataSet = BarDataSet(issuedEntries, "Broj izdatih")
                                    dataSet.color = android.graphics.Color.rgb(0, 191, 255)
                                    dataSet.valueTextColor = android.graphics.Color.WHITE
                                    dataSet.valueTextSize = 14f

                                    data = BarData(dataSet)
                                    description.isEnabled = false
                                    legend.isEnabled = true
                                    legend.textColor = android.graphics.Color.WHITE
                                    legend.textSize = 12f

                                    xAxis.valueFormatter = IndexAxisValueFormatter(years)
                                    xAxis.granularity = 1f
                                    xAxis.setDrawGridLines(false)
                                    xAxis.labelRotationAngle = -30f
                                    xAxis.position = com.github.mikephil.charting.components.XAxis.XAxisPosition.BOTTOM
                                    xAxis.textColor = android.graphics.Color.WHITE
                                    xAxis.textSize = 12f

                                    axisLeft.textColor = android.graphics.Color.WHITE
                                    axisRight.textColor = android.graphics.Color.WHITE

                                    animateY(1000)
                                    invalidate()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                        )
                    },
                    isVisible = isVisible
                )
            }

            item {
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

@Composable
fun ChartSection(title: String, description: String, chart: @Composable () -> Unit, isVisible: Boolean) {
    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = title,
        color = Color(0xFFBFD7ED),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )

    Box(
        modifier = Modifier
            .width(100.dp)
            .height(4.dp)
            .padding(top = 6.dp)
            .background(Color(0xFFFFC300))
    )

    Spacer(modifier = Modifier.height(12.dp))

    Text(
        text = description,
        color = Color(0xFF8DA9C4),
        fontSize = 16.sp
    )

    Spacer(modifier = Modifier.height(24.dp))

    AnimatedVisibility(visible = isVisible, enter = fadeIn()) {
        chart()
    }

    Spacer(modifier = Modifier.height(40.dp))
}
