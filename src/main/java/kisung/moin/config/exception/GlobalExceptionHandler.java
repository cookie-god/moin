package kisung.moin.config.exception;

import kisung.moin.common.code.ErrorCode;
import kisung.moin.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(MoinException.class)
  public ResponseEntity<ErrorResponse> MoinExceptionHandler(MoinException exception) {
    ErrorResponse errorResponse = ErrorResponse.of(exception.getErrorCode());
    return ResponseEntity
        .status(errorResponse.getStatus())
        .body(errorResponse);
  }

  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.USER_AUTHORIZE_ERROR);
    StackTraceElement[] stackTraceElements = ex.getStackTrace();
    log.error("error = {}", stackTraceElements[0]);
    return ResponseEntity
        .status(errorResponse.getStatus())
        .body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
    StackTraceElement[] stackTraceElements = ex.getStackTrace();
    log.error("error = {}", stackTraceElements[0]);
    return ResponseEntity
        .status(errorResponse.getStatus())
        .body(errorResponse);
  }
}
