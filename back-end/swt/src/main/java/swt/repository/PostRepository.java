package swt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import swt.model.Post;
import swt.model.User;

import java.util.List;

@Transactional
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUser(User user);
    @Query("SELECT p FROM Post p WHERE p NOT IN (SELECT gp.posts FROM Group gp WHERE gp.posts IS NOT EMPTY)")
    List<Post> findPosts();
}
