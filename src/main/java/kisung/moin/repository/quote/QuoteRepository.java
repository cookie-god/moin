package kisung.moin.repository.quote;

import kisung.moin.entity.Quote;
import kisung.moin.repository.quote.custom.CustomQuoteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long>, CustomQuoteRepository {
}
