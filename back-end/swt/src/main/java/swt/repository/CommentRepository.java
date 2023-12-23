package swt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import swt.model.Comment;

@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
