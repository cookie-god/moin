package kisung.moin.controller.user;

import kisung.moin.common.response.BasicResponse;
import kisung.moin.dto.UserDto;
import kisung.moin.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<BasicResponse> postSignUp(@RequestBody UserDto.PostSignUpReq postSignUpReq) throws Exception {
    BasicResponse response = BasicResponse.success(CREATE_SUCCESS);
    userService.createUsers(postSignUpReq);
    return ResponseEntity.ok(response);
  }

  @PostMapping(value = "/login")
  public ResponseEntity<BasicResponse> postLogin(@RequestBody UserDto.PostLoginReq postLoginReq) {
    BasicResponse response = BasicResponse.success(READ_SUCCESS);
    response.addField("token", userService.login(postLoginReq).getToken());
    return ResponseEntity.ok(response);
  }
}
