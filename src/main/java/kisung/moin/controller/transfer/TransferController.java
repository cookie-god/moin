package kisung.moin.controller.transfer;

import kisung.moin.config.SecurityUtil;
import kisung.moin.config.exception.MoinException;
import kisung.moin.dto.TransferDto;
import kisung.moin.entity.UserInfo;
import kisung.moin.service.transfer.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kisung.moin.common.code.ErrorCode.NON_EXIST_USER;

@RestController
@RequestMapping(value = "/transfer", consumes = "application/json", produces = "application/json")
@RequiredArgsConstructor
@Slf4j
public class TransferController {
  private final TransferService transferService;

  @PostMapping(value = "/quote")
  public TransferDto.PostQuoteRes postQuote(@RequestBody TransferDto.PostQuoteReq postQuoteReq) throws Exception {
    UserInfo userInfo = SecurityUtil.getUser().orElseThrow(() -> new MoinException(NON_EXIST_USER));
    return transferService.retrieveQuote(userInfo, postQuoteReq);
  }
}
