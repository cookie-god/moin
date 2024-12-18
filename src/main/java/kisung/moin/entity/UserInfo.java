package kisung.moin.entity;

import jakarta.persistence.*;
import kisung.moin.entity.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "USER_INFO")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@ToString
public class UserInfo extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long userId;

  @Column(name = "password")
  private String password;

  @Column(name = "name")
  private String name;

  @Column(name = "id_type")
  private String idType;

  @Column(name = "id_value")
  private String idValue;

  public void setInitPassword() {
    this.password = null;
  }

  // 비밀번호 암호화
  public UserInfo hashPassword(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(this.password);
    return this;
  }

  // 비밀번호 암호화
  public void changePassword(PasswordEncoder passwordEncoder, String newPassword) {
    this.password = passwordEncoder.encode(newPassword);
  }

  // 비밀번호 확인
  public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder) {
    return passwordEncoder.matches(plainPassword, this.password);
  }
}
