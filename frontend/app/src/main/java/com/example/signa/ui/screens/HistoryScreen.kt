package com.example.signa.ui.screens

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.signa.data.model.PredicaoResponse
import com.example.signa.ui.viewmodel.ListaUiState
import com.example.signa.ui.viewmodel.PredicaoViewModel
import java.io.OutputStream

// ─── Tela: Histórico + Exportar CSV ──────────────────────────────────────────

@Composable
fun HistoryScreen(
    navController: NavController,
    vm: PredicaoViewModel = viewModel()
) {
    val listaState by vm.listaState.collectAsState()
    val context    = LocalContext.current
    var exportando by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { vm.carregarHistorico() }

    val predicoes = when (listaState) {
        is ListaUiState.Success -> (listaState as ListaUiState.Success).data
        else                    -> emptyList()
    }

    // Injeta impactos do cache em cada item da lista
    val predicoesComImpacto = predicoes.map { p ->
        p.copy(variaveisImpacto = p.variaveisImpacto.ifEmpty { vm.getImpactosParaId(p.id) })
    }

    if (showSuccess) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(3000)
            showSuccess = false
        }
    }

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
                Text("Histórico", fontSize = 20.sp,
                    fontWeight = FontWeight.Bold, color = SignaPurple)

                if (predicoesComImpacto.isNotEmpty()) {
                    Button(
                        onClick = {
                            exportando = true
                            exportarCSV(context, predicoesComImpacto) { sucesso ->
                                exportando  = false
                                showSuccess = sucesso
                                Toast.makeText(
                                    context,
                                    if (sucesso) "CSV exportado para Downloads!" else "Erro ao exportar.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        enabled = !exportando,
                        shape  = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SignaPurple),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        if (exportando) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(Icons.Default.Download, null,
                                modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(Modifier.width(6.dp))
                            Text("Exportar CSV", fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        },
        bottomBar = { HistoryBottomNavigation(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(8.dp))

            // Banner de sucesso
            if (showSuccess) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape  = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null,
                            tint = Color(0xFF43A047), modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("CSV salvo em Downloads/signa_historico.csv",
                            fontSize = 13.sp, color = Color(0xFF2E7D32))
                    }
                }
            }

            when (listaState) {
                is ListaUiState.Loading -> {
                    Box(Modifier.fillMaxWidth().padding(40.dp),
                        contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = SignaPurple)
                    }
                }
                is ListaUiState.Error -> {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape  = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC))
                    ) {
                        Text(
                            "Erro ao carregar histórico.",
                            color = Color(0xFFC62828),
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {
                    if (predicoesComImpacto.isEmpty()) {
                        HistoryEmptyState()
                    } else {
                        Text("${predicoesComImpacto.size} predição(ões) registrada(s)",
                            fontSize = 12.sp, color = SignaTextGray,
                            modifier = Modifier.padding(bottom = 12.dp))

                        predicoesComImpacto.forEach { p ->
                            HistoryItemCard(predicao = p)
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun HistoryItemCard(predicao: PredicaoResponse) {
    val status = resolveStatusFromRisco(predicao.nivelRisco)
    Card(
        modifier = Modifier.fillMaxWidth().shadow(3.dp, RoundedCornerShape(16.dp)),
        shape  = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(38.dp).clip(CircleShape)
                            .background(status.lightColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, null, tint = status.mainColor,
                            modifier = Modifier.size(22.dp))
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(predicao.pacienteNome, fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold, color = SignaTextDark)
                        Text(
                            predicao.criadoEm.take(10).replace("-", "/"),
                            fontSize = 11.sp, color = SignaTextGray
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = status.lightColor
                ) {
                    Text(
                        formatarRisco(predicao.nivelRisco),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = status.mainColor,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF5F5F5))
            Spacer(Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HistoryStat("Probabilidade", "${predicao.probabilidade.toInt()}%", status.mainColor)
                HistoryStat("Variáveis", "${predicao.variaveisImpacto.size} fatores", SignaTextGray)
                HistoryStat("ID", predicao.id.take(8) + "…", SignaTextGray)
            }

            if (predicao.variaveisImpacto.isNotEmpty()) {
                Spacer(Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFFF5F5F5))
                Spacer(Modifier.height(8.dp))
                Text("Variáveis de impacto:", fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold, color = SignaTextGray)
                Spacer(Modifier.height(4.dp))
                predicao.variaveisImpacto.forEach { vi ->
                    Row(verticalAlignment = Alignment.Top,
                        modifier = Modifier.padding(vertical = 2.dp)) {
                        Box(Modifier.size(5.dp).clip(CircleShape)
                            .background(status.mainColor)
                            .padding(top = 5.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("${vi.variavel} — ${vi.valor}",
                            fontSize = 12.sp, color = SignaTextDark)
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = color)
        Text(label, fontSize = 10.sp, color = SignaTextGray)
    }
}

@Composable
private fun HistoryEmptyState() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.History, null,
                tint = SignaPurple.copy(alpha = 0.3f), modifier = Modifier.size(56.dp))
            Spacer(Modifier.height(12.dp))
            Text("Nenhum histórico encontrado", fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold, color = SignaTextGray,
                textAlign = TextAlign.Center)
            Text("As predições realizadas aparecerão aqui.",
                fontSize = 12.sp, color = Color(0xFFBDBDBD),
                textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

// ─── Exportar CSV ─────────────────────────────────────────────────────────────

private fun exportarCSV(
    context: Context,
    predicoes: List<PredicaoResponse>,
    onResult: (Boolean) -> Unit
) {
    try {
        val csvHeader = "ID,Paciente,Data,Predicao,Probabilidade (%),Nivel Risco,Variaveis Impacto\n"
        val csvBody   = predicoes.joinToString("\n") { p ->
            val variaveis = p.variaveisImpacto.joinToString(" | ") { it.variavel }
            listOf(
                p.id,
                "\"${p.pacienteNome.replace("\"", "'")}\"",
                p.criadoEm.take(10),
                p.predicao,
                p.probabilidade.toInt().toString(),
                p.nivelRisco,
                "\"${variaveis}\""
            ).joinToString(",")
        }
        val csvContent = csvHeader + csvBody

        val fileName = "signa_historico.csv"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "text/csv")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = context.contentResolver
                .insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
            uri?.let { u ->
                context.contentResolver.openOutputStream(u)?.use { os: OutputStream ->
                    os.write(csvContent.toByteArray(Charsets.UTF_8))
                }
            }
        } else {
            @Suppress("DEPRECATION")
            val dir  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = java.io.File(dir, fileName)
            file.writeText(csvContent, Charsets.UTF_8)
        }
        onResult(true)
    } catch (e: Exception) {
        onResult(false)
    }
}

@Composable
fun HistoryBottomNavigation(navController: NavController) {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = false,
            onClick  = { navController.navigate("home") { launchSingleTop = true } },
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
            selected = true,
            onClick  = {},
            icon     = { Icon(Icons.Default.History, null) },
            label    = { Text("History") }
        )
    }
}
