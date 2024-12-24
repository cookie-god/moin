package kisung.moin.controller.transfer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kisung.moin.common.response.ErrorResponse;
import kisung.moin.config.SecurityUtil;
import kisung.moin.config.exception.MoinException;
import kisung.moin.dto.TransferDto;
import kisung.moin.entity.UserInfo;
import kisung.moin.service.transfer.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static kisung.moin.common.code.ErrorCode.NON_EXIST_USER;

@RestController
@RequestMapping(value = "/transfer", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class TransferController {
  private final TransferService transferService;

  @Operation(summary = "송금 견적서를 갖고 오는 API", description = "송금 견적서를 가져오기")
  @PostMapping(value = "/quote")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "USER_ERROR_013", description = "Not exist user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "QUOTE_ERROR_001", description = "Amount is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "QUOTE_ERROR_003", description = "Amount is negative number",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "QUOTE_ERROR_002", description = "Amount is over 10000 won",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "QUOTE_ERROR_004", description = "Target Currency is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "QUOTE_ERROR_005", description = "Target Currency is only JPY, USD",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "CALL_ERROR_001", description = "Client error in Other Server",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "CALL_ERROR_002", description = "Error in Other Server",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "SERVER_ERROR_001", description = "Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  public TransferDto.PostQuoteRes postQuote(@RequestBody TransferDto.PostQuoteReq postQuoteReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new MoinException(NON_EXIST_USER));
    return transferService.createQuotes(userInfo, postQuoteReq);
  }

  @Operation(summary = "송금 접수 요청 API", description = "송급 접수 요청하기")
  @PostMapping(value = "/request")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "USER_ERROR_013", description = "Not exist user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "QUOTE_ERROR_006", description = "Quote id is empty",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "QUOTE_ERROR_007", description = "Not exist quote",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "QUOTE_ERROR_008", description = "Quote's expire time is over 10 minutes",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "QUOTE_ERROR_010", description = "Already exist transfer",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "CALL_ERROR_001", description = "Client error in Other Server",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "CALL_ERROR_002", description = "Error in Other Server",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "QUOTE_ERROR_009", description = "Daily limit is over",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "SERVER_ERROR_001", description = "Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  public TransferDto.PostQuoteRequestRes postQuoteRequest(@RequestBody TransferDto.PostQuoteRequestReq postQuoteRequestReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new MoinException(NON_EXIST_USER));
    return transferService.createQuoteRequests(userInfo, postQuoteRequestReq);
  }

  @Operation(summary = "회원의 거래 이력을 가지고 오는 API", description = "회원의 거래 이력 조회하기")
  @GetMapping(value = "/list")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "OK"),
      @ApiResponse(responseCode = "USER_ERROR_013", description = "Not exist user",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "SERVER_ERROR_001", description = "Server Error",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  public TransferDto.GetTransfersRes getTransfers() {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new MoinException(NON_EXIST_USER));
    return transferService.retrieveTransfers(userInfo);
  }
}
