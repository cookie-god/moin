package kisung.moin.service.auth;

import kisung.moin.entity.UserInfo;
import kisung.moin.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;

  @Override
  public UserInfo retrieveUserInfoById(Long userId) {
    return userRepository.findUserInfoById(userId).orElse(null);
  }
}
