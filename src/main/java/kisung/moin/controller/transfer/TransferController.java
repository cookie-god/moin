package kisung.moin.controller.transfer;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kisung.moin.common.response.ErrorResponse;
import kisung.moin.config.SecurityUtil;
import kisung.moin.config.exception.MoinException;
import kisung.moin.controller.transfer.swagger.GetTransferListSwaggerResponse;
import kisung.moin.controller.transfer.swagger.PostTransferQuoteSwaggerResponse;
import kisung.moin.controller.transfer.swagger.PostTransferRequestSwaggerResponse;
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
  @PostTransferQuoteSwaggerResponse
  public TransferDto.PostQuoteRes postQuote(@RequestBody TransferDto.PostQuoteReq postQuoteReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new MoinException(NON_EXIST_USER));
    return transferService.createQuotes(userInfo, postQuoteReq);
  }

  @Operation(summary = "송금 접수 요청 API", description = "송급 접수 요청하기")
  @PostMapping(value = "/request")
  @PostTransferRequestSwaggerResponse
  public TransferDto.PostQuoteRequestRes postQuoteRequest(@RequestBody TransferDto.PostQuoteRequestReq postQuoteRequestReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new MoinException(NON_EXIST_USER));
    return transferService.createQuoteRequests(userInfo, postQuoteRequestReq);
  }

  @Operation(summary = "회원의 거래 이력을 가지고 오는 API", description = "회원의 거래 이력 조회하기")
  @GetMapping(value = "/list")
  @GetTransferListSwaggerResponse
  public TransferDto.GetTransfersRes getTransfers() {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new MoinException(NON_EXIST_USER));
    return transferService.retrieveTransfers(userInfo);
  }
}
