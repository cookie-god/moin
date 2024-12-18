package kisung.moin.repository.user.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kisung.moin.entity.UserInfo;
import kisung.moin.repository.user.custom.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static kisung.moin.entity.QUserInfo.userInfo;
import static kisung.moin.enums.Status.ACTIVE;

@Repository
@Slf4j
@RequiredArgsConstructor
public class UserRepositoryImpl implements CustomUserRepository {
  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Optional<UserInfo> findUserInfoById(Long userId) {
    return Optional.ofNullable(
        jpaQueryFactory
            .select(userInfo)
            .from(userInfo)
            .where(
                userInfo.userId.eq(userId),
                userInfo.status.eq(ACTIVE.value())
            )
            .fetchFirst()
    );
  }
}
