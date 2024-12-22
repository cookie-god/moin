package kisung.moin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kisung.moin.common.response.BasicResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class TransferDto {

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PostQuoteReq {
    @Schema(description = "금액", example = "10000")
    private Double amount;
    @Schema(description = "환전 나라", example = "JPY")
    private String targetCurrency;
  }

  @Getter
  @SuperBuilder
  public static class PostQuoteRes extends BasicResponse {
    @Schema(description = "견적서 정보")
    private QuoteInfo quote;
  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpbitInfo {
    private String code;
    private String currencyCode;
    private Double basePrice;
    private Long currencyUnit;
  }

  @Data
  @Builder
  public static class QuoteInfo {
    @Schema(description = "견적서 아이디", example = "1")
    private Long quoteId;
    @Schema(description = "환율", example = "9.013")
    private Double exchageRate;
    @Schema(description = "만료 날짜")
    private LocalDateTime expireTime;
    @Schema(description = "환전후 금액", example = "630.91")
    private BigDecimal targetAmount;
  }
}
