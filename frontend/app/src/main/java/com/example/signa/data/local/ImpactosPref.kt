package com.example.signa.data.local

import android.content.Context
import com.example.signa.data.model.VariavelImpacto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Cache persistente das variaveis_impacto em SharedPreferences.
 *
 * O banco NÃO salva variaveis_impacto — elas são geradas dinamicamente
 * pelo backend só no momento do POST. Este objeto garante que fiquem
 * disponíveis mesmo após fechar e reabrir o app.
 */
object ImpactosPrefs {

    private const val PREFS_NAME = "signa_impactos_cache"
    private val gson = Gson()

    fun salvar(context: Context, predicaoId: String, variaveis: List<VariavelImpacto>) {
        if (variaveis.isEmpty()) return
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(predicaoId, gson.toJson(variaveis))
            .apply()
    }

    fun carregar(context: Context, predicaoId: String): List<VariavelImpacto> {
        val json = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(predicaoId, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<VariavelImpacto>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun limparAntigos(context: Context, idsAtivos: Set<String>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val obsoletos = prefs.all.keys.filter { it !in idsAtivos }
        if (obsoletos.isNotEmpty()) {
            prefs.edit().also { ed -> obsoletos.forEach { ed.remove(it) } }.apply()
        }
    }
}
