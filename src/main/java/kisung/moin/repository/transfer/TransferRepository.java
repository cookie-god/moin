package kisung.moin.repository.transfer;

import kisung.moin.entity.Transfer;
import kisung.moin.repository.transfer.custom.CustomTransferRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long>, CustomTransferRepository {
}
