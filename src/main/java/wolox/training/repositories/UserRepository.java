package wolox.training.repositories;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT ur FROM User ur " +
            "WHERE (:sequence = '' OR LOWER(ur.name) LIKE LOWER(CONCAT('%', :sequence,'%'))) " +
            "AND (cast(:startDate AS date) IS NULL OR ur.birthDate >= :startDate) " +
            "AND (cast(:endDate AS date) IS NULL OR ur.birthDate <= :endDate)" )

    /*@Query("SELECT u FROM User u WHERE (:sequence IS NULL OR lower(u.name) like lower(concat('%', :sequence,'%')))"
            + " and (:startDate IS NULL OR :endDate IS NULL OR u.birthDate BETWEEN :startDate AND :endDate)")*/
    Page<User> findAllByNameIgnoreCaseContainingAndBirthdateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("sequence") String sequence,
            Pageable pageable
    );
}
