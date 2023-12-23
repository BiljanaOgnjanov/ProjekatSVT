package swt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import swt.model.Reaction;

@Transactional
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}
