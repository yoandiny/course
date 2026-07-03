package mg.yoan.course.repository;

import java.util.UUID;
import mg.yoan.course.PojaGenerated;
import mg.yoan.course.repository.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@PojaGenerated
@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {}
