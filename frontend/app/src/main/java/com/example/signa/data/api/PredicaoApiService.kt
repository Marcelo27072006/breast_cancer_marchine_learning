package com.example.signa.data.api

import com.example.signa.data.model.PredicaoRequest
import com.example.signa.data.model.PredicaoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PredicaoApiService {

    /** POST /predicao/ → cria uma nova predição */
    @POST("predicao/")
    suspend fun criarPredicao(
        @Body body: PredicaoRequest
    ): Response<PredicaoResponse>

    /** GET /predicao/ → lista todas as predições */
    @GET("predicao/")
    suspend fun listarPredicoes(): Response<List<PredicaoResponse>>

    /** GET /predicao/{id} → busca uma predição pelo UUID */
    @GET("predicao/{id}")
    suspend fun buscarPredicao(
        @Path("id") id: String
    ): Response<PredicaoResponse>
}
