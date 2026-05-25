package com.example.signa.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.signa.data.model.VariavelImpacto
import com.example.signa.ui.viewmodel.ListaUiState
import com.example.signa.ui.viewmodel.PredicaoUiState
import com.example.signa.ui.viewmodel.PredicaoViewModel

// ─── Tela: Dashboard de Sintomas ─────────────────────────────────────────────

@Composable
fun SymptomsScreen(
    navController: NavController,
    vm: PredicaoViewModel = viewModel()
) {
    val predicaoState by vm.predicaoState.collectAsState()
    val listaState    by vm.listaState.collectAsState()

    // Resolve a predição mais recente com variáveis de impacto
    val ultimaPredicao = when {
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Sintomas e Indicadores", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold, color = SignaPurple)
            }
        },
        bottomBar = { SymptomsBottomNavigation(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))

            if (ultimaPredicao == null) {
                EmptyStateCard()
            } else {
                // Card resumo do risco
                RiscoResumoCard(
                    nivelRisco    = ultimaPredicao.nivelRisco,
                    probabilidade = ultimaPredicao.probabilidade.toInt(),
                    nome          = ultimaPredicao.pacienteNome,
                    status        = status
                )
                Spacer(Modifier.height(16.dp))

                val impactos = ultimaPredicao.variaveisImpacto
                if (impactos.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp).fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.CheckCircle, null,
                                tint = Color(0xFF43A047), modifier = Modifier.size(40.dp))
                            Spacer(Modifier.height(8.dp))
                            Text("Nenhum indicador de risco crítico", fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold, color = SignaTextGray)
                            Text("Os dados clínicos não apontam variáveis de alto impacto.",
                                fontSize = 12.sp, color = Color(0xFFBDBDBD),
                                modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                } else {
                    // Título seção
                    Text("Variáveis de Impacto Identificadas",
                        fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                        color = SignaTextGray, modifier = Modifier.padding(bottom = 8.dp))

                    impactos.forEach { vi ->
                        SymptomCard(vi = vi, status = status)
                        Spacer(Modifier.height(10.dp))
                    }

                    // Todos os sintomas agrupados
                    Spacer(Modifier.height(4.dp))
                    Text("Todos os sintomas relacionados",
                        fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                        color = SignaTextGray, modifier = Modifier.padding(bottom = 8.dp))
                    AllSymptomsCard(impactos = impactos, status = status)
                }

                Spacer(Modifier.height(12.dp))
                // Aviso legal
                Text(
                    "As hipóteses apresentadas são baseadas em literatura científica e têm caráter " +
                    "informativo. Não substituem avaliação médica especializada.",
                    fontSize = 10.sp, color = Color(0xFFBDBDBD), lineHeight = 14.sp
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RiscoResumoCard(
    nivelRisco: String, probabilidade: Int, nome: String, status: PatientStatus
) {
    val animatedProgress by animateFloatAsState(
        targetValue = probabilidade / 100f,
        animationSpec = tween(durationMillis = 800),
        label = "progress"
    )

    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(44.dp).clip(CircleShape)
                        .background(status.lightColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = status.mainColor,
                        modifier = Modifier.size(24.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(nome, fontSize = 13.sp, color = SignaTextGray)
                    Text(formatarRisco(nivelRisco), fontSize = 16.sp,
                        fontWeight = FontWeight.Bold, color = status.mainColor)
                }
                Spacer(Modifier.weight(1f))
                Text("$probabilidade%", fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold, color = status.mainColor)
            }
            Spacer(Modifier.height(14.dp))
            Text("Probabilidade de risco calculada pelo modelo XGBoost",
                fontSize = 11.sp, color = SignaTextGray)
            Spacer(Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                color = status.mainColor, trackColor = status.lightColor
            )
        }
    }
}

@Composable
private fun SymptomCard(vi: VariavelImpacto, status: PatientStatus) {
    Card(
        modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp))
                        .background(status.lightColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Warning, null, tint = status.mainColor,
                        modifier = Modifier.size(20.dp))
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(vi.variavel, fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold, color = SignaTextDark)
                    Text("Valor registrado: ${vi.valor}", fontSize = 12.sp, color = SignaTextGray)
                }
            }
            Spacer(Modifier.height(10.dp))
            Text(vi.hipotese, fontSize = 12.sp, color = SignaTextGray,
                lineHeight = 17.sp)

            if (vi.sintomas.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFF0F0F0))
                Spacer(Modifier.height(8.dp))
                Text("Sintomas associados", fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold, color = SignaTextGray)
                Spacer(Modifier.height(6.dp))
                // chips de sintoma
                FlowRow(vi.sintomas, status)
            }

            Spacer(Modifier.height(8.dp))
            Text("Fonte: ${vi.fonte}", fontSize = 10.sp, color = Color(0xFFBDBDBD),
                lineHeight = 13.sp)
        }
    }
}

@Composable
private fun FlowRow(sintomas: List<String>, status: PatientStatus) {
    // Simula wrap com rows de até 2 itens por linha
    val rows = sintomas.chunked(2)
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        rows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                row.forEach { sintoma ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = status.lightColor
                    ) {
                        Text(
                            text = sintoma,
                            fontSize = 11.sp,
                            color = status.mainColor,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AllSymptomsCard(impactos: List<VariavelImpacto>, status: PatientStatus) {
    val todos = impactos.flatMap { it.sintomas }.distinct()
    Card(
        modifier = Modifier.fillMaxWidth().shadow(2.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            todos.forEachIndexed { idx, sintoma ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(6.dp).clip(CircleShape)
                            .background(status.mainColor)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(sintoma, fontSize = 13.sp, color = SignaTextDark)
                }
                if (idx < todos.lastIndex) Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun SymptomsBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = false,
            onClick  = { navController.navigate("home") { launchSingleTop = true } },
            icon     = { Icon(Icons.Default.Dashboard, null) },
            label    = { Text("Dashboard") }
        )
        NavigationBarItem(
            selected = true,
            onClick  = {},
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
