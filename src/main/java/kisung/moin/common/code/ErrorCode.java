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
  NON_EXIST_NICKNAME(400, "USER_ERROR_003", "Nickname is empty"),
  INVALID_EMAIL(400, "USER_ERROR_004", "Email is invalid"),
  INVALID_PASSWORD(400, "USER_ERROR_005", "Password is invalid"),
  INVALID_NICKNAME(400, "USER_ERROR_006", "Nickname is invalid"),
  DUPLICATE_EMAIL(404, "USER_ERROR_007", "email is already exist"),
  DUPLICATE_NICKNAME(404, "USER_ERROR_008", "Nickname is already exist"),
  NOT_EXIST_USER_BY_EMAIL(404, "USER_ERROR_009", "User's email is not exist."),
  WRONG_PASSWORD(404, "USER_ERROR_010", "User's password is wrong."),
  NON_EXIST_USER(404, "USER_ERROR_011", "Not exist user"),
  NON_EXIST_NEW_PASSWORD(400, "USER_ERROR_012", "New Password is empty"),
  INVALID_NEW_PASSWORD(400, "USER_ERROR_013", "New Password is invalid"),

  NON_EXIST_FEED_CONTENT(400, "FEED_ERROR_001", "Content is empty"),
  NON_EXIST_FEED_ID(400, "FEED_ERROR_002", "Feed Id is empty"),
  NON_EXIST_PAGE_SIZE(400, "FEED_ERROR_003", "Page Size is empty"),
  NON_EXIST_FEED(404, "FEED_ERROR_004", "Not exist feed"),
  NOT_MY_FEED(404, "FEED_ERROR_005", "Not my feed"),


  NON_EXIST_COMMENT_CONTENT(400, "COMMENT_ERROR_001", "Content is empty"),
  NON_EXIST_COMMENT(404, "COMMENT_ERROR_002", "Not exist comment"),
  NON_EXIST_PARENT_COMMENT(404, "COMMENT_ERROR_003", "Not exist parent comment"),
  INVALID_CONTENT(400, "COMMENT_ERROR_004", "Comment content' length is under 300 characters"),
  NON_EXIST_PARENT_COMMENT_ID(400, "COMMENT_ERROR_005", "Parent Comment Id is empty"),
  NON_EXIST_COMMENT_ID(400, "COMMENT_ERROR_006", "Comment Id is empty"),


  INTERNAL_SERVER_ERROR(500, "SERVER_ERROR_001", "Server Error");

  private final int status;
  private final String code;
  private final String message;
}
