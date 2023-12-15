package com.example.uas_ppapb_v2.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.uas_ppapb_v2.database.model.Account

class SessionManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }
    companion object {

        private const val IS_LOGGED_IN = "is_logged_in"
        private const val IS_ADMIN = "is_admin"
        private const val UID = "uid"
        private const val USERNAME = "username"
        private const val EMAIL = "email"
        private const val DOB = "date_of_birth"
        @Volatile
        private var INSTANCE: SessionManager? = null
        fun getInstance(context: Context): SessionManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SessionManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    fun setAccount(uid: String, username: String, email: String, dob: String) {
        editor.putString(UID, uid)
        editor.putString(USERNAME, username)
        editor.putString(EMAIL, email)
        editor.putString(DOB, dob)
        editor.apply()
    }

    fun getAccount(): Account {
        val uid = sharedPreferences.getString(UID, "")
        val username = sharedPreferences.getString(USERNAME, "")
        val email = sharedPreferences.getString(EMAIL, "")
        val dob = sharedPreferences.getString(DOB, "")
        return Account(uid!!, username!!, email!!, dob!!)
    }

    fun getUID(): String {
        return sharedPreferences.getString(UID, "")!!
    }
    fun setLoggedIn(isLoggedIn: Boolean, isAdmin: Boolean) {
        editor.putBoolean(IS_LOGGED_IN, isLoggedIn)
        editor.putBoolean(IS_ADMIN, isAdmin)
        editor.apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun isAdmin(): Boolean {
        return sharedPreferences.getBoolean(IS_ADMIN, false)
    }
    fun clear() {
        editor.clear()
        editor.apply()
    }

}