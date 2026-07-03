package mg.yoan.course.endpoint.rest.controller;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import mg.yoan.course.endpoint.event.EventProducer;
import mg.yoan.course.endpoint.event.model.CourseSubscriptionCreated;
import mg.yoan.course.repository.CourseRepository;
import mg.yoan.course.repository.CourseSubscriptionRepository;
import mg.yoan.course.repository.UserRepository;
import mg.yoan.course.repository.model.CourseSubscription;
import mg.yoan.course.repository.model.CourseSubscriptionId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@AllArgsConstructor
public class CourseSubscriptionController {

  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final CourseSubscriptionRepository courseSubscriptionRepository;
  private final EventProducer eventProducer;

  @PostMapping("/users/{userId}/courses/{courseId}/subscriptions")
  public ResponseEntity<Void> subscribe(@PathVariable UUID userId, @PathVariable UUID courseId) {
    if (!userRepository.existsById(userId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found: " + userId);
    }
    if (!courseRepository.existsById(courseId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found: " + courseId);
    }

    var id = new CourseSubscriptionId(userId, courseId);
    if (courseSubscriptionRepository.existsById(id)) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Already subscribed");
    }

    var subscription = new CourseSubscription();
    subscription.setId(id);
    courseSubscriptionRepository.save(subscription);

    eventProducer.accept(
        List.of(CourseSubscriptionCreated.builder().userId(userId).courseId(courseId).build()));

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
