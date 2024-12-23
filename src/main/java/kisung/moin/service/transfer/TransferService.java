package kisung.moin.service.transfer;

import kisung.moin.dto.TransferDto;
import kisung.moin.entity.UserInfo;

public interface TransferService {
  TransferDto.PostQuoteRes createQuotes(UserInfo userInfo, TransferDto.PostQuoteReq postQuoteReq);
  TransferDto.PostQuoteRequestRes createQuoteRequests(UserInfo userInfo, TransferDto.PostQuoteRequestReq postQuoteRequestReq);
}
