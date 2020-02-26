package kr.kdev.demo.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
@RequestMapping("/api")
public abstract class BaseApi {

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    protected static String ERROR = "error";
    protected static String TIMESTAMP = "timestamp";
    protected static String MESSAGE = "message";

    @Autowired protected MessageSource messageSource;

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        e.printStackTrace();
        Map<String, Object> data = new HashMap<>();
        data.put(TIMESTAMP, System.currentTimeMillis());
        data.put(ERROR, e.getMessage());
        return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {HttpClientErrorException.Forbidden.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Map<String, Object>> unForbiddenException(Exception e) {
        Map<String, Object> data = new HashMap<>();
        data.put(TIMESTAMP, System.currentTimeMillis());
        data.put(ERROR, HttpStatus.FORBIDDEN.getReasonPhrase());
        return new ResponseEntity<>(data, HttpStatus.FORBIDDEN);
    }
}
