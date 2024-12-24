package kisung.moin.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kisung.moin.common.response.ErrorResponse;
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
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success"),
      @ApiResponse(responseCode = "USER_ERROR_001", description = "Email is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_004", description = "Email is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_002", description = "Password is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_005", description = "Password is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_003", description = "Nickname is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_006", description = "Id Type is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_007", description = "Id Type is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_008", description = "Id Value is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_009", description = "Reg Id Value is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_010", description = "Business Id Value is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_011", description = "email is already exist",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "SERVER_ERROR_001", description = "Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity<UserDto.PostSignUpRes> postSignUp(@RequestBody UserDto.PostSignUpReq postSignUpReq) throws Exception {
    return ResponseEntity.ok(userService.createUsers(postSignUpReq));
  }

  @Operation(summary = "로그인 API", description = "로그인")
  @PostMapping(value = "/login")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success"),
      @ApiResponse(responseCode = "USER_ERROR_001", description = "Email is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_004", description = "Email is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_002", description = "Password is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_005", description = "Password is invalid",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "USER_ERROR_013", description = "Not exist user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "SERVER_ERROR_001", description = "Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  public ResponseEntity<UserDto.PostLoginRes> postLogin(@RequestBody UserDto.PostLoginReq postLoginReq) {
    return ResponseEntity.ok(userService.login(postLoginReq));
  }
}
