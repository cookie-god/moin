package kisung.moin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class TransferDto {

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PostQuoteReq {
    @Schema(description = "금액", example = "10000")
    private Long amount;
    @Schema(description = "환전 나라", example = "JPY")
    private String targetCurrency;
  }

  @Getter
  @Builder
  public static class PostQuoteRes {
    @Schema(description = "체크용", example = "1")
    private Long id;
  }
}
