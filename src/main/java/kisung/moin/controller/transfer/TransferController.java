package kisung.moin.controller.transfer;

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

  @PostMapping(value = "/quote")
  public TransferDto.PostQuoteRes postQuote(@RequestBody TransferDto.PostQuoteReq postQuoteReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new MoinException(NON_EXIST_USER));
    return transferService.createQuotes(userInfo, postQuoteReq);
  }

  @PostMapping(value = "/request")
  public TransferDto.PostQuoteRequestRes postQuoteRequest(@RequestBody TransferDto.PostQuoteRequestReq postQuoteRequestReq) {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new MoinException(NON_EXIST_USER));
    return transferService.createQuoteRequests(userInfo, postQuoteRequestReq);
  }

  @GetMapping(value = "/list")
  public TransferDto.GetTransfersRes getTransfers() {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new MoinException(NON_EXIST_USER));
    return transferService.retrieveTransfers(userInfo);
  }
}
