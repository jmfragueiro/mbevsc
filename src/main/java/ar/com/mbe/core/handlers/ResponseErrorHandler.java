package ar.com.mbe.core.handlers;

import ar.com.mbe.base.repos.ItemNotFoundException;
import ar.com.mbe.core.common.C;
import ar.com.mbe.core.common.E;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ResponseErrorHandler extends ResponseEntityExceptionHandler {
    private HttpHeaders getHttpHeaders() {
        var headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        return headers;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<String> AccessExceptionHandler(AccessDeniedException ex, WebRequest req) {
        try {
            return new ResponseEntity<>(
                    E.getJsonErrorString(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), C.MSJ_SEC_INF_NOACCES,
                                         ((ServletWebRequest) req).getRequest().getServletPath()),
                    this.getHttpHeaders(),
                    HttpStatus.UNAUTHORIZED);
        } catch (Exception exc) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    public final ResponseEntity<String> AuthenticationExceptionHandler(AuthenticationException ex, WebRequest req) {
        try {
            return new ResponseEntity<>(
                    E.getJsonErrorString(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), C.MSJ_ERR_UNAUTHORIZED,
                                         ((ServletWebRequest) req).getRequest().getServletPath()),
                    this.getHttpHeaders(),
                    HttpStatus.UNAUTHORIZED);
        } catch (Exception exc) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ExceptionHandler(SecurityException.class)
    public final ResponseEntity<String> SecurityExceptionHandler(SecurityException ex, WebRequest req) {
        try {
            return new ResponseEntity<>(
                    E.getJsonErrorString(HttpStatus.UNAUTHORIZED.value(), ex.getMessage(), C.MSJ_SEC_ERR_USERCANTOP,
                                         ((ServletWebRequest) req).getRequest().getServletPath()),
                    this.getHttpHeaders(),
                    HttpStatus.UNAUTHORIZED);
        } catch (Exception exc) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public final ResponseEntity<String> ItemNotFoundExceptionHandler(Exception ex, WebRequest req) {
        try {
            return new ResponseEntity<>(
                    E.getJsonErrorString(HttpStatus.NOT_FOUND.value(), ex.getMessage(), C.MSJ_ERR_DB_NOITEM,
                                         ((ServletWebRequest) req).getRequest().getServletPath()),
                    this.getHttpHeaders(),
                    HttpStatus.NOT_FOUND);
        } catch (Exception exc) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> ConverterErrorsHandler(MethodArgumentTypeMismatchException ex, WebRequest req) {
        try {
            return new ResponseEntity<>(
                    E.getJsonErrorString(HttpStatus.BAD_REQUEST.value(), ex.getMessage(), C.MSJ_ERR_BADFORMATREQUEST,
                                         ((ServletWebRequest) req).getRequest().getServletPath()),
                    this.getHttpHeaders(),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception exc) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> OtherExceptionHandler(Exception ex, WebRequest req) {
        try {
            return new ResponseEntity<>(
                    E.getJsonErrorString(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage(), C.MSJ_ERR_EXCEPCION,
                                         ((ServletWebRequest) req).getRequest().getServletPath()),
                    this.getHttpHeaders(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception exc) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
