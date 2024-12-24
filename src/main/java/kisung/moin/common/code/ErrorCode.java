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
  WRONG_PASSWORD(404, "USER_ERROR_012", "User's password is wrong."),
  NON_EXIST_USER(404, "USER_ERROR_013", "Not exist user"),

  NON_EXIST_AMOUNT(400, "QUOTE_ERROR_001", "Amount is empty"),
  INVALID_AMOUNT(400, "QUOTE_ERROR_002", "Amount is over 10000 won"),
  NEGATIVE_NUMBER(400, "QUOTE_ERROR_003", "Amount is negative number"),
  NON_EXIST_TARGET_CURRENCY(400, "QUOTE_ERROR_004", "Target Currency is empty"),
  INVALID_TARGET_CURRENCY(400, "QUOTE_ERROR_005", "Target Currency is only JPY, USD"),
  NON_EXIST_QUOTE_ID(400, "QUOTE_ERROR_006", "Quote id is empty"),
  NON_EXIST_QUOTE(404, "QUOTE_ERROR_007", "Not exist quote"),
  INVALID_QUOTE(404, "QUOTE_ERROR_008", "Quote's expire time is over 10 minutes"),
  QUOTE_LIMIT_EXCESS(404, "QUOTE_ERROR_009", "Daliy limit is over"),
  ALREADY_EXIST_TRANSFER(404, "QUOTE_ERROR_010", "Already exist transfer"),

  WEB_CLIENT_ERROR(404, "CALL_ERROR_001", "Client error in Other Server"),
  WEB_SERVER_ERROR(404, "CALL_ERROR_002", "Error in Other Server"),


  INTERNAL_SERVER_ERROR(500, "SERVER_ERROR_001", "Server Error");

  private final int status;
  private final String code;
  private final String message;
}
