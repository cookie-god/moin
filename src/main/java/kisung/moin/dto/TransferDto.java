package kisung.moin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kisung.moin.common.response.BasicResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

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
  @SuperBuilder
  public static class PostQuoteRes extends BasicResponse {
    @Schema(description = "체크용", example = "1")
    private List<UpbitInfo> upbitInfos;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpbitInfo {
    String code;
    String currencyCode;
    Double basePrice;
    Long currencyUnit;
  }
}
