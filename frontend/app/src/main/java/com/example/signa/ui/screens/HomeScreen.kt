package com.example.signa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.style.TextAlign

// ─── Cores base ──────────────────────────────────────────────────────────────
val SignaBg       = Color(0xFFF3F2F8)
val SignaPurple   = Color(0xFF5E35B1)
val SignaTextDark = Color(0xFF333333)
val SignaTextGray = Color(0xFF757575)

// ─── Paletas por status ───────────────────────────────────────────────────────
val ColorWorsening         = Color(0xFFE53935)
val ColorWorseningLight    = Color(0xFFFCE4EC)
val ColorWorseningRing     = Color(0xFFEF9A9A)

val ColorPossible          = Color(0xFFF9A825)
val ColorPossibleLight     = Color(0xFFFFF8E1)
val ColorPossibleRing      = Color(0xFFFFCC80)

val ColorStable            = Color(0xFF1565C0)
val ColorStableLight       = Color(0xFFE3F2FD)
val ColorStableRing        = Color(0xFF90CAF9)

// ─── Modelo de dados ──────────────────────────────────────────────────────────
data class Symptom(
    val name: String,
    val timeAgo: String,
    val isUrgent: Boolean = false,
    val isResolved: Boolean = false
)

data class PatientData(
    val id: String,
    val stage: String,
    val age: Int,
    val mlPrediction: Int,
    val symptoms: List<Symptom>
)

sealed class PatientStatus(
    val mainColor: Color,
    val lightColor: Color,
    val ringColor: Color,
    val title: String,
    val description: String,
    val icon: ImageVector
) {
    object Worsening : PatientStatus(
        mainColor   = ColorWorsening,
        lightColor  = ColorWorseningLight,
        ringColor   = ColorWorseningRing,
        title       = "Agravamento",
        description = "Sintomas intensificados nas últimas 24h.",
        icon        = Icons.Default.TrendingUp
    )
    object PossibleWorsening : PatientStatus(
        mainColor   = ColorPossible,
        lightColor  = ColorPossibleLight,
        ringColor   = ColorPossibleRing,
        title       = "Possível Agravamento",
        description = "Notamos variações nos seus sintomas hoje.",
        icon        = Icons.Default.Warning
    )
    object Stable : PatientStatus(
        mainColor   = ColorStable,
        lightColor  = ColorStableLight,
        ringColor   = ColorStableRing,
        title       = "Estável",
        description = "Sua tendência está positiva nas últimas 24h.",
        icon        = Icons.Default.TrendingUp
    )
}

fun resolveStatus(mlPrediction: Int): PatientStatus = when {
    mlPrediction >= 60 -> PatientStatus.Worsening
    mlPrediction in 30..59 -> PatientStatus.PossibleWorsening
    else -> PatientStatus.Stable
}

// ─── Modifier neumórfico ──────────────────────────────────────────────────────
fun Modifier.neumorphicCircle(
    lightColor: Color = Color(0xFFFFFFFF),
    darkColor: Color  = Color(0xFFC8C4DC),
    radius: Float     = 60f,
    offset: Float     = 14f
): Modifier = this.drawBehind {
    val cx = size.width / 2f
    val cy = size.height / 2f
    val r  = size.minDimension / 2f
    listOf(
        Triple(-offset, -offset, lightColor),
        Triple(offset,  offset,  darkColor)
    ).forEach { (dx, dy, color) ->
        drawIntoCanvas { canvas ->
            canvas.drawCircle(
                center = Offset(cx, cy),
                radius = r,
                paint  = Paint().apply {
                    asFrameworkPaint().apply {
                        isAntiAlias = true
                        this.color  = android.graphics.Color.TRANSPARENT
                        setShadowLayer(radius, dx, dy, color.toArgb())
                    }
                }
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
//  HomeScreen
// ═══════════════════════════════════════════════════════════════════════════════
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    patient: PatientData = samplePatient()
) {
    val status = resolveStatus(patient.mlPrediction)

    Scaffold(
        containerColor = SignaBg,
        topBar    = { SignaTopBar(status = status) },
        bottomBar = { SignaBottomNavigation() }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            StatusCircle(status = status)

            Spacer(Modifier.height(20.dp))

            MlPredictionCard(status = status, value = patient.mlPrediction)

            Spacer(Modifier.height(12.dp))

            // Passando a função de clique para o Card
            PatientInfoCard(
                patient = patient,
                onEditClick = { navController.navigate("pacientes") }
            )

            Spacer(Modifier.height(12.dp))

            SymptomsSection(status = status, symptoms = patient.symptoms)

            Spacer(Modifier.height(16.dp))

            ContactTeamButton()

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ─── TopBar ───────────────────────────────────────────────────────────────────
@Composable
fun SignaTopBar(status: PatientStatus) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFD4A0A0)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(10.dp))
            Text("Signa", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SignaPurple)
        }

        Box(
            modifier = Modifier
                .size(38.dp)
                .shadow(4.dp, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, tint = SignaPurple, modifier = Modifier.size(20.dp))
        }
    }
}

// ─── Círculo de Status ────────────────────────────────────────────────────────
@Composable
fun StatusCircle(status: PatientStatus) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .neumorphicCircle(radius = 50f, offset = 14f)
                .clip(CircleShape)
                .background(Brush.radialGradient(listOf(status.lightColor, SignaBg), radius = 500f))
                .border(6.dp, status.ringColor.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(0.76f)
                    .shadow(20.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                // horizontalAlignment centraliza os itens verticalmente na coluna
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Icon(
                        imageVector = status.icon,
                        contentDescription = null,
                        tint = status.mainColor,
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = status.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = status.mainColor,
                        textAlign = TextAlign.Center // Centraliza se o texto quebrar linha
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = status.description,
                        fontSize = 13.sp,
                        color = SignaTextGray,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center // Centraliza a descrição
                    )
                }
            }
        }
    }
}

// ─── Card: Predição do ML ─────────────────────────────────────────────────────
@Composable
fun MlPredictionCard(status: PatientStatus, value: Int) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Predição do ML", fontSize = 13.sp, color = SignaTextGray)
                Icon(Icons.Default.BarChart, null, tint = status.mainColor, modifier = Modifier.size(20.dp))
            }
            Text("$value", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = SignaTextDark)
            LinearProgressIndicator(
                progress = value / 100f,
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = status.mainColor,
                trackColor = status.lightColor
            )
        }
    }
}

// ─── Card: Info do Paciente (MODIFICADO PARA BOTÃO) ────────────────────────────
@Composable
fun PatientInfoCard(patient: PatientData, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Paciente #${patient.id}", fontSize = 12.sp, color = SignaTextGray)
                Spacer(Modifier.height(2.dp))
                Text(patient.stage, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = SignaTextDark)
                Text("Idade: ${patient.age}", fontSize = 13.sp, color = SignaTextGray)
            }

            // Transformado em IconButton para ter semântica de clique
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Trocar Paciente",
                    tint = SignaPurple,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ─── Seção de Sintomas ───────────────────────────────────────────────────────
@Composable
fun SymptomsSection(status: PatientStatus, symptoms: List<Symptom>) {
    val sectionTitle = if (status is PatientStatus.Worsening) "Variáveis de Impacto" else "Sintomas Reportados"

    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(sectionTitle, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = SignaTextDark)
                Text("Novo Registro", fontSize = 12.sp, color = SignaPurple, modifier = Modifier.clickable { })
            }
            Spacer(Modifier.height(12.dp))
            symptoms.forEachIndexed { idx, symptom ->
                SymptomRow(symptom, status)
                if (idx < symptoms.lastIndex) HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color(0xFFF0F0F0))
            }
        }
    }
}

@Composable
fun SymptomRow(symptom: Symptom, status: PatientStatus) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                .background(if (symptom.isResolved) Color(0xFFE8F5E9) else if (symptom.isUrgent) status.lightColor else Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            val icon = when {
                symptom.isResolved -> Icons.Default.CheckCircle
                symptom.isUrgent -> Icons.Default.PriorityHigh
                else -> Icons.Default.Bed
            }
            Icon(icon, null, tint = if(symptom.isResolved) Color(0xFF4CAF50) else if(symptom.isUrgent) status.mainColor else SignaTextGray, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(symptom.name, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = SignaTextDark)
            Text(symptom.timeAgo, fontSize = 12.sp, color = SignaTextGray)
        }
        Icon(Icons.Default.ChevronRight, null, tint = SignaTextGray, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun ContactTeamButton() {
    OutlinedButton(
        onClick = {},
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
    ) {
        Icon(Icons.Default.Phone, null, tint = SignaPurple, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text("Contactar Equipe Médica de Plantão", color = SignaPurple, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun SignaBottomNavigation() {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Dashboard, null) }, label = { Text("Dashboard") })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Assignment, null) }, label = { Text("Symptoms") })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.History, null) }, label = { Text("History") })
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Person, null) }, label = { Text("Profile") })
    }
}

fun samplePatient() = PatientData(
    id = "1048",
    stage = "Estágio II",
    age = 57,
    mlPrediction = 45,
    symptoms = listOf(
        Symptom("Náusea Severa", "Há 2 horas", isUrgent = true),
        Symptom("Fadiga", "Há 5 horas")
    )
)