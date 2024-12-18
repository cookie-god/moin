package kisung.moin.repository.user;

import kisung.moin.entity.UserInfo;
import kisung.moin.repository.user.custom.CustomUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long>, CustomUserRepository {
}
