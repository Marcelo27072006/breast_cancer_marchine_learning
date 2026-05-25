package com.example.signa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.signa.ui.viewmodel.PredicaoUiState
import com.example.signa.ui.viewmodel.PredicaoViewModel

// ─── Tela: Formulário Clínico + Resultado da Predição ────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacientesScreen(
    navController: NavController,
    vm: PredicaoViewModel = viewModel()
) {
    val predicaoState by vm.predicaoState.collectAsState()

    LaunchedEffect(predicaoState) {
        if (predicaoState is PredicaoUiState.Success) {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Dados Clínicos do Paciente",
                        color = SignaPurple,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Voltar", tint = SignaPurple)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SignaBg)
            )
        },
        containerColor = SignaBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // ── Seção: Identificação ─────────────────────────────────────────
            SectionHeader("Identificação")
            OutlinedTextField(
                value = vm.pacienteNome.collectAsState().value,
                onValueChange = { vm.pacienteNome.value = it },
                label = { Text("Nome do Paciente", fontSize = 15.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(fontSize = 15.sp, color = SignaTextDark)
            )

            // ── Seção: Dados Pessoais ────────────────────────────────────────
            SectionHeader("Dados Pessoais")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberField("Idade", vm.age.collectAsState().value, Modifier.weight(1f)) {
                    vm.age.value = it
                }
                DropdownField(
                    label = "Raça",
                    options = listOf("White", "Black", "Other"),
                    displayOptions = listOf("Branca", "Negra", "Outra"),
                    selected = vm.race.collectAsState().value,
                    modifier = Modifier.weight(1f)
                ) { vm.race.value = it }
            }
            DropdownField(
                label = "Estado Civil",
                options = listOf("Married", "Single", "Divorced", "Widowed", "Separated"),
                displayOptions = listOf("Casado(a)", "Solteiro(a)", "Divorciado(a)", "Viúvo(a)", "Separado(a)"),
                selected = vm.maritalStatus.collectAsState().value
            ) { vm.maritalStatus.value = it }

            // ── Seção: Dados do Tumor ─────────────────────────────────────────
            SectionHeader("Dados do Tumor")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberField("Tamanho (mm)", vm.tumorSize.collectAsState().value, Modifier.weight(1f)) {
                    vm.tumorSize.value = it
                }
                DropdownField(
                    label = "Estágio T",
                    options = listOf("T1", "T2", "T3", "T4"),
                    displayOptions = listOf("T1 — Pequeno", "T2 — Moderado", "T3 — Grande", "T4 — Extenso"),
                    selected = vm.tStage.collectAsState().value,
                    modifier = Modifier.weight(1f)
                ) { vm.tStage.value = it }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DropdownField(
                    label = "Grau do Tumor",
                    options = listOf("1", "2", "3", "4"),
                    displayOptions = listOf("Grau 1 — Baixo", "Grau 2 — Moderado", "Grau 3 — Alto", "Grau 4 — Anaplásico"),
                    selected = vm.grade.collectAsState().value,
                    modifier = Modifier.weight(1f)
                ) { vm.grade.value = it }
                DropdownField(
                    label = "Estágio A",
                    options = listOf("Regional", "Distant"),
                    displayOptions = listOf("Regional", "Distante"),
                    selected = vm.aStage.collectAsState().value,
                    modifier = Modifier.weight(1f)
                ) { vm.aStage.value = it }
            }
            DropdownField(
                label = "Diferenciação Celular",
                options = listOf(
                    "Well differentiated",
                    "Moderately differentiated",
                    "Poorly differentiated",
                    "Undifferentiated"
                ),
                displayOptions = listOf(
                    "Bem diferenciada",
                    "Moderadamente diferenciada",
                    "Pouco diferenciada",
                    "Indiferenciada"
                ),
                selected = vm.differentiate.collectAsState().value
            ) { vm.differentiate.value = it }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DropdownField(
                    label = "Estadiamento",
                    options = listOf("IIA", "IIB", "IIIA", "IIIB", "IIIC"),
                    displayOptions = listOf("IIA", "IIB", "IIIA", "IIIB", "IIIC"),
                    selected = vm.sixthStage.collectAsState().value,
                    modifier = Modifier.weight(1f)
                ) { vm.sixthStage.value = it }
                DropdownField(
                    label = "Estágio N",
                    options = listOf("N1", "N2", "N3"),
                    displayOptions = listOf("N1 — Poucos", "N2 — Moderado", "N3 — Muitos"),
                    selected = vm.nStage.collectAsState().value,
                    modifier = Modifier.weight(1f)
                ) { vm.nStage.value = it }
            }

            // ── Seção: Linfonodos ────────────────────────────────────────────
            SectionHeader("Linfonodos")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                NumberField("Examinados", vm.regionalNode.collectAsState().value, Modifier.weight(1f)) {
                    vm.regionalNode.value = it
                }
                NumberField("Positivos", vm.reginolPositive.collectAsState().value, Modifier.weight(1f)) {
                    vm.reginolPositive.value = it
                }
            }

            // ── Seção: Status Hormonal ────────────────────────────────────────
            SectionHeader("Status Hormonal")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                DropdownField(
                    label = "Estrogênio",
                    options = listOf("Positive", "Negative"),
                    displayOptions = listOf("Positivo", "Negativo"),
                    selected = vm.estrogenStatus.collectAsState().value,
                    modifier = Modifier.weight(1f)
                ) { vm.estrogenStatus.value = it }
                DropdownField(
                    label = "Progesterona",
                    options = listOf("Positive", "Negative"),
                    displayOptions = listOf("Positivo", "Negativo"),
                    selected = vm.progesteroneStatus.collectAsState().value,
                    modifier = Modifier.weight(1f)
                ) { vm.progesteroneStatus.value = it }
            }

            Spacer(Modifier.height(8.dp))

            // ── Feedback de erro ─────────────────────────────────────────────
            if (predicaoState is PredicaoUiState.Error) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4EC)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "⚠ ${(predicaoState as PredicaoUiState.Error).message}",
                        color = Color(0xFFC62828),
                        modifier = Modifier.padding(12.dp),
                        fontSize = 14.sp
                    )
                }
            }

            // ── Botão Enviar ─────────────────────────────────────────────────
            Button(
                onClick = { vm.enviarPredicao() },
                enabled = predicaoState !is PredicaoUiState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SignaPurple)
            ) {
                if (predicaoState is PredicaoUiState.Loading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        "Executar Predição",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ─── Componentes auxiliares ───────────────────────────────────────────────────

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title.uppercase(),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = SignaPurple,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
    )
}

@Composable
private fun NumberField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (it.all(Char::isDigit)) onValueChange(it) },
        label = { Text(label, fontSize = 14.sp) },
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = LocalTextStyle.current.copy(fontSize = 15.sp, color = SignaTextDark)
    )
}

@Composable
private fun DropdownField(
    label: String,
    options: List<String>,
    displayOptions: List<String> = options,
    selected: String,
    modifier: Modifier = Modifier,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Mostra o display label do valor selecionado
    val displaySelected = options.indexOf(selected).let { idx ->
        if (idx >= 0 && idx < displayOptions.size) displayOptions[idx] else selected
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = displaySelected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label, fontSize = 14.sp) },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, null, tint = SignaPurple)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(fontSize = 15.sp, color = SignaTextDark)
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEachIndexed { idx, option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            displayOptions.getOrElse(idx) { option },
                            fontSize = 14.sp,
                            color = SignaTextDark
                        )
                    },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}