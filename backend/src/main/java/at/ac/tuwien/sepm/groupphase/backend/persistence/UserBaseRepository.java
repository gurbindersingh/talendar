package at.ac.tuwien.sepm.groupphase.backend.persistence;

import at.ac.tuwien.sepm.groupphase.backend.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface UserBaseRepository<E extends User> extends JpaRepository<E, Long> {
    Optional<E> findByIdAndDeletedFalse(Long id);

    List<E> findAll();

    E findByEmail(String email);
}
