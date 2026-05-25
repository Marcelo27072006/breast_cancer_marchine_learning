package com.example.signa.data.model

import com.google.gson.annotations.SerializedName

// ─── Request ──────────────────────────────────────────────────────────────────

data class PredicaoRequest(
    @SerializedName("paciente_nome")          val pacienteNome: String,
    @SerializedName("age")                    val age: Int,
    @SerializedName("race")                   val race: String,
    @SerializedName("marital_status")         val maritalStatus: String,
    @SerializedName("tumor_size")             val tumorSize: Int,
    @SerializedName("t_stage")                val tStage: String,
    @SerializedName("grade")                  val grade: Int,
    @SerializedName("differentiate")          val differentiate: String,
    @SerializedName("a_stage")                val aStage: String,
    @SerializedName("sixth_stage")            val sixthStage: String,
    @SerializedName("n_stage")                val nStage: String,
    @SerializedName("regional_node_examined") val regionalNodeExamined: Int,
    @SerializedName("reginol_node_positive")  val reginolNodePositive: Int,
    @SerializedName("estrogen_status")        val estrogenStatus: String,
    @SerializedName("progesterone_status")    val progesteroneStatus: String
)

// ─── Response ─────────────────────────────────────────────────────────────────

data class VariavelImpacto(
    @SerializedName("variavel") val variavel: String,
    @SerializedName("valor")    val valor: String,
    @SerializedName("hipotese") val hipotese: String,
    @SerializedName("sintomas") val sintomas: List<String>,
    @SerializedName("fonte")    val fonte: String
)

data class PredicaoResponse(
    @SerializedName("id")               val id: String,
    @SerializedName("paciente_nome")    val pacienteNome: String,
    @SerializedName("predicao")         val predicao: String,        // "Alive" | "Dead"
    @SerializedName("probabilidade")    val probabilidade: Double,   // 0–100
    @SerializedName("nivel_risco")      val nivelRisco: String,      // "baixo" | "moderado" | "alto"
    @SerializedName("criado_em")        val criadoEm: String,
    @SerializedName("aviso")            val aviso: String,
    @SerializedName("variaveis_impacto") val variaveisImpacto: List<VariavelImpacto>
)
