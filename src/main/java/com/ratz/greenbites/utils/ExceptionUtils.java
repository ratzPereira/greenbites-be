package com.ratz.greenbites.utils;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ratz.greenbites.exception.ApiException;
import com.ratz.greenbites.response.HttpResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import java.io.OutputStream;

import static java.time.LocalTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class ExceptionUtils {

    public static void processError(HttpServletRequest request, HttpServletResponse response, Exception e) {
        HttpResponse httpResponse;
        if (e instanceof ApiException || e instanceof DisabledException || e instanceof LockedException ||
                e instanceof InvalidClaimException || e instanceof TokenExpiredException ||
                e instanceof BadCredentialsException) {

            httpResponse = getHttpResponse(response, e.getMessage(), HttpStatus.BAD_REQUEST);
        } else {
            httpResponse = getHttpResponse(response, "An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        writeResponse(response, httpResponse);

        log.error("An error occurred while processing the request: {}", e.getMessage());
    }

    private static void writeResponse(HttpServletResponse response, HttpResponse httpResponse) {
        OutputStream out;

        try {
            out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, httpResponse);

            out.flush();

        } catch (Exception e) {
            log.error("An error occurred while processing the request: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private static HttpResponse getHttpResponse(HttpServletResponse response, String message, HttpStatus httpStatusCode) {
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(message)
                .statusCode(httpStatusCode.value())
                .build();

        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(httpStatusCode.value());

        return httpResponse;
    }
}
