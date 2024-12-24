package kisung.moin.repository.transfer.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kisung.moin.dto.TransferDto;
import kisung.moin.entity.Transfer;
import kisung.moin.repository.transfer.custom.CustomTransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static kisung.moin.entity.QQuote.quote;
import static kisung.moin.entity.QTransfer.transfer;
import static kisung.moin.enums.Status.ACTIVE;

@Repository
@RequiredArgsConstructor
public class TransferRepositoryImpl implements CustomTransferRepository {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Double findTransferAmountByUserId(Long id) {
    LocalDate today = LocalDate.now(); // 오늘 날짜
    LocalDateTime startOfDay = today.atStartOfDay(); // 오늘의 00:00:00
    LocalDateTime endOfDay = today.atTime(LocalTime.MAX); // 오늘의 23:59:59.999

    return jpaQueryFactory
        .select(quote.amount.sum().coalesce(0.0))
        .from(transfer).leftJoin(quote).on(transfer.quote.id.eq(quote.id)).fetchJoin()
        .where(
            transfer.userInfo.id.eq(id),
            transfer.requestedDate.between(startOfDay, endOfDay),
            transfer.status.eq(ACTIVE.value())
        )
        .fetchFirst();
  }

  @Override
  public Optional<Transfer> findTransferByQuoteId(Long quoteId) {
    return Optional.ofNullable(
        jpaQueryFactory
            .select(transfer)
            .from(transfer)
            .where(
                transfer.quote.id.eq(quoteId),
                transfer.status.eq(ACTIVE.value())
            )
            .fetchFirst()
    );
  }

  @Override
  public List<TransferDto.TransferInfo> findTransferHistoriesByUserId(Long id) {
    return jpaQueryFactory
        .select(
            Projections.bean(TransferDto.TransferInfo.class,
                quote.amount.as("sourceAmount"),
                quote.fee,
                quote.usdExchangeRate,
                quote.usdTargetAmount.as("usdAmount"),
                quote.currencyCode.as("targetCurrency"),
                quote.exchangeRate,
                quote.targetAmount,
                transfer.requestedDate
            )
        )
        .from(transfer).leftJoin(quote).on(transfer.quote.id.eq(quote.id)).fetchJoin()
        .where(
            transfer.userInfo.id.eq(id),
            transfer.status.eq(ACTIVE.value())
        )
        .fetch();
  }
}
