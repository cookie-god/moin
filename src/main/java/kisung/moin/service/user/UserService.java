package kisung.moin.service.user;

import kisung.moin.dto.UserDto;

public interface UserService {
  UserDto.PostSignUpRes createUsers(UserDto.PostSignUpReq postSignUpReq) throws Exception;
  UserDto.PostLoginRes login(UserDto.PostLoginReq postLoginReq);
}
