package kisung.moin.service.transfer;

import kisung.moin.dto.TransferDto;
import kisung.moin.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {
  @Override
  public TransferDto.PostQuoteRes retrieveQuote(UserInfo userInfo, TransferDto.PostQuoteReq postQuoteReq) {
    return TransferDto.PostQuoteRes.builder()
        .id(userInfo.getId())
        .build();
  }
}
