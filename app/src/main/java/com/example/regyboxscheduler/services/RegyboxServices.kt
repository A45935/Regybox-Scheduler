package com.example.regyboxscheduler.services

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

private const val HOST = "https://www.regibox.pt/app/app_nova/php"

class RegyboxServices (
    private val httpClient: OkHttpClient,
) {

    fun login (idBox: String, username: String, password: String) {
        val formBody = FormBody.Builder()
            .add("id_box", idBox)
            .add("login", username)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url("$HOST/login/scripts/verifica_acesso.php?lang=pt")
            .post(formBody)
            .build()

        httpClient.newCall(request).execute().use { response ->
            println(response.body.string())
        }
    }

    fun getAulas (timestamp: Long) {
        val request = Request.Builder()
            .url("$HOST/aulas/aulas.php?valor1=$timestamp")
            .build()

        httpClient.newCall(request).execute().use { response ->
            println(response.body.string())
        }
    }

    fun marcarAula (idAula: String, data: String, ano: String, idRato: String, x: String) {
        val request = Request.Builder()
            .url("$HOST/aulas/marca_aulas.php?id_aula=$idAula&data=$data&source=mes&ano=$ano&id_rato=$idRato&x=$x")
            .build()

        httpClient.newCall(request).execute().use { response ->
            println(response.body.string())
        }
    }

    fun cancelarAula (idAula: String, data: String, ano: String, x: String) {
        val request = Request.Builder()
            .url("$HOST/aulas/cancela_aula.php?id_aula=$idAula&data=$data&source=mes&ano=$ano&x=$x")
            .build()

        httpClient.newCall(request).execute().use { response ->
            println(response.body.string())
        }
    }
}