package kisung.moin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.regex.Pattern;

public class UserDto {
  private static final String USER_ID_PATTERN = "^[a-z0-9A-Z._-]*@[a-z0-9A-Z]*.[a-zA-Z.]*$";
  private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";
  private static final String REG_ID_VALUE_PATTERN = "^\\d{6}-?\\d{7}$";
  private static final String BUSINESS_ID_VALUE_PATTERN = "^\\d{3}-\\d{2}-\\d{5}$";

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PostSignUpReq {
    @Schema(description = "유저 아이디(이메일)", example = "lion0193@gmail.com")
    private String userId;
    @Schema(description = "비밀번호 (영문 숫자 특수기호 조합 8자리 이상)", example = "qwer1234!")
    private String password;
    @Schema(description = "이름", example = "구기성")
    private String name;
    @Schema(description = "회원 유형", example = "REG_NO")
    private String idType;
    @Schema(description = "주민등록번호", example = "960906-0000000")
    private String idValue;

    public boolean validateUserId() {
      return Pattern.matches(USER_ID_PATTERN, userId);
    }
    public boolean validatePassword() {
      return Pattern.matches(PASSWORD_PATTERN, password);
    }
    public boolean validateRegIdValue() {
      return Pattern.matches(REG_ID_VALUE_PATTERN, idValue);
    }
    public boolean validateBusinessIdValue() {
      return Pattern.matches(BUSINESS_ID_VALUE_PATTERN, idValue);
    }
  }

  @Getter
  @Builder
  public static class PostSignUpRes {
    @Schema(description = "유저 고유 아이디", example = "1")
    private Long id;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PostLoginReq {
    @Schema(description = "유저 아이디(이메일)", example = "lion0193@gmail.com")
    private String userId;
    @Schema(description = "비밀번호 (영문 숫자 특수기호 조합 8자리 이상)", example = "qwer1234!")
    private String password;

    public boolean validateUserId() {
      return Pattern.matches(USER_ID_PATTERN, userId);
    }
    public boolean validatePassword() {
      return Pattern.matches(PASSWORD_PATTERN, password);
    }
  }

  @Getter
  @Builder
  public static class PostLoginRes {
    @Schema(description = "jwt 토큰", example = "asdasdasd")
    private String token;
  }

  @Getter
  @Builder
  public static class UserBasicInfo {
    @Schema(description = "유저 아이디", example = "1")
    private Long id;
    @Schema(description = "유저 이메일 아이디", example = "lion0193@gmail.com")
    private String userId;
    @Schema(description = "이름", example = "구기성")
    private String name;
    @Schema(description = "회원 유형", example = "REG_NO")
    private String idType;
  }
}
