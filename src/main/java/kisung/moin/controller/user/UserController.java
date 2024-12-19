package kisung.moin.controller.user;

import kisung.moin.common.code.SuccessCode;
import kisung.moin.common.response.BasicResponse;
import kisung.moin.dto.UserDto;
import kisung.moin.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kisung.moin.common.code.SuccessCode.CREATE_SUCCESS;
import static kisung.moin.common.code.SuccessCode.READ_SUCCESS;

@RestController
@RequestMapping(value = "/users", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class UserController {
  private final UserService userService;

  @PostMapping(value = "/signup")
  public BasicResponse<UserDto.PostSignUpRes> postSignUp(@RequestBody UserDto.PostSignUpReq postSignUpReq) throws Exception {
    return BasicResponse.success(userService.createUsers(postSignUpReq), CREATE_SUCCESS);
  }

  @PostMapping(value = "/login")
  public BasicResponse<UserDto.PostLoginRes> postLogin(@RequestBody UserDto.PostLoginReq postLoginReq) throws Exception {
    return BasicResponse.success(userService.login(postLoginReq), READ_SUCCESS);
  }
}
