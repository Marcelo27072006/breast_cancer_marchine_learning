package com.example.signa.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.signa.data.local.ImpactosPrefs
import com.example.signa.data.model.PredicaoRequest
import com.example.signa.data.model.PredicaoResponse
import com.example.signa.data.model.VariavelImpacto
import com.example.signa.data.repository.PredicaoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PredicaoUiState {
    object Idle    : PredicaoUiState()
    object Loading : PredicaoUiState()
    data class Success(val data: PredicaoResponse) : PredicaoUiState()
    data class Error(val message: String)          : PredicaoUiState()
}

sealed class ListaUiState {
    object Idle    : ListaUiState()
    object Loading : ListaUiState()
    data class Success(val data: List<PredicaoResponse>) : ListaUiState()
    data class Error(val message: String)                : ListaUiState()
}

/**
 * Mudanças em relação ao original:
 *  - Herda AndroidViewModel para ter acesso ao Context sem vazar Activity.
 *  - enviarPredicao() grava variaveis_impacto em SharedPreferences (ImpactosPrefs)
 *    no momento do POST, quando o backend as devolve cheias.
 *  - carregarHistorico() limpa entradas obsoletas do cache.
 *  - getImpactosParaId() busca: memória → SharedPreferences → lista vazia.
 *  - Nenhuma alteração no backend é necessária.
 */
class PredicaoViewModel(application: Application) : AndroidViewModel(application) {

    private val ctx        = application.applicationContext
    private val repository = PredicaoRepository()

    private val _predicaoState = MutableStateFlow<PredicaoUiState>(PredicaoUiState.Idle)
    val predicaoState: StateFlow<PredicaoUiState> = _predicaoState

    private val _listaState = MutableStateFlow<ListaUiState>(ListaUiState.Idle)
    val listaState: StateFlow<ListaUiState> = _listaState

    // Cache em memória — evita leituras repetidas de SharedPreferences
    private val impactosCache = mutableMapOf<String, List<VariavelImpacto>>()

    // ── Campos do formulário ──────────────────────────────────────────────────
    val pacienteNome       = MutableStateFlow("")
    val age                = MutableStateFlow("52")
    val race               = MutableStateFlow("White")
    val maritalStatus      = MutableStateFlow("Married")
    val tumorSize          = MutableStateFlow("30")
    val tStage             = MutableStateFlow("T2")
    val grade              = MutableStateFlow("2")
    val differentiate      = MutableStateFlow("Moderately differentiated")
    val aStage             = MutableStateFlow("Regional")
    val sixthStage         = MutableStateFlow("IIA")
    val nStage             = MutableStateFlow("N1")
    val regionalNode       = MutableStateFlow("14")
    val reginolPositive    = MutableStateFlow("2")
    val estrogenStatus     = MutableStateFlow("Positive")
    val progesteroneStatus = MutableStateFlow("Positive")

    fun enviarPredicao() {
        _predicaoState.value = PredicaoUiState.Loading
        viewModelScope.launch {
            val request = buildRequest() ?: run {
                _predicaoState.value = PredicaoUiState.Error("Preencha todos os campos corretamente.")
                return@launch
            }
            val result = repository.criarPredicao(request)
            _predicaoState.value = result.fold(
                onSuccess = { response ->
                    // Persiste localmente para sobreviver ao fechamento do app
                    if (response.variaveisImpacto.isNotEmpty()) {
                        ImpactosPrefs.salvar(ctx, response.id, response.variaveisImpacto)
                        impactosCache[response.id] = response.variaveisImpacto
                    }
                    PredicaoUiState.Success(response)
                },
                onFailure = { PredicaoUiState.Error(it.message ?: "Erro desconhecido") }
            )
        }
    }

    fun carregarHistorico() {
        _listaState.value = ListaUiState.Loading
        viewModelScope.launch {
            val result = repository.listarPredicoes()
            _listaState.value = result.fold(
                onSuccess = { lista ->
                    val idsAtivos = lista.map { it.id }.toSet()
                    ImpactosPrefs.limparAntigos(ctx, idsAtivos)
                    ListaUiState.Success(lista)
                },
                onFailure = { ListaUiState.Error(it.message ?: "Erro ao carregar histórico") }
            )
        }
    }

    /** Retorna impactos: memória → SharedPreferences → vazio */
    fun getImpactosParaId(id: String): List<VariavelImpacto> =
        impactosCache.getOrPut(id) { ImpactosPrefs.carregar(ctx, id) }

    fun resetarEstado() {
        _predicaoState.value = PredicaoUiState.Idle
    }

    private fun buildRequest(): PredicaoRequest? = try {
        PredicaoRequest(
            pacienteNome         = pacienteNome.value.trim().ifEmpty { return null },
            age                  = age.value.toInt(),
            race                 = race.value,
            maritalStatus        = maritalStatus.value,
            tumorSize            = tumorSize.value.toInt(),
            tStage               = tStage.value,
            grade                = grade.value.toInt(),
            differentiate        = differentiate.value,
            aStage               = aStage.value,
            sixthStage           = sixthStage.value,
            nStage               = nStage.value,
            regionalNodeExamined = regionalNode.value.toInt(),
            reginolNodePositive  = reginolPositive.value.toInt(),
            estrogenStatus       = estrogenStatus.value,
            progesteroneStatus   = progesteroneStatus.value
        )
    } catch (e: NumberFormatException) { null }
}
