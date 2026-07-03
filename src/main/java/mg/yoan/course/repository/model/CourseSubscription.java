package mg.yoan.course.repository.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "courses_subscription")
@Getter
@Setter
public class CourseSubscription {
  @EmbeddedId private CourseSubscriptionId id;
}
