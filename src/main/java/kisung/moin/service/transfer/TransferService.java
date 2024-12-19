package kisung.moin.service.transfer;

import kisung.moin.dto.TransferDto;
import kisung.moin.entity.UserInfo;

public interface TransferService {
  TransferDto.PostQuoteRes retrieveQuote(UserInfo userInfo, TransferDto.PostQuoteReq postQuoteReq);
}
