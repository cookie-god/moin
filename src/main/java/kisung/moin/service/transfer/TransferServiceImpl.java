package kisung.moin.service.transfer;

import kisung.moin.dto.TransferDto;
import kisung.moin.entity.UserInfo;
import kisung.moin.service.webclient.WebClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransferServiceImpl implements TransferService {
  private final WebClientService webClientService;

  @Override
  public TransferDto.PostQuoteRes retrieveQuote(UserInfo userInfo, TransferDto.PostQuoteReq postQuoteReq) {
    List<TransferDto.UpbitInfo> upbitInfos = webClientService.retrieveUpbitPriceInfo();
    return TransferDto.PostQuoteRes.builder()
        .upbitInfos(upbitInfos)
        .build();
  }
}
