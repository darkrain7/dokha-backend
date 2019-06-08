package com.dokhabackend.dokha.core

/**
 * Created by GavrilinKU on 23.08.16.
 * DTO universal response
 */

class RestResponse<T> {

    var data: T? = null
    var error: String?

    val isValid: Boolean get() = data != null && error == null

    val isNotValid: Boolean get() = data == null || error != null

    constructor(data: T) : super() {
        this.data = data
        this.error = null
    }

    constructor(error: String?) : super() {
        this.data = null
        this.error = error
    }

}
