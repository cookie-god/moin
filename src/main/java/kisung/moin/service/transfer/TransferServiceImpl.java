package kisung.moin.service.transfer;

import kisung.moin.config.exception.MoinException;
import kisung.moin.dto.TransferDto;
import kisung.moin.entity.Quote;
import kisung.moin.entity.Transfer;
import kisung.moin.entity.UserInfo;
import kisung.moin.repository.quote.QuoteRepository;
import kisung.moin.repository.transfer.TransferRepository;
import kisung.moin.service.webclient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

import static java.math.RoundingMode.HALF_UP;
import static kisung.moin.common.code.ErrorCode.*;
import static kisung.moin.enums.Status.ACTIVE;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {
  private final QuoteRepository quoteRepository;
  private final TransferRepository transferRepository;
  private final WebClientService webClientService;

  @Transactional
  @Override
  public TransferDto.PostQuoteRes createQuotes(UserInfo userInfo, TransferDto.PostQuoteReq postQuoteReq) {
    validate(postQuoteReq);
    TransferDto.UpbitInfo upbitInfo = webClientService.retrieveUpbitPriceInfo().stream()
        .filter(data -> data.getCurrencyCode().equals(postQuoteReq.getTargetCurrency())).findFirst()
        .orElseThrow(() -> new MoinException(INTERNAL_SERVER_ERROR)); // 업비트 가격 정보
    BigDecimal fee = makeFee(postQuoteReq.getTargetCurrency(), BigDecimal.valueOf(postQuoteReq.getAmount()));
    BigDecimal targetAmount = makeTargetAmount(BigDecimal.valueOf(postQuoteReq.getAmount()), fee, upbitInfo);
    Double exchangeRate = upbitInfo.getBasePrice() / upbitInfo.getCurrencyUnit();
    Quote quote = createQuoteEntity(userInfo, upbitInfo.getCode(), upbitInfo.getCurrencyCode(), postQuoteReq.getAmount(), fee.doubleValue(), exchangeRate, targetAmount.doubleValue());
    quote = quoteRepository.save(quote);

    return TransferDto.PostQuoteRes.builder()
        .quote(
            TransferDto.QuoteInfo.builder()
                .quoteId(quote.getId())
                .exchageRate(exchangeRate)
                .expireTime(quote.getExpireTime())
                .targetAmount(targetAmount)
                .build()
        )
        .build();
  }

  @Transactional
  @Override
  public TransferDto.PostQuoteRequestRes createQuoteRequests(UserInfo userInfo, TransferDto.PostQuoteRequestReq postQuoteRequestReq) {
    validate(postQuoteRequestReq);
    Quote quote = quoteRepository.findQuoteById(postQuoteRequestReq.getQuoteId()).orElseThrow(() -> new MoinException(NON_EXIST_QUOTE));
    if (quote.getExpireTime().isBefore(LocalDateTime.now())) {
      throw new MoinException(INVALID_QUOTE);
    }
    Transfer transfer = createTransferEntity(userInfo, quote);
    transferRepository.save(transfer);
    return TransferDto.PostQuoteRequestRes.builder().build();
  }

  /**
   * 수수료 계산
   * 1. USD or JPY 나누기
   * 2. 금액별 수수료율 체크
   * 3. 나라별 고정 수수료 계산
   * 4. 수수료율 계산
   */
  private BigDecimal makeFee(String targetCurrency, BigDecimal amount) {
    BigDecimal fee;
    BigDecimal oneMillion = new BigDecimal("1000000");
    BigDecimal usdRateLow = new BigDecimal("0.002");
    BigDecimal usdRateHigh = new BigDecimal("0.001");
    BigDecimal jpyRate = new BigDecimal("0.005");
    BigDecimal fixedFeeUsdLow = new BigDecimal("1000");
    BigDecimal fixedFeeUsdHigh = new BigDecimal("3000");
    BigDecimal fixedFeeJpy = new BigDecimal("3000");

    if ("USD".equals(targetCurrency)) {
      if (amount.compareTo(oneMillion) <= 0) {
        fee = amount.multiply(usdRateLow).setScale(12, HALF_UP);
        fee = fee.add(fixedFeeUsdLow);
      } else {
        fee = amount.multiply(usdRateHigh).setScale(12, HALF_UP);
        fee = fee.add(fixedFeeUsdHigh);
      }
    } else {
      fee = amount.multiply(jpyRate).setScale(12, HALF_UP);
      fee = fee.add(fixedFeeJpy);
    }
    return fee;
  }

  /**
   * 최종 금액 계산
   * 1. USD or JPY 나누기
   * 2. 금액별 수수료율 체크
   * 3. 나라별 고정 수수료 계산
   * 4. 수수료율 계산
   */
  private BigDecimal makeTargetAmount(BigDecimal amount, BigDecimal fee, TransferDto.UpbitInfo upbitInfo) {
    BigDecimal netAmount = amount.subtract(fee).setScale(12, HALF_UP);
    BigDecimal rate = BigDecimal.valueOf(upbitInfo.getBasePrice() / upbitInfo.getCurrencyUnit()).setScale(12, HALF_UP);
    Currency usd = Currency.getInstance(upbitInfo.getCurrencyCode());
    return netAmount.divide(rate, usd.getDefaultFractionDigits(), RoundingMode.HALF_UP);
  }

  /**
   * 견적서 생성 validate
   * request에 대한 값 체크하는 메서드
   * 1. 금액 존재 여부 체크
   * 2. 금액 음수값 체크
   * 3. 환전 나라 존재 여부 체크
   * 4. 환전 가능 나라 체크
   */
  private void validate(TransferDto.PostQuoteReq postQuoteReq) {
    if (postQuoteReq.getAmount() == null || postQuoteReq.getAmount() == 0) {
      throw new MoinException(NON_EXIST_AMOUNT);
    }
    if (postQuoteReq.getAmount() < 0) {
      throw new MoinException(INVALID_AMOUNT);
    }
    if (postQuoteReq.getTargetCurrency() == null || postQuoteReq.getTargetCurrency().isEmpty()) {
      throw new MoinException(NON_EXIST_TARGET_CURRENCY);
    }
    if (!List.of("JPY", "USD").contains(postQuoteReq.getTargetCurrency())) {
      throw new MoinException(INVALID_TARGET_CURRENCY);
    }
  }

  /**
   * 송금 접수 요청 validate
   * request에 대한 값 체크하는 메서드
   * 1. 견적서 아이디 존재 여부 체크
   * 2. 금액 음수값 체크
   * 3. 환전 나라 존재 여부 체크
   * 4. 환전 가능 나라 체크
   */
  private void validate(TransferDto.PostQuoteRequestReq postQuoteRequestReq) {
    if (postQuoteRequestReq.getQuoteId() == null || postQuoteRequestReq.getQuoteId() == 0) {
      throw new MoinException(NON_EXIST_QUOTE_ID);
    }
  }

  /**
   * 견적서 엔티티 인스턴스 생성하는 메서드
   */
  private Quote createQuoteEntity(UserInfo userInfo, String code, String currencyCode, Double amount, Double fee, Double exchangeRate, Double targetAmount) {
    LocalDateTime now = LocalDateTime.now();
    return Quote.builder()
        .userInfo(userInfo)
        .code(code)
        .currencyCode(currencyCode)
        .amount(amount)
        .fee(fee)
        .exchangeRate(exchangeRate)
        .targetAmount(targetAmount)
        .expireTime(now.plusMinutes(10))
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.value())
        .build();
  }

  /**
   * 견적서 엔티티 인스턴스 생성하는 메서드
   */
  private Transfer createTransferEntity(UserInfo userInfo, Quote quote) {
    LocalDateTime now = LocalDateTime.now();
    return Transfer.builder()
        .userInfo(userInfo)
        .quote(quote)
        .requestedDate(now)
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.value())
        .build();
  }

}
