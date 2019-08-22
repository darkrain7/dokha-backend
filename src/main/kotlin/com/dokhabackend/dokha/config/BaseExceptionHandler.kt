package com.dokhabackend.dokha.config

import com.dokhabackend.dokha.core.ErrorMessage
import com.dokhabackend.dokha.core.RestResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import javax.security.auth.message.AuthException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author v.butuzov
 * Контроллер для перехвата исключений
 */

private val logger = KotlinLogging.logger {}

@ControllerAdvice
class BaseExceptionHandler : AuthenticationEntryPoint {

    /**
     * Auth Exception handling
     */
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {

        logger.error { "$authException" }

        authException.stackTrace.forEach { logger.error { it } }

        prepareResponse(authException, response)

        val jsonResponse = toJSON(RestResponse(ErrorMessage(errorText = "Пользователь не авторизован")))

        writeResponse(response, jsonResponse)
    }

    /**
     * Перехватчик исключений
     *
     * @param exception исключение
     * @param response  ответ
     */
    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception, response: HttpServletResponse) {

        logger.error { exception }

        prepareResponse(exception, response)

        val jsonResponse = toJSON(RestResponse(ErrorMessage(errorText = getErrorMessage(exception).toString())))

        writeResponse(response, jsonResponse)
    }

    private fun getErrorMessage(exception: Exception): String? {

        return if (exception.cause?.message == null)
            exception.message
        else
            exception.cause!!.message
    }

    private fun toJSON(restResponse: RestResponse<Any>): String? {
        val jsonMapper = ObjectMapper()

        return try {
            jsonMapper.writeValueAsString(restResponse)
        } catch (e: JsonProcessingException) {
            e.printStackTrace()
            null
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

