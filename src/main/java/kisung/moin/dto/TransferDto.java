package kisung.moin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kisung.moin.common.response.BasicResponse;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransferDto {

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PostQuoteReq {
    @Schema(description = "금액 (10000원 이상)", example = "10000")
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
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class PostQuoteRequestReq {
    @Schema(description = "견적서 아이디", example = "1")
    private Long quoteId;
  }

  @Getter
  @SuperBuilder
  public static class PostQuoteRequestRes extends BasicResponse {
  }

  @Getter
  @SuperBuilder
  public static class GetTransfersRes extends BasicResponse {
    @Schema(description = "유저 아이디(이메일)", example = "lion0193@gmail.com")
    private String userId;
    @Schema(description = "이름", example = "구기성")
    private String name;
    @Schema(description = "오늘 송금 횟수", example = "1")
    private Long todayTransferCount;
    @Schema(description = "오늘 송금 금액", example = "457.10")
    private Double todayTransferUsdAmount;
    @Schema(description = "송금 내역")
    private List<TransferInfo> history = new ArrayList<>();
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

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class TransferInfo {
    @Schema(description = "원화 송금 요청액", example = "10000")
    private Double sourceAmount;
    @Schema(description = "수수료", example = "3000")
    private Double fee;
    @Schema(description = "usd 환율", example = "1301.01")
    private Double usdExchangeRate;
    @Schema(description = "usd 송금액", example = "305.14")
    private Double usdAmount;
    @Schema(description = "받는 환율 정보", example = "JPY")
    private String targetCurrency;
    @Schema(description = "환율", example = "9.013")
    private Double exchangeRate;
    @Schema(description = "환전후 금액", example = "630.91")
    private Double targetAmount;
    @Schema(description = "송금 요청 시간")
    private LocalDateTime requestedDate;
  }
}
