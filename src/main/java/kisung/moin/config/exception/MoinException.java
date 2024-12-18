package kisung.moin.config.exception;

import kisung.moin.common.code.ErrorCode;
import lombok.Getter;

@Getter
public class MoinException extends RuntimeException {
  private final ErrorCode errorCode;

  public MoinException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
