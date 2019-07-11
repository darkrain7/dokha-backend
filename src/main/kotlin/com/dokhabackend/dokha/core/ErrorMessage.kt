package com.dokhabackend.dokha.core

import com.google.gson.Gson

/**
 * Created by SemenovAE on 11.07.2019
 */

data class ErrorMessage(
        val errorId: Int = 500,
        val errorText: String) {

    override fun toString(): String {
        return Gson().toJson(this)
    }
}
