package com.example.signa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.signa.data.model.PredicaoResponse
import com.example.signa.ui.viewmodel.ListaUiState
import com.example.signa.ui.viewmodel.PredicaoUiState
import com.example.signa.ui.viewmodel.PredicaoViewModel

val SignaBg       = Color(0xFFF3F2F8)
val SignaPurple   = Color(0xFF5E35B1)
val SignaTextDark = Color(0xFF333333)
val SignaTextGray = Color(0xFF757575)

val ColorWorsening      = Color(0xFFE53935)
val ColorWorseningLight = Color(0xFFFCE4EC)
val ColorWorseningRing  = Color(0xFFEF9A9A)
val ColorPossible       = Color(0xFFF9A825)
val ColorPossibleLight  = Color(0xFFFFF8E1)
val ColorPossibleRing   = Color(0xFFFFCC80)
val ColorStable         = Color(0xFF1565C0)
val ColorStableLight    = Color(0xFFE3F2FD)
val ColorStableRing     = Color(0xFF90CAF9)

sealed class PatientStatus(
    val mainColor: Color,
    val lightColor: Color,
    val ringColor: Color,
    val title: String,
    val description: String,
    val icon: ImageVector
) {
    object Worsening : PatientStatus(ColorWorsening, ColorWorseningLight, ColorWorseningRing,
        "Agravamento", "Sintomas intensificados nas últimas 24h.", Icons.Default.TrendingUp)
    object PossibleWorsening : PatientStatus(ColorPossible, ColorPossibleLight, ColorPossibleRing,
        "Possível Agravamento", "Notamos variações nos seus sintomas hoje.", Icons.Default.Warning)
    object Stable : PatientStatus(ColorStable, ColorStableLight, ColorStableRing,
        "Estável", "Sua tendência está positiva nas últimas 24h.", Icons.Default.TrendingUp)
}

fun resolveStatusFromRisco(nivelRisco: String): PatientStatus = when (nivelRisco) {
    "alto"     -> PatientStatus.Worsening
    "moderado" -> PatientStatus.PossibleWorsening
    else       -> PatientStatus.Stable
}

/** Converte o nivel_risco da API para texto exibível em português capitalizado */
fun formatarRisco(nivelRisco: String): String = when (nivelRisco.lowercase()) {
    "alto"     -> "Risco Alto"
    "moderado" -> "Risco Moderado"
    "baixo"    -> "Risco Baixo"
    else       -> nivelRisco.replaceFirstChar { it.uppercaseChar() }
}

fun Modifier.neumorphicCircle(lightColor: Color = Color.White, darkColor: Color = Color(0xFFC8C4DC),
    radius: Float = 60f, offset: Float = 14f): Modifier = this.drawBehind {
    val cx = size.width / 2f; val cy = size.height / 2f
    listOf(Triple(-offset, -offset, lightColor), Triple(offset, offset, darkColor))
        .forEach { (dx, dy, color) ->
            drawIntoCanvas { canvas ->
                canvas.drawCircle(Offset(cx, cy), size.minDimension / 2f,
                    Paint().apply { asFrameworkPaint().apply {
                        isAntiAlias = true
                        this.color = android.graphics.Color.TRANSPARENT
                        setShadowLayer(radius, dx, dy, color.toArgb())
                    }}
                )
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    vm: PredicaoViewModel = viewModel()
) {
    val predicaoState by vm.predicaoState.collectAsState()
    val listaState    by vm.listaState.collectAsState()

    LaunchedEffect(Unit) {
        if (predicaoState !is PredicaoUiState.Success) {
            vm.carregarHistorico()
        }
    }

    // Resolve a última predição.
    // Se variaveis_impacto vierem vazias da API (backend não salva no banco),
    // injeta do cache local (SharedPreferences) gravado no momento do POST.
    val ultimaPredicao: PredicaoResponse? = when {
        predicaoState is PredicaoUiState.Success -> {
            val data = (predicaoState as PredicaoUiState.Success).data
            if (data.variaveisImpacto.isEmpty())
                data.copy(variaveisImpacto = vm.getImpactosParaId(data.id))
            else data
        }
        listaState is ListaUiState.Success -> {
            val p = (listaState as ListaUiState.Success).data.firstOrNull()
            p?.copy(variaveisImpacto = p.variaveisImpacto.ifEmpty { vm.getImpactosParaId(p.id) })
        }
        else -> null
    }

    val status       = ultimaPredicao?.let { resolveStatusFromRisco(it.nivelRisco) } ?: PatientStatus.Stable
    val probabilidade = ultimaPredicao?.probabilidade?.toInt() ?: 0

    Scaffold(
        containerColor = SignaBg,
        topBar    = { SignaTopBar(status = status) },
        bottomBar = { SignaBottomNavigation(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(16.dp))

            if (listaState is ListaUiState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().clip(CircleShape),
                    color = SignaPurple
                )
                Spacer(Modifier.height(8.dp))
            }

            StatusCircle(status = status)
            Spacer(Modifier.height(20.dp))
            MlPredictionCard(status = status, value = probabilidade)
            Spacer(Modifier.height(12.dp))

            PatientInfoCard(
                nome        = ultimaPredicao?.pacienteNome ?: "—",
                // Exibe o nível de risco formatado em vez de "Dead"/"Alive"
                nivelRisco  = ultimaPredicao?.nivelRisco   ?: "",
                status      = status,
                onEditClick = {
                    navController.navigate("pacientes")
                    vm.resetarEstado()
                }
            )
            Spacer(Modifier.height(12.dp))

            if (ultimaPredicao != null) {
                VariaveisImpactoSection(predicao = ultimaPredicao, status = status)
            } else {
                EmptyStateCard()
            }

            Spacer(Modifier.height(12.dp))
            ContactTeamButton()
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun SignaTopBar(status: PatientStatus) {
    Row(
        modifier = Modifier.fillMaxWidth().statusBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(38.dp).clip(CircleShape).background(Color(0xFFD4A0A0)),
                contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, tint = Color.White, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(10.dp))
            Text("Signa", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SignaPurple)
        }
        Box(modifier = Modifier.size(38.dp).shadow(4.dp, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)).background(Color.White),
            contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Person, null, tint = SignaPurple, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun StatusCircle(status: PatientStatus) {
    Box(modifier = Modifier.fillMaxWidth().aspectRatio(1f).padding(12.dp),
        contentAlignment = Alignment.Center) {
        Box(modifier = Modifier.fillMaxSize()
            .neumorphicCircle(radius = 50f, offset = 14f)
            .clip(CircleShape)
            .background(Brush.radialGradient(listOf(status.lightColor, SignaBg), radius = 500f))
            .border(6.dp, status.ringColor.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center) {
            Box(modifier = Modifier.fillMaxSize(0.76f).shadow(20.dp, CircleShape)
                .clip(CircleShape).background(Color.White),
                contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 24.dp)) {
                    Icon(status.icon, null, tint = status.mainColor, modifier = Modifier.size(36.dp))
                    Spacer(Modifier.height(8.dp))
                    Text(status.title, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold,
                        color = status.mainColor, textAlign = TextAlign.Center)
                    Spacer(Modifier.height(6.dp))
                    Text(status.description, fontSize = 13.sp, color = SignaTextGray,
                        lineHeight = 18.sp, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun MlPredictionCard(status: PatientStatus, value: Int) {
    Card(modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Probabilidade de Risco", fontSize = 13.sp, color = SignaTextGray)
                Icon(Icons.Default.BarChart, null, tint = status.mainColor, modifier = Modifier.size(20.dp))
            }
            Text("$value%", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = SignaTextDark)
            LinearProgressIndicator(
                progress = value / 100f,
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = status.mainColor, trackColor = status.lightColor
            )
        }
    }
}

@Composable
fun PatientInfoCard(
    nome: String,
    nivelRisco: String,
    status: PatientStatus,
    onEditClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(nome, fontSize = 12.sp, color = SignaTextGray)
                Spacer(Modifier.height(4.dp))

                if (nivelRisco.isNotEmpty()) {
                    // Badge colorido com o nível de risco
                    Surface(
                        shape  = RoundedCornerShape(8.dp),
                        color  = status.lightColor,
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(status.mainColor)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text       = formatarRisco(nivelRisco),
                                fontSize   = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color      = status.mainColor
                            )
                        }
                    }
                } else {
                    Text("Aguardando predição", fontSize = 15.sp,
                        fontWeight = FontWeight.Bold, color = SignaTextDark)
                }
            }
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, "Nova Predição", tint = SignaPurple, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun VariaveisImpactoSection(predicao: PredicaoResponse, status: PatientStatus) {
    Card(modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Variáveis de Impacto", fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold, color = SignaTextDark)
                Text("ID: ${predicao.id.take(8)}…", fontSize = 11.sp, color = SignaTextGray)
            }
            Spacer(Modifier.height(4.dp))

            if (predicao.variaveisImpacto.isEmpty()) {
                Text("Nenhuma variável de risco crítico identificada.",
                    fontSize = 13.sp, color = SignaTextGray,
                    modifier = Modifier.padding(vertical = 8.dp))
            } else {
                predicao.variaveisImpacto.forEachIndexed { idx, vi ->
                    if (idx > 0) HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp),
                        color = Color(0xFFF0F0F0))
                    Row(modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                            .background(status.lightColor), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PriorityHigh, null, tint = status.mainColor,
                                modifier = Modifier.size(18.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(Modifier.weight(1f)) {
                            Text(vi.variavel, fontSize = 14.sp,
                                fontWeight = FontWeight.Medium, color = SignaTextDark)
                            Text("Valor: ${vi.valor}", fontSize = 12.sp, color = SignaTextGray)
                            Text(vi.hipotese, fontSize = 11.sp, color = SignaTextGray,
                                lineHeight = 15.sp, modifier = Modifier.padding(top = 2.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Text(predicao.aviso, fontSize = 10.sp, color = Color(0xFFBDBDBD), lineHeight = 14.sp)
        }
    }
}

@Composable
fun EmptyStateCard() {
    Card(modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Science, null, tint = SignaPurple.copy(alpha = 0.4f),
                modifier = Modifier.size(48.dp))
            Spacer(Modifier.height(8.dp))
            Text("Nenhuma predição realizada", fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold, color = SignaTextGray,
                textAlign = TextAlign.Center)
            Text("Toque no ✎ acima para inserir os dados clínicos do paciente.",
                fontSize = 12.sp, color = Color(0xFFBDBDBD), textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
fun ContactTeamButton() {
    val context = LocalContext.current
    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:192"))
            context.startActivity(intent)
        },
        modifier = Modifier.fillMaxWidth().height(52.dp),
        shape  = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = SignaPurple)
    ) {
        Icon(Icons.Default.Phone, null, tint = Color.White, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text("Contactar Equipe Médica de Plantão",
            color = Color.White, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun SignaBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = true,
            onClick  = {},
            icon     = { Icon(Icons.Default.Dashboard, null) },
            label    = { Text("Dashboard") }
        )
        NavigationBarItem(
            selected = false,
            onClick  = { navController.navigate("symptoms") { launchSingleTop = true } },
            icon     = { Icon(Icons.Default.Assignment, null) },
            label    = { Text("Symptoms") }
        )
        NavigationBarItem(
            selected = false,
            onClick  = { navController.navigate("history") { launchSingleTop = true } },
            icon     = { Icon(Icons.Default.History, null) },
            label    = { Text("History") }
        )
        NavigationBarItem(
            selected = false,
            onClick  = {},
            icon     = { Icon(Icons.Default.Person, null) },
            label    = { Text("Profile") }
        )
    }
}
