package kisung.moin.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kisung.moin.common.code.ErrorCode;
import kisung.moin.common.response.ErrorResponse;
import kisung.moin.config.exception.MoinException;
import kisung.moin.config.jwt.JwtTokenProvider;
import kisung.moin.entity.UserInfo;
import kisung.moin.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthService authService;
  private static final String HEADER_AUTHORIZATION = "Authorization";
  private static final String TOKEN_PREFIX = "Bearer ";
  // 토큰 검증이 필요한 URL 패턴 목록
  private static final List<String> SECURE_URLS = List.of(
      "/transfer"
  );


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String token = request.getHeader(HEADER_AUTHORIZATION);
      if (requiresToken(request.getRequestURI())) {
        if (token != null && token.startsWith(TOKEN_PREFIX)) {
          token = token.substring(TOKEN_PREFIX.length()); // bearer 토큰 만큼 문자열 자름
          jwtTokenProvider.validateToken(token); // 토큰 유효성 체크 -> AccessDeniedException 나올 수 있음
          UserInfo userInfo = authService.retrieveUserInfoById(jwtTokenProvider.getId(token));
          if (userInfo == null) { // 존재하지 않는 유저 체크
            throw new MoinException(ErrorCode.NON_EXIST_USER);
          }
          userInfo.setInitPassword();
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userInfo, null, null); // authentication 생성
          SecurityContextHolder.getContext().setAuthentication(authentication); // authetication 셋팅
        } else {
          throw new AccessDeniedException("access denied");
        }
      }
      filterChain.doFilter(request, response);
    } catch (MoinException e) {
      setErrorResponse(response, ErrorCode.NON_EXIST_USER);
    } catch (AccessDeniedException | NullPointerException e) {
      setErrorResponse(response, ErrorCode.INVALID_TOKEN);
    }
  }

  private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    ErrorResponse errorResponse = ErrorResponse.of(errorCode);
    ObjectMapper objectMapper = new ObjectMapper();
    response.setContentType("application/json"); // Content-Type 설정
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  // URI가 SECURE_URLS 목록에 포함되어 있는지 확인
  private boolean requiresToken(String uri) {
    return SECURE_URLS.stream().anyMatch(uri::startsWith);
  }
}
