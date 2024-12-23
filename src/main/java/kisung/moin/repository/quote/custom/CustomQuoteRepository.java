package kisung.moin.repository.quote.custom;

import kisung.moin.entity.Quote;

import java.util.Optional;

public interface CustomQuoteRepository {
  Optional<Quote> findQuoteById(Long quoteId);
}
