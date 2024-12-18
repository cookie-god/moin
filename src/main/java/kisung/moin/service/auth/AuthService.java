package kisung.moin.service.auth;

import kisung.moin.entity.UserInfo;

public interface AuthService {
  UserInfo retrieveUserInfoById(Long userId);
}
