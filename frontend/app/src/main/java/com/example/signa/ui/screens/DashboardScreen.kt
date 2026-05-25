package com.example.signa.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.signa.data.model.PredicaoRequest
import com.example.signa.data.model.PredicaoResponse
import com.example.signa.data.model.VariavelImpacto
import com.example.signa.ui.viewmodel.ListaUiState
import com.example.signa.ui.viewmodel.PredicaoUiState
import com.example.signa.ui.viewmodel.PredicaoViewModel

// ─── Tela: Dashboard do Paciente ─────────────────────────────────────────────

@Composable
fun DashboardScreen(
    navController: NavController,
    vm: PredicaoViewModel = viewModel()
) {
    val predicaoState by vm.predicaoState.collectAsState()
    val listaState    by vm.listaState.collectAsState()
    // Snapshot dos dados clínicos reais do paciente consultado
    val ultimoRequest by vm.ultimoRequest.collectAsState()

    LaunchedEffect(Unit) {
        if (predicaoState !is PredicaoUiState.Success) {
            vm.carregarHistorico()
        }
    }

    val ultimaPredicao: PredicaoResponse? = when {
        predicaoState is PredicaoUiState.Success -> {
            val d = (predicaoState as PredicaoUiState.Success).data
            if (d.variaveisImpacto.isEmpty()) d.copy(variaveisImpacto = vm.getImpactosParaId(d.id)) else d
        }
        listaState is ListaUiState.Success -> {
            val p = (listaState as ListaUiState.Success).data.firstOrNull()
            p?.copy(variaveisImpacto = p.variaveisImpacto.ifEmpty { vm.getImpactosParaId(p.id) })
        }
        else -> null
    }

    val status = ultimaPredicao?.let { resolveStatusFromRisco(it.nivelRisco) } ?: PatientStatus.Stable

    Scaffold(
        containerColor = SignaBg,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Dashboard do Paciente",
                    fontSize   = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color      = SignaPurple
                )
                IconButton(onClick = {
                    navController.navigate("pacientes")
                    vm.resetarEstado()
                }) {
                    Icon(Icons.Default.PersonAdd, "Nova Predição", tint = SignaPurple)
                }
            }
        },
        bottomBar = { DashboardBottomNavigation(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(8.dp))

            if (listaState is ListaUiState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().clip(CircleShape),
                    color    = SignaPurple
                )
                Spacer(Modifier.height(8.dp))
            }

            if (ultimaPredicao == null) {
                EmptyStateCard()
            } else {
                PatientHeaderCard(
                    predicao = ultimaPredicao,
                    request  = ultimoRequest,
                    status   = status
                )
                Spacer(Modifier.height(12.dp))

                RiskGaugeCard(
                    probabilidade = ultimaPredicao.probabilidade.toInt(),
                    nivelRisco    = ultimaPredicao.nivelRisco,
                    status        = status
                )
                Spacer(Modifier.height(12.dp))

                // Métricas rápidas — lidas do request real do paciente
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricCard(
                        label    = "Tamanho Tumor",
                        value    = ultimoRequest?.let { "${it.tumorSize} mm" } ?: "—",
                        icon     = Icons.Default.Biotech,
                        color    = status.mainColor,
                        light    = status.lightColor,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        label    = "Idade",
                        value    = ultimoRequest?.let { "${it.age} anos" } ?: "—",
                        icon     = Icons.Default.Person,
                        color    = SignaPurple,
                        light    = Color(0xFFEDE7F6),
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        label    = "Linfonodos+",
                        value    = ultimoRequest?.reginolNodePositive?.toString() ?: "—",
                        icon     = Icons.Default.Grain,
                        color    = Color(0xFF00897B),
                        light    = Color(0xFFE0F2F1),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(12.dp))

                // Dados clínicos — lidos do request real
                ClinicalDataCard(request = ultimoRequest)
                Spacer(Modifier.height(12.dp))

                if (ultimaPredicao.variaveisImpacto.isNotEmpty()) {
                    ImpactBarChartCard(
                        impactos = ultimaPredicao.variaveisImpacto,
                        status   = status
                    )
                    Spacer(Modifier.height(12.dp))
                }

                // Status hormonal — lido do request real
                HormonalStatusCard(request = ultimoRequest, status = status)
                Spacer(Modifier.height(12.dp))

                // Estadiamento — lido do request real
                StagingCard(request = ultimoRequest, status = status)
                Spacer(Modifier.height(12.dp))

                Text(
                    "As informações apresentadas são baseadas em modelo de machine learning (XGBoost) " +
                    "treinado com dados do SEER e têm caráter informativo. Não substituem avaliação médica.",
                    fontSize   = 10.sp,
                    color      = Color(0xFFBDBDBD),
                    lineHeight = 14.sp
                )
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

// ─── Card cabeçalho do paciente ───────────────────────────────────────────────

@Composable
private fun PatientHeaderCard(
    predicao: PredicaoResponse,
    request:  PredicaoRequest?,
    status:   PatientStatus
) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(20.dp)),
        shape    = RoundedCornerShape(20.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(status.mainColor.copy(alpha = 0.08f), Color.White)
                    )
                )
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar com inicial do nome
                Box(
                    modifier         = Modifier.size(56.dp).clip(CircleShape).background(status.lightColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        predicao.pacienteNome.take(1).uppercase(),
                        fontSize   = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = status.mainColor
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        predicao.pacienteNome,
                        fontSize   = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color      = SignaTextDark
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        // Badge raça — do request real
                        request?.race?.let { race ->
                            InfoBadge(
                                text  = when (race) {
                                    "White" -> "Branca"
                                    "Black" -> "Negra"
                                    else    -> "Outra"
                                },
                                color = SignaPurple,
                                light = Color(0xFFEDE7F6)
                            )
                        }
                        // Badge estado civil — do request real
                        request?.maritalStatus?.let { ms ->
                            InfoBadge(
                                text  = when (ms) {
                                    "Married"   -> "Casada"
                                    "Single"    -> "Solteira"
                                    "Divorced"  -> "Divorciada"
                                    "Widowed"   -> "Viúva"
                                    "Separated" -> "Separada"
                                    else        -> ms
                                },
                                color = Color(0xFF546E7A),
                                light = Color(0xFFECEFF1)
                            )
                        }
                        // Badge idade — do request real
                        request?.age?.let { age ->
                            InfoBadge(
                                text  = "$age anos",
                                color = Color(0xFF4527A0),
                                light = Color(0xFFEDE7F6)
                            )
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "ID: ${predicao.id.take(8)}…  •  ${formatDate(predicao.criadoEm)}",
                        fontSize = 11.sp,
                        color    = SignaTextGray
                    )
                }
                // Badge de nível de risco
                Surface(shape = RoundedCornerShape(12.dp), color = status.lightColor) {
                    Column(
                        modifier            = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(status.icon, null, tint = status.mainColor, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.height(2.dp))
                        Text(
                            formatarRisco(predicao.nivelRisco).replace(" ", "\n"),
                            fontSize   = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color      = status.mainColor,
                            textAlign  = TextAlign.Center,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}

// ─── Gauge circular de risco ──────────────────────────────────────────────────

@Composable
private fun RiskGaugeCard(
    probabilidade: Int,
    nivelRisco:    String,
    status:        PatientStatus
) {
    val animProg by animateFloatAsState(
        targetValue   = probabilidade / 100f,
        animationSpec = tween(1000),
        label         = "gauge"
    )

    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(20.dp)),
        shape    = RoundedCornerShape(20.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier            = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Probabilidade de Risco — XGBoost",
                fontSize   = 13.sp,
                color      = SignaTextGray,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(16.dp))

            Box(modifier = Modifier.size(160.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke   = 18.dp.toPx()
                    val diameter = size.minDimension - stroke
                    val topLeft  = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
                    drawArc(
                        color      = status.lightColor,
                        startAngle = 135f, sweepAngle = 270f,
                        useCenter  = false,
                        topLeft    = topLeft, size = Size(diameter, diameter),
                        style      = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                    drawArc(
                        brush      = Brush.sweepGradient(listOf(status.lightColor, status.mainColor)),
                        startAngle = 135f, sweepAngle = 270f * animProg,
                        useCenter  = false,
                        topLeft    = topLeft, size = Size(diameter, diameter),
                        style      = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "$probabilidade%",
                        fontSize   = 36.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = status.mainColor
                    )
                    Text(
                        formatarRisco(nivelRisco),
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = SignaTextGray
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScaleLegendItem("Baixo",    Color(0xFF1565C0), 0..33,   probabilidade)
                ScaleLegendItem("Moderado", Color(0xFFF9A825), 34..66,  probabilidade)
                ScaleLegendItem("Alto",     Color(0xFFE53935), 67..100, probabilidade)
            }
        }
    }
}

@Composable
private fun ScaleLegendItem(label: String, color: Color, range: IntRange, atual: Int) {
    val isActive = atual in range
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isActive) color.copy(alpha = 0.12f) else Color.Transparent
    ) {
        Column(
            modifier            = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.size(8.dp).clip(CircleShape).background(color))
            Spacer(Modifier.height(3.dp))
            Text(
                label,
                fontSize   = 11.sp,
                color      = if (isActive) color else SignaTextGray,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
            )
            Text("${range.first}–${range.last}%", fontSize = 10.sp, color = SignaTextGray)
        }
    }
}

// ─── Card de métrica individual ───────────────────────────────────────────────

@Composable
private fun MetricCard(
    label:    String,
    value:    String,
    icon:     ImageVector,
    color:    Color,
    light:    Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(14.dp)),
        shape    = RoundedCornerShape(14.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier            = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier         = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(light),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.height(6.dp))
            Text(value, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Text(label, fontSize = 10.sp, color = SignaTextGray, textAlign = TextAlign.Center, lineHeight = 13.sp)
        }
    }
}

// ─── Dados clínicos completos — lidos do PredicaoRequest real ────────────────

@Composable
private fun ClinicalDataCard(request: PredicaoRequest?) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(3.dp, RoundedCornerShape(18.dp)),
        shape    = RoundedCornerShape(18.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            SectionTitle(icon = Icons.Default.MedicalServices, title = "Dados Clínicos do Tumor")
            Spacer(Modifier.height(12.dp))

            if (request == null) {
                Text(
                    "Dados clínicos não disponíveis para esta predição.",
                    fontSize = 13.sp, color = SignaTextGray
                )
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StageChip("Estágio T", request.tStage,       SignaPurple,          Color(0xFFEDE7F6), Modifier.weight(1f))
                    StageChip("Estágio N", request.nStage,       Color(0xFF1565C0),    Color(0xFFE3F2FD), Modifier.weight(1f))
                    StageChip("Grau",      request.grade.toString(), Color(0xFF6A1B9A), Color(0xFFF3E5F5), Modifier.weight(1f))
                }
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StageChip(
                        "Estadiamento", request.sixthStage,
                        Color(0xFF00838F), Color(0xFFE0F7FA), Modifier.weight(1f)
                    )
                    StageChip(
                        "Estágio A",
                        if (request.aStage == "Regional") "Regional" else "Distante",
                        Color(0xFF2E7D32), Color(0xFFE8F5E9), Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Spacer(Modifier.height(12.dp))

                DataRow(
                    icon  = Icons.Default.Biotech,
                    label = "Diferenciação Celular",
                    value = when (request.differentiate) {
                        "Well differentiated"       -> "Bem diferenciada"
                        "Moderately differentiated" -> "Moderadamente diferenciada"
                        "Poorly differentiated"     -> "Pouco diferenciada"
                        "Undifferentiated"          -> "Indiferenciada"
                        else                        -> request.differentiate
                    }
                )
                Spacer(Modifier.height(8.dp))
                DataRow(
                    icon  = Icons.Default.Grain,
                    label = "Linfonodos Examinados / Positivos",
                    value = "${request.regionalNodeExamined} examinados  •  ${request.reginolNodePositive} positivos"
                )
                Spacer(Modifier.height(8.dp))
                DataRow(
                    icon  = Icons.Default.Straighten,
                    label = "Tamanho do Tumor",
                    value = "${request.tumorSize} mm"
                )
            }
        }
    }
}

// ─── Gráfico de barras: impacto das variáveis ─────────────────────────────────

@Composable
private fun ImpactBarChartCard(
    impactos: List<VariavelImpacto>,
    status:   PatientStatus
) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(3.dp, RoundedCornerShape(18.dp)),
        shape    = RoundedCornerShape(18.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            SectionTitle(icon = Icons.Default.BarChart, title = "Variáveis de Impacto Detectadas")
            Spacer(Modifier.height(14.dp))

            impactos.take(6).forEachIndexed { idx, vi ->
                val animWidth by animateFloatAsState(
                    targetValue   = ((impactos.size - idx).toFloat() / impactos.size),
                    animationSpec = tween(800 + idx * 100),
                    label         = "bar_$idx"
                )
                Column {
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment     = Alignment.CenterVertically
                    ) {
                        Text(
                            vi.variavel,
                            fontSize   = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color      = SignaTextDark,
                            modifier   = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("val: ${vi.valor}", fontSize = 11.sp, color = SignaTextGray)
                    }
                    Spacer(Modifier.height(4.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().height(8.dp)
                            .clip(CircleShape).background(status.lightColor)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animWidth)
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(status.mainColor.copy(alpha = 0.7f), status.mainColor)
                                    )
                                )
                        )
                    }
                    if (idx < impactos.lastIndex) Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

// ─── Status hormonal — lido do PredicaoRequest real ───────────────────────────

@Composable
private fun HormonalStatusCard(request: PredicaoRequest?, status: PatientStatus) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(3.dp, RoundedCornerShape(18.dp)),
        shape    = RoundedCornerShape(18.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            SectionTitle(icon = Icons.Default.Science, title = "Status Hormonal")
            Spacer(Modifier.height(12.dp))

            if (request == null) {
                Text("Dados hormonais não disponíveis.", fontSize = 13.sp, color = SignaTextGray)
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    HormonalStatusItem(
                        label    = "Receptor de\nEstrogênio",
                        positive = request.estrogenStatus == "Positive",
                        modifier = Modifier.weight(1f)
                    )
                    HormonalStatusItem(
                        label    = "Receptor de\nProgesterona",
                        positive = request.progesteroneStatus == "Positive",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun HormonalStatusItem(label: String, positive: Boolean, modifier: Modifier = Modifier) {
    val color = if (positive) Color(0xFF2E7D32) else Color(0xFFC62828)
    val light = if (positive) Color(0xFFE8F5E9) else Color(0xFFFCE4EC)
    val icon  = if (positive) Icons.Default.CheckCircle else Icons.Default.Cancel
    val text  = if (positive) "Positivo" else "Negativo"

    Surface(shape = RoundedCornerShape(14.dp), color = light, modifier = modifier) {
        Column(
            modifier            = Modifier.padding(14.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(28.dp))
            Spacer(Modifier.height(6.dp))
            Text(text, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, color = color)
            Spacer(Modifier.height(4.dp))
            Text(label, fontSize = 11.sp, color = SignaTextGray, textAlign = TextAlign.Center, lineHeight = 14.sp)
        }
    }
}

// ─── Estadiamento visual — lido do PredicaoRequest real ──────────────────────

@Composable
private fun StagingCard(request: PredicaoRequest?, status: PatientStatus) {
    val sixthStage = request?.sixthStage ?: "IIA"
    val tStage     = request?.tStage     ?: "—"
    val nStage     = request?.nStage     ?: "—"

    val stages     = listOf("IIA", "IIB", "IIIA", "IIIB", "IIIC")
    val currentIdx = stages.indexOf(sixthStage).takeIf { it >= 0 } ?: 0

    Card(
        modifier = Modifier.fillMaxWidth().shadow(3.dp, RoundedCornerShape(18.dp)),
        shape    = RoundedCornerShape(18.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            SectionTitle(icon = Icons.Default.Timeline, title = "Progressão — Estadiamento Clínico")
            Spacer(Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                stages.forEachIndexed { idx, stage ->
                    val isActive  = idx == currentIdx
                    val isPassed  = idx < currentIdx
                    val nodeColor = when {
                        isActive -> status.mainColor
                        isPassed -> status.mainColor.copy(alpha = 0.4f)
                        else     -> Color(0xFFE0E0E0)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = if (isActive) Modifier.weight(1.4f) else Modifier.weight(1f)
                    ) {
                        Box(
                            modifier         = Modifier
                                .size(if (isActive) 36.dp else 26.dp)
                                .clip(CircleShape)
                                .background(nodeColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                stage,
                                fontSize   = if (isActive) 10.sp else 9.sp,
                                fontWeight = FontWeight.Bold,
                                color      = Color.White
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        if (isActive) {
                            Text("Atual", fontSize = 9.sp, color = status.mainColor, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (idx < stages.lastIndex) {
                        Box(
                            modifier = Modifier
                                .weight(0.3f)
                                .height(2.dp)
                                .background(
                                    if (idx < currentIdx) status.mainColor.copy(alpha = 0.4f)
                                    else Color(0xFFE0E0E0)
                                )
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column {
                    Text("Estágio T", fontSize = 11.sp, color = SignaTextGray)
                    Text(tStage, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = SignaPurple)
                }
                Box(Modifier.width(1.dp).height(40.dp).background(Color(0xFFF0F0F0)))
                Column {
                    Text("Estágio N", fontSize = 11.sp, color = SignaTextGray)
                    Text(nStage, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1565C0))
                }
                Box(Modifier.width(1.dp).height(40.dp).background(Color(0xFFF0F0F0)))
                Column {
                    Text("Estadiamento", fontSize = 11.sp, color = SignaTextGray)
                    Text(sixthStage, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = status.mainColor)
                }
            }
        }
    }
}

// ─── Componentes auxiliares ───────────────────────────────────────────────────

@Composable
private fun SectionTitle(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = SignaPurple, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SignaTextDark)
    }
}

@Composable
private fun StageChip(label: String, value: String, color: Color, light: Color, modifier: Modifier = Modifier) {
    Surface(shape = RoundedCornerShape(10.dp), color = light, modifier = modifier) {
        Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 10.sp, color = SignaTextGray)
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = color)
        }
    }
}

@Composable
private fun DataRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, null, tint = SignaPurple, modifier = Modifier.size(16.dp).padding(top = 1.dp))
        Spacer(Modifier.width(8.dp))
        Column {
            Text(label, fontSize = 11.sp, color = SignaTextGray)
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SignaTextDark)
        }
    }
}

@Composable
private fun InfoBadge(text: String, color: Color, light: Color) {
    Surface(shape = RoundedCornerShape(6.dp), color = light) {
        Text(
            text,
            fontSize   = 11.sp,
            color      = color,
            fontWeight = FontWeight.Medium,
            modifier   = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

private fun formatDate(isoDate: String): String = try {
    val parts = isoDate.substringBefore("T").split("-")
    "${parts[2]}/${parts[1]}/${parts[0]}"
} catch (e: Exception) { isoDate }

// ─── Bottom Navigation ────────────────────────────────────────────────────────

@Composable
fun DashboardBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = false,
            onClick  = { navController.navigate("home") { launchSingleTop = true } },
            icon     = { Icon(Icons.Default.Home, null) },
            label    = { Text("Início") }
        )
        NavigationBarItem(
            selected = true,
            onClick  = {},
            icon     = { Icon(Icons.Default.Dashboard, null) },
            label    = { Text("Dashboard") }
        )
        NavigationBarItem(
            selected = false,
            onClick  = { navController.navigate("history") { launchSingleTop = true } },
            icon     = { Icon(Icons.Default.History, null) },
            label    = { Text("Histórico") }
        )
    }
}