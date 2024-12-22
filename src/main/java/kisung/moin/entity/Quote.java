package kisung.moin.entity;

import jakarta.persistence.*;
import kisung.moin.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "QUOTE")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString
public class Quote extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserInfo userInfo;

  @Column(name = "code")
  private String code;

  @Column(name = "currency_code")
  private String currencyCode;

  @Column(name = "amount")
  private Double amount;

  @Column(name = "fee")
  private Double fee;

  @Column(name = "exchange_rate")
  private Double exchangeRate;

  @Column(name = "target_amount")
  private Double targetAmount;

  @Column(name = "expire_time")
  private LocalDateTime expireTime;
}
