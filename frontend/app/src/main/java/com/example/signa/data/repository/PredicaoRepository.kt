package com.example.signa.data.repository

import com.example.signa.data.api.RetrofitClient
import com.example.signa.data.model.PredicaoRequest
import com.example.signa.data.model.PredicaoResponse

/**
 * Repositório responsável por toda comunicação com a API FastAPI.
 * Retorna [Result] para facilitar o tratamento de erros no ViewModel.
 */
class PredicaoRepository {

    private val api = RetrofitClient.predicaoService

    /** Envia os dados clínicos e recebe a predição do ML */
    suspend fun criarPredicao(request: PredicaoRequest): Result<PredicaoResponse> {
        return try {
            val response = api.criarPredicao(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Erro ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Lista todas as predições já salvas */
    suspend fun listarPredicoes(): Result<List<PredicaoResponse>> {
        return try {
            val response = api.listarPredicoes()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Erro ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Busca uma predição específica pelo UUID */
    suspend fun buscarPredicao(id: String): Result<PredicaoResponse> {
        return try {
            val response = api.buscarPredicao(id)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Predição não encontrada"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
