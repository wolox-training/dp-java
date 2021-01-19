package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wolox.training.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * This method consults a user by name
     *
     * @param username: is the username
     * @return {@link User}
     */
    Optional<User> findByUsername(String username);
}
