package kisung.moin.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode {
  READ_SUCCESS(200, "SUCCESS-001", "SUCCESS"),
  CREATE_SUCCESS(201, "SUCCESS-001", "SUCCESS"),
  UPDATE_SUCCESS(204, "SUCCESS-001", "SUCCESS"),
  DELETE_SUCCESS(200, "SUCCESS-001", "SUCCESS");

  private final int status;
  private final String code;
  private final String message;
}
