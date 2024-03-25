package com.ratz.greenbites.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder(builderClassName = "HttpResponseBuilder")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class HttpResponse {

    private String timeStamp;
    private int statusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;
    private String developerMessage;
    private Map<String, Object> data;

    // Construtor privado para forçar o uso do builder
    private HttpResponse(String timeStamp, int statusCode, HttpStatus httpStatus, String reason, String message, String developerMessage, Map<String, Object> data) {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.httpStatus = httpStatus;
        this.reason = reason;
        this.message = message;
        this.developerMessage = developerMessage;
        this.data = data;
    }

    // Métodos estáticos auxiliares para facilitar a criação de respostas
    public static HttpResponseBuilder ok(String message, Map<String, Object> data) {
        return HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message(message)
                .data(data);
    }

    public static HttpResponseBuilder error(HttpStatus status, String message, String developerMessage) {
        return HttpResponse.builder()
                .timeStamp(LocalDateTime.now().toString())
                .statusCode(status.value())
                .httpStatus(status)
                .message(message)
                .developerMessage(developerMessage);
    }
}
