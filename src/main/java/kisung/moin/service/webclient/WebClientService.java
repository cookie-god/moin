package kisung.moin.service.webclient;

import kisung.moin.config.exception.MoinException;
import kisung.moin.dto.TransferDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

import static kisung.moin.common.code.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebClientService {
  @Value("${server.upbit-server-url}")
  private String upbitServerUrl;

  private final WebClient webClient;

  public List<TransferDto.UpbitInfo> retrieveUpbitPriceInfo() {
    return webClient
        .get()
        .uri(upbitServerUrl + "/v1/forex/recent")
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.just(new MoinException(WEB_CLIENT_ERROR))) // 에러 체크
        .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.just(new MoinException(WEB_SERVER_ERROR))) // 에러 체크
        .bodyToMono(new ParameterizedTypeReference<List<TransferDto.UpbitInfo>>() {})
        .timeout(Duration.ofSeconds(5)) // 5초 응답 체크
        .doOnError(e -> log.info("error 발생"))
        .onErrorResume(e -> Mono.empty())
        .block();
  }
}
