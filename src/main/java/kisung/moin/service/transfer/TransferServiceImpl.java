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
import static kisung.moin.enums.IdType.REG_NO;
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
    validate(postQuoteReq); // request에 대한 검증
    List<TransferDto.UpbitInfo> upbitInfos = webClientService.retrieveUpbitPriceInfo();
    TransferDto.UpbitInfo usdUpbitInfo = upbitInfos.stream().filter(data -> data.getCurrencyCode().equals("USD")).findFirst().orElseThrow(() -> new MoinException(INTERNAL_SERVER_ERROR)); // usd 환율 정보 확인
    TransferDto.UpbitInfo upbitInfo = upbitInfos.stream().filter(data -> data.getCurrencyCode().equals(postQuoteReq.getTargetCurrency())).findFirst().orElseThrow(() -> new MoinException(INTERNAL_SERVER_ERROR)); // request에 맞는 환율 정보 확인
    BigDecimal fee = makeFee(postQuoteReq.getTargetCurrency(), BigDecimal.valueOf(postQuoteReq.getAmount())); // 수수료 계산
    BigDecimal targetAmount = makeTargetAmount(BigDecimal.valueOf(postQuoteReq.getAmount()), fee, upbitInfo); // 목표 송금액 계산
    BigDecimal usdFee = makeFee("USD", BigDecimal.valueOf(postQuoteReq.getAmount())); // 달러 수수료 계산
    BigDecimal usdTargetAmount = makeTargetAmount(BigDecimal.valueOf(postQuoteReq.getAmount()), usdFee, usdUpbitInfo); // 달러 목표 송금액 계산
    Double exchangeRate = upbitInfo.getBasePrice() / upbitInfo.getCurrencyUnit(); // 기본 단위량 계산
    Double usdExchangeRate = usdUpbitInfo.getBasePrice() / usdUpbitInfo.getCurrencyUnit(); // 달러 기본 단위량 계산
    Quote quote = createQuoteEntity(userInfo, upbitInfo.getCode(), upbitInfo.getCurrencyCode(), usdExchangeRate, usdTargetAmount.doubleValue(), postQuoteReq.getAmount(), fee.doubleValue(), exchangeRate, targetAmount.doubleValue()); // 견적서 엔티티 생성
    quote = quoteRepository.save(quote); // 저장
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
    validate(postQuoteRequestReq); // request에 대한 검증
    Quote quote = quoteRepository.findQuoteById(postQuoteRequestReq.getQuoteId()).orElseThrow(() -> new MoinException(NON_EXIST_QUOTE)); // 견적서 조회 및 에러 처리
    if (quote.getExpireTime().isBefore(LocalDateTime.now())) { // 견적서 만료기한 체크
      throw new MoinException(INVALID_QUOTE);
    }
    Transfer checkTransfer = transferRepository.findTransferByQuoteId(quote.getId()).orElse(null); // 해당 견적서에 대한 송금 요청 존재 여부 체크
    if (checkTransfer != null) { // 견적서 이미 존재하는 경우 에러 처리
      throw new MoinException(ALREADY_EXIST_TRANSFER);
    }
    TransferDto.UpbitInfo upbitInfo = webClientService.retrieveUpbitPriceInfo().stream() // 업비트 가격 정보 api를 통해 미국 달러 확인
        .filter(data -> data.getCurrencyCode().equals("USD")).findFirst()
        .orElseThrow(() -> new MoinException(INTERNAL_SERVER_ERROR)); // 업비트 가격 정보

    double limitedAmount = makeLimitAmount(userInfo, upbitInfo); // 유저별 최대 송금 금액 계산
    double totalAmount = transferRepository.findTransferAmountByUserId(userInfo.getId()); // 송금 금액 확인

    log.info("limitedAmount = {}", limitedAmount);
    log.info("totalAmount = {}", totalAmount);
    log.info("totalAmount with amount = {}", totalAmount + quote.getAmount());

    if (limitedAmount < totalAmount + quote.getAmount()) { // 송금하려는 금액과 이전 송금량이 유저별 최대 송금 금액보다 큰 경우 에러 처리
      throw new MoinException(QUOTE_LIMIT_EXCESS);
    }
    Transfer transfer = createTransferEntity(userInfo, quote); // 송금 엔티티 생성
    transferRepository.save(transfer); // 저장
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
    BigDecimal fee; // 수수료
    BigDecimal oneMillion = new BigDecimal("1000000"); // 미국 달러 100만원 기준점
    BigDecimal usdRateLow = new BigDecimal("0.002"); // 미국 달러 100만원 이하 수수료 0.2%
    BigDecimal usdRateHigh = new BigDecimal("0.001"); // 미국 달러 100만원 초과 수수료 0.1%
    BigDecimal jpyRate = new BigDecimal("0.005"); // 일본 엔화 수수료 0.5%
    BigDecimal fixedFeeUsdLow = new BigDecimal("1000"); // 미국 달러 100만원 이하 고정 수수료 1000원
    BigDecimal fixedFeeUsdHigh = new BigDecimal("3000"); // 미국 달러 100만원 초과 고정 수수료 3000원
    BigDecimal fixedFeeJpy = new BigDecimal("3000"); // 일본 엔화 고정 수수료 3000원

    if ("USD".equals(targetCurrency)) {
      if (amount.compareTo(oneMillion) <= 0) {
        fee = amount.multiply(usdRateLow).setScale(12, HALF_UP); // 12자리 계산
        fee = fee.add(fixedFeeUsdLow); // 고정 수수료 더함
      } else {
        fee = amount.multiply(usdRateHigh).setScale(12, HALF_UP); // 12자리 계산
        fee = fee.add(fixedFeeUsdHigh); // 고정 수수료 더함
      }
    } else {
      fee = amount.multiply(jpyRate).setScale(12, HALF_UP); // 12자리 계산
      fee = fee.add(fixedFeeJpy); // 고정 수수료 더함
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
    BigDecimal netAmount = amount.subtract(fee).setScale(12, HALF_UP); // 기존 금액에서 수수료 뺌
    BigDecimal rate = BigDecimal.valueOf(upbitInfo.getBasePrice() / upbitInfo.getCurrencyUnit()).setScale(12, HALF_UP); // 통화 단위로 환전 진행
    Currency usd = Currency.getInstance(upbitInfo.getCurrencyCode()); // 통화 단위로 소수점 반올림 하기 위해 사용
    return netAmount.divide(rate, usd.getDefaultFractionDigits(), RoundingMode.HALF_UP); // 통화 단위로 계산
  }

  /**
   * 회원별 일별 송금 요청 금액 확인
   * 1. 회원별 정의
   */
  private double makeLimitAmount(UserInfo userInfo, TransferDto.UpbitInfo upbitInfo) {
    if (userInfo.getIdType().equals(REG_NO.value())) {
      return upbitInfo.getBasePrice() * 1000;
    } else {
      return upbitInfo.getBasePrice() * 5000;
    }
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
  private Quote createQuoteEntity(UserInfo userInfo, String code, String currencyCode, Double usdExchangeRate, Double usdTargetAmount, Double amount, Double fee, Double exchangeRate, Double targetAmount) {
    LocalDateTime now = LocalDateTime.now();
    return Quote.builder()
        .userInfo(userInfo)
        .code(code)
        .currencyCode(currencyCode)
        .usdExchangeRate(usdExchangeRate)
        .usdTargetAmount(usdTargetAmount)
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
