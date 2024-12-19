package kisung.moin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.regex.Pattern;

public class UserDto {
  private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$";

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
