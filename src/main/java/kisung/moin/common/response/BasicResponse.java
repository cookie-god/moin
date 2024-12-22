package kisung.moin.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@SuperBuilder
@Getter
public abstract class BasicResponse {
  @Schema(description = "상태 코드", example = "200")
  @Builder.Default // 기본값 설정
  private int resultCode = 200;
  @Builder.Default // 기본값 설정
  @Schema(description = "응답 메시지", example = "OK")
  private String resultMsg = "OK";
}
