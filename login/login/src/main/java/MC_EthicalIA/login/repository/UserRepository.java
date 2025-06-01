package MC_EthicalIA.login.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import MC_EthicalIA.login.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
