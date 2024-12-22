package kisung.moin.controller.user;

import kisung.moin.dto.UserDto;
import kisung.moin.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class UserController {
  private final UserService userService;

  @PostMapping(value = "/signup")
  public ResponseEntity<UserDto.PostSignUpRes> postSignUp(@RequestBody UserDto.PostSignUpReq postSignUpReq) throws Exception {
    return ResponseEntity.ok(userService.createUsers(postSignUpReq));
  }

  @PostMapping(value = "/login")
  public ResponseEntity<UserDto.PostLoginRes> postLogin(@RequestBody UserDto.PostLoginReq postLoginReq) {
    return ResponseEntity.ok(userService.login(postLoginReq));
  }
}
