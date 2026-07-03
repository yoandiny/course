package mg.yoan.course.repository;

import java.util.UUID;
import mg.yoan.course.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {}
