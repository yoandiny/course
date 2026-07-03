package mg.yoan.course.repository;

import mg.yoan.course.repository.model.CourseSubscription;
import mg.yoan.course.repository.model.CourseSubscriptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSubscriptionRepository
    extends JpaRepository<CourseSubscription, CourseSubscriptionId> {}
