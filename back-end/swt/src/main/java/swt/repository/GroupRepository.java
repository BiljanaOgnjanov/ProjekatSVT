package swt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import swt.model.Group;

@Transactional
public interface GroupRepository extends JpaRepository<Group, Long> {

}
