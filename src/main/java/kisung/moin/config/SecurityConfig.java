package kisung.moin.config;

import kisung.moin.config.filter.JwtAuthenticationFilter;
import kisung.moin.config.filter.LoggingFilter;
import kisung.moin.config.jwt.JwtTokenProvider;
import kisung.moin.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthService authService;

  /**
   * PasswordEncoder를 Bean으로 등록
   */
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring().requestMatchers("/swagger-ui/**", "/v3/api-docs/**");  // /public 경로는 시큐리티 필터 체인에서 제외
  }

  /**
   * 필터 설정
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(authz -> authz
            .requestMatchers("/users/**").permitAll()  // 모든 요청을 허용
            .requestMatchers("/swagger", "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**", "/v3/api-docs/**").permitAll()  // 모든 요청을 허용
            .requestMatchers("/h2/**").permitAll()  // 모든 요청을 허용
            .anyRequest().permitAll()
        )
        .addFilterBefore(new LoggingFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, authService), LoggingFilter.class)
    ;  // CSRF 비활성화


    return http.build();
  }
}
