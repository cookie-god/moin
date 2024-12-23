package kisung.moin.repository.transfer.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kisung.moin.repository.transfer.custom.CustomTransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TransferRepositoryImpl implements CustomTransferRepository {
  private final JPAQueryFactory jpaQueryFactory;
}
