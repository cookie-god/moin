package kisung.moin.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kisung.moin.common.response.ErrorResponse;
import kisung.moin.controller.user.swagger.PostUserLoginSwaggerResponse;
import kisung.moin.controller.user.swagger.PostUserSignUpSwaggerResponse;
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

  @Operation(summary = "회원 가입 API", description = "회원 가입")
  @PostMapping(value = "/signup")
  @PostUserSignUpSwaggerResponse
  public ResponseEntity<UserDto.PostSignUpRes> postSignUp(@RequestBody UserDto.PostSignUpReq postSignUpReq) throws Exception {
    return ResponseEntity.ok(userService.createUsers(postSignUpReq));
  }

  @Operation(summary = "로그인 API", description = "로그인")
  @PostMapping(value = "/login")
  @PostUserLoginSwaggerResponse
  public ResponseEntity<UserDto.PostLoginRes> postLogin(@RequestBody UserDto.PostLoginReq postLoginReq) {
    return ResponseEntity.ok(userService.login(postLoginReq));
  }
}
