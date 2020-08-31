package com.example.gyrodatasakhalin.utils

import android.content.Context

class AppPreferences(context: Context) {
    val PREFERENCE_NAME = "Preferences"
    val TOKEN = "Token"
    val LOGIN = "Login"
    val PASS = "Password"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getLogin() : String? {
        return preference.getString(LOGIN, null)
    }

    fun getPass() : String? {
        return preference.getString(PASS, null)
    }

    fun getToken() : String? {
        return preference.getString(TOKEN, null)
    }

    fun setLoginPass(login: String, pass: String){
        val editor = preference.edit()
        editor.putString(LOGIN, login)
        editor.putString(PASS, pass)
        editor.apply()
    }

    fun setToken(token: String){
        val editor = preference.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }
}