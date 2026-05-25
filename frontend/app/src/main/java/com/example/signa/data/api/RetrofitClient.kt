package com.example.signa.data.api

import com.example.signa.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    /**
     * BASE_URL vem de BuildConfig, que é preenchido no build.gradle.kts
     * lendo o arquivo .env na raiz do projeto.
     *
     * Valor padrão de desenvolvimento: http://10.0.2.2:8000
     *   → 10.0.2.2 é o alias que o emulador Android usa para "localhost do PC".
     *   → Troque por o IP real da máquina se usar dispositivo físico.
     */
    private val baseUrl: String
        get() = BuildConfig.BASE_URL.trimEnd('/') + "/"

    /** Interceptor que injeta o header X-API-Key em toda requisição */
    private val apiKeyInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("X-API-Key", BuildConfig.API_KEY)
            .build()
        chain.proceed(request)
    }

    /** Log detalhado apenas em builds de debug */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG)
            HttpLoggingInterceptor.Level.BODY
        else
            HttpLoggingInterceptor.Level.NONE
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        android.util.Log.d("API_URL", baseUrl)
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val predicaoService: PredicaoApiService by lazy {
        retrofit.create(PredicaoApiService::class.java)
    }
}
