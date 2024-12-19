package kisung.moin.service.user;

import kisung.moin.config.exception.MoinException;
import kisung.moin.config.jwt.JwtTokenProvider;
import kisung.moin.dto.UserDto;
import kisung.moin.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kisung.moin.common.code.ErrorCode.*;
import static kisung.moin.enums.IdType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final JwtTokenProvider jwtTokenProvider;


  @Transactional
  @Override
  public UserDto.PostSignUpRes createUsers(UserDto.PostSignUpReq postSignUpReq) {
    validate(postSignUpReq);
    return UserDto.PostSignUpRes.builder()
        .id(1L)
        .build();
  }

  private void validate(UserDto.PostSignUpReq postSignUpReq) {
    if (postSignUpReq.getUserId() == null || postSignUpReq.getUserId().isEmpty()) {
      throw new MoinException(NON_EXIST_EMAIL);
    }
    if (!postSignUpReq.validateUserId()) {
      throw new MoinException(INVALID_EMAIL);
    }
    if (postSignUpReq.getPassword() == null || postSignUpReq.getPassword().isEmpty()) {
      throw new MoinException(NON_EXIST_PASSWORD);
    }
    if (!postSignUpReq.validatePassword()) {
      throw new MoinException(INVALID_PASSWORD);
    }
    if (postSignUpReq.getName() == null || postSignUpReq.getName().isEmpty()) {
      throw new MoinException(NON_EXIST_NAME);
    }
    if (postSignUpReq.getIdType() == null || postSignUpReq.getIdType().isEmpty()) {
      throw new MoinException(NON_EXIST_ID_TYPE);
    }
    if (!postSignUpReq.getIdType().equals(REG_NO.value()) && !postSignUpReq.getIdType().equals(BUSINESS_NO.value())) {
      throw new MoinException(INVALID_ID_TYPE);
    }
    if (postSignUpReq.getIdValue() == null || postSignUpReq.getIdValue().isEmpty()) {
      throw new MoinException(NON_EXIST_ID_VALUE);
    }
    if (postSignUpReq.getIdType().equals(REG_NO.value())) {
      if (!postSignUpReq.validateRegIdValue()) {
        throw new MoinException(INVALID_REG_ID_VALUE);
      }
    }
    if (postSignUpReq.getIdType().equals(BUSINESS_NO.value())) {
      if (!postSignUpReq.validateBusinessIdValue()) {
        throw new MoinException(INVALID_BUSINESS_ID_VALUE);
      }
    }

//    if (userRepository.existsByEmail(postUserReq.getEmail())) {
//      throw new BoardException(DUPLICATE_EMAIL);
//    }
//    if (userRepository.existsByNickname(postUserReq.getNickname())) {
//      throw new BoardException(DUPLICATE_NICKNAME);
//    }
  }
}
