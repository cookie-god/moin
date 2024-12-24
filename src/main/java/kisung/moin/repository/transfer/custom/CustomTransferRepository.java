package kisung.moin.repository.transfer.custom;

import kisung.moin.dto.TransferDto;
import kisung.moin.entity.Transfer;

import java.util.List;
import java.util.Optional;

public interface CustomTransferRepository {
  Double findTransferAmountByUserId(Long id);
  Optional<Transfer> findTransferByQuoteId(Long quoteId);
  List<TransferDto.TransferInfo> findTransferHistoriesByUserId(Long id);
  Long findTransferCountByUserId(Long id);
}
