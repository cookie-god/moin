package kisung.moin.repository.user.custom;

import kisung.moin.entity.UserInfo;

import java.util.Optional;

public interface CustomUserRepository {
  Optional<UserInfo> findUserInfoById(Long userId);
}