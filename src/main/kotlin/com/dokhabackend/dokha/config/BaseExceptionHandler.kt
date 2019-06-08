package com.dokhabackend.dokha.config

import com.dokhabackend.dokha.core.RestResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import javax.security.auth.message.AuthException
import javax.servlet.http.HttpServletResponse

/**
 * @author v.butuzov
 * Контроллер для перехвата исключений
 */
@ControllerAdvice
class BaseExceptionHandler {

    /**
     * Перехватчик исключений
     *
     * @param exception исключение
     * @param response  ответ
     */
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception?, response: HttpServletResponse?) {
        if (exception != null && response != null) {
            prepareResponse(exception, response)

            val jsonResponse = toJSON(RestResponse<Any>(exception.message))

            writeResponse(response, jsonResponse)
        }

    }

    private fun toJSON(restResponse: RestResponse<Any>): String? {
        val jsonMapper = ObjectMapper()

        try {
            return jsonMapper.writeValueAsString(restResponse)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
            return null
        }

    }

    private fun prepareResponse(exception: Exception, response: HttpServletResponse) {
        val responseCode = getResponseCode(exception)
        response.characterEncoding = "UTF-8"
        response.contentType = "application/json;charset=UTF-8"
        response.status = responseCode
    }

    private fun getResponseCode(exception: Exception): Int {
        var responseCode = 0
        if (exception is IllegalArgumentException) {
            responseCode = 400
        } else if (exception is AuthException) {
            responseCode = 401
        } else if (exception is HttpMessageNotReadableException) {
            responseCode = 400
        }

        if (responseCode <= 0) {
            responseCode = 500
        }

        return responseCode
    }

    private fun writeResponse(response: HttpServletResponse, responseJson: String?) {
        try {
            response.writer.write(responseJson!!)
        } catch (e: Exception) {
        }

    }
}
