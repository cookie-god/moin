package kisung.moin.repository.transfer.custom;

import kisung.moin.entity.Transfer;

import java.util.Optional;

public interface CustomTransferRepository {
  Double findTransferAmountByUserId(Long id);
  Optional<Transfer> findTransferByQuoteId(Long quoteId);
}
