package com.example.signa.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PacientesScreen(navController: NavController) {
    // Exemplo de como os dados virão do seu dataset/viewmodel futuramente
    val listaPacientes = listOf(
        PatientData("1048", "Estágio II", 57, 45, emptyList()),
        PatientData("1049", "Estágio I", 62, 20, emptyList()),
        PatientData("1050", "Estágio III", 45, 85, emptyList())
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Selecionar Paciente", color = SignaPurple, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = SignaPurple)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SignaBg)
            )
        },
        containerColor = SignaBg
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(listaPacientes) { paciente ->
                PacienteItemCard(paciente = paciente) {
                    // Aqui no futuro você passará o ID do paciente selecionado
                    navController.popBackStack()
                }
            }
        }
    }
}

@Composable
fun PacienteItemCard(paciente: PatientData, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Paciente #${paciente.id}",
                fontSize = 12.sp,
                color = SignaTextGray
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = paciente.stage,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SignaTextDark
            )
            Text(
                text = "Idade: ${paciente.age}",
                fontSize = 14.sp,
                color = SignaTextGray
            )
        }
    }
}