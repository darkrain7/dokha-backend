package com.dokhabackend.dokha.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author v.butuzov
 * Контроллер для перехвата исключений
 */
@Slf4j
@ControllerAdvice
public class BaseExceptionHandler {

    /**
     * Перехватчик исключений
     *
     * @param exception исключение
     * @param request   запрос
     * @param response  ответ
     */
    @ExceptionHandler(Exception.class)
    public void handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) {
        log.error("Handling exception", exception);
        if (exception != null && response != null) {
            prepareResponse(exception, response);

            String str = toJSON(exception.getMessage());
            writeResponse(response, str);
        }

    }

    private static String toJSON(Object object) {
        if (object == null) {
            return null;
        } else {
            ObjectMapper jsonMapper = new ObjectMapper();

            try {
                return jsonMapper.writeValueAsString(object);
            } catch (IOException e) {
                log.error("Cannot serialize object [" + object + "] to json object. Reason[" + e.getClass() + "]: " + e.getMessage());
                return null;
            }
        }
    }

    private static void prepareResponse(Exception exception, HttpServletResponse response) {
        int responseCode = getResponseCode(exception);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(responseCode);
    }

    private static int getResponseCode(Exception exception) {
        int responseCode = 0;
        if (exception instanceof IllegalArgumentException) {
            responseCode = 400;
        } else if (exception instanceof AuthException) {
            responseCode = 401;
        } else if (exception instanceof HttpMessageNotReadableException) {
            responseCode = 400;
        }

        if (responseCode <= 0) {
            responseCode = 500;
        }

        return responseCode;
    }

    private static void writeResponse(HttpServletResponse response, String responseJson) {
        try {
            response.getWriter().write(responseJson);
        } catch (Exception e) {
            log.warn("Cannot send error information to client. Reason[" + e.getClass() + "]: " + e.getMessage());
        }
    }
}
