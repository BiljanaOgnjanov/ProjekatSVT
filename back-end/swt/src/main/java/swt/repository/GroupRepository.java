package swt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import swt.model.Group;

import java.util.Optional;

@Transactional
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByIdAndIsSuspendedIs(Long id, Boolean suspended);
}
