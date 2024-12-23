package kisung.moin.repository.quote.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kisung.moin.entity.Quote;
import kisung.moin.repository.quote.custom.CustomQuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static kisung.moin.entity.QQuote.quote;
import static kisung.moin.enums.Status.ACTIVE;

@Repository
@RequiredArgsConstructor
public class QuoteRepositoryImpl implements CustomQuoteRepository {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Optional<Quote> findQuoteById(Long quoteId) {
    return Optional.ofNullable(
        jpaQueryFactory
            .select(quote)
            .from(quote)
            .where(
                quote.id.eq(quoteId),
                quote.status.eq(ACTIVE.value())
            )
            .fetchFirst()
    );
  }
}
