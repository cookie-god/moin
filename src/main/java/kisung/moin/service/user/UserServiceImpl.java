package kisung.moin.service.user;

import kisung.moin.config.AESUtil;
import kisung.moin.config.exception.MoinException;
import kisung.moin.config.jwt.JwtTokenProvider;
import kisung.moin.dto.UserDto;
import kisung.moin.entity.UserInfo;
import kisung.moin.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static kisung.moin.common.code.ErrorCode.*;
import static kisung.moin.enums.IdType.*;
import static kisung.moin.enums.Status.ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final AESUtil aesUtil;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final JwtTokenProvider jwtTokenProvider;


  @Transactional
  @Override
  public UserDto.PostSignUpRes createUsers(UserDto.PostSignUpReq postSignUpReq) throws Exception {
    validate(postSignUpReq);
    UserInfo userInfo = CreateUserEntity(postSignUpReq);
    userInfo = userInfo.hashPassword(bCryptPasswordEncoder);
    userInfo.encryptIdValue(aesUtil.encrypt(postSignUpReq.getIdValue()));
    userRepository.save(userInfo);
    return UserDto.PostSignUpRes.builder().build();
  }

  @Override
  public UserDto.PostLoginRes login(UserDto.PostLoginReq postLoginReq) {
    validate(postLoginReq);
    UserInfo userInfo = userRepository.findUserInfoByUserId(postLoginReq.getUserId()).orElseThrow(() -> new MoinException(NON_EXIST_USER));
    if (!userInfo.checkPassword(postLoginReq.getPassword(), bCryptPasswordEncoder)) { // 비밀번호 확인
      throw new MoinException(WRONG_PASSWORD);
    }
    String token = jwtTokenProvider.createAccessToken(
        UserDto.UserBasicInfo.builder()
            .id(userInfo.getId())
            .userId(userInfo.getUserId())
            .name(userInfo.getName())
            .idType(userInfo.getIdType())
            .build()
    );
    return UserDto.PostLoginRes.builder()
        .token(token)
        .build();
  }

  /**
   * 유저 회원 가입 validate
   * request에 대한 값 체크하는 메서드
   * 1. 이메일 값 존재 여부 체크
   * 2. 이메일 정규식 체크
   * 3. 비밀번호 값 존재 여부 체크
   * 4. 비밀번호 정규식 체크
   * 5. 이름 존재 여부 체크
   * 6. 아이디 타입 존재 여부 체크
   * 7. 아이디 타입 값 체크
   * 8. 아이디 값 정규식 체크
   * 9. 이메일 중복 체크
   */
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
    if (userRepository.existsByUserId(postSignUpReq.getUserId())) {
      throw new MoinException(DUPLICATE_EMAIL);
    }
  }

  /**
   * 유저 로그인 validate
   * request에 대한 값 체크하는 메서드
   * 1. 이메일 값 존재 여부 체크
   * 2. 이메일 정규식 체크
   * 3. 비밀번호 값 존재 여부 체크
   * 4. 비밀번호 정규식 체크
   */
  public void validate(UserDto.PostLoginReq postLoginReq) {
    if (postLoginReq.getUserId() == null || postLoginReq.getUserId().isEmpty()) {
      throw new MoinException(NON_EXIST_EMAIL);
    }
    if (!postLoginReq.validateUserId()) {
      throw new MoinException(INVALID_EMAIL);
    }
    if (postLoginReq.getPassword() == null || postLoginReq.getPassword().isEmpty()) {
      throw new MoinException(NON_EXIST_PASSWORD);
    }
    if (!postLoginReq.validatePassword()) {
      throw new MoinException(INVALID_PASSWORD);
    }
  }

  /**
   * 유저 엔티티 인스턴스 생성하는 메서드
   */
  private UserInfo CreateUserEntity(UserDto.PostSignUpReq postSignUpReq) {
    LocalDateTime now = LocalDateTime.now();
    return UserInfo.builder()
        .userId(postSignUpReq.getUserId())
        .name(postSignUpReq.getName())
        .password(postSignUpReq.getPassword())
        .idType(postSignUpReq.getIdType())
        .idValue(postSignUpReq.getIdValue())
        .createdAt(now)
        .updatedAt(now)
        .status(ACTIVE.value())
        .build();
  }
}
