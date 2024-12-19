package kisung.moin.common.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  INVALID_TOKEN(400, "AUTH_ERROR_001", "Token is invalid"),
  USER_AUTHORIZE_ERROR(401, "AUTH_ERROR_002", "Authorize Error"),

  NON_EXIST_EMAIL(400, "USER_ERROR_001", "Email is empty"),
  NON_EXIST_PASSWORD(400, "USER_ERROR_002", "Password is empty"),
  NON_EXIST_NAME(400, "USER_ERROR_003", "Nickname is empty"),
  INVALID_EMAIL(400, "USER_ERROR_004", "Email is invalid"),
  INVALID_PASSWORD(400, "USER_ERROR_005", "Password is invalid"),
  NON_EXIST_ID_TYPE(400, "USER_ERROR_006", "Id Type is empty"),
  INVALID_ID_TYPE(400, "USER_ERROR_007", "Id Type is invalid"),
  NON_EXIST_ID_VALUE(400, "USER_ERROR_008", "Id Value is empty"),
  INVALID_REG_ID_VALUE(400, "USER_ERROR_009", "Reg Id Value is invalid"),
  INVALID_BUSINESS_ID_VALUE(400, "USER_ERROR_010", "Business Id Value is invalid"),
  DUPLICATE_EMAIL(404, "USER_ERROR_011", "email is already exist"),
  DUPLICATE_NICKNAME(404, "USER_ERROR_012", "Nickname is already exist"),
  NOT_EXIST_USER_BY_EMAIL(404, "USER_ERROR_013", "User's email is not exist."),
  WRONG_PASSWORD(404, "USER_ERROR_014", "User's password is wrong."),
  NON_EXIST_USER(404, "USER_ERROR_015", "Not exist user"),


  INTERNAL_SERVER_ERROR(500, "SERVER_ERROR_001", "Server Error");

  private final int status;
  private final String code;
  private final String message;
}
