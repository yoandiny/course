package mg.yoan.course.service.event;

import jakarta.mail.internet.InternetAddress;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mg.yoan.course.endpoint.event.model.CourseSubscriptionCreated;
import mg.yoan.course.mail.Email;
import mg.yoan.course.mail.Mailer;
import mg.yoan.course.repository.CourseRepository;
import mg.yoan.course.repository.UserRepository;
import mg.yoan.course.repository.model.Course;
import mg.yoan.course.repository.model.User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CourseSubscriptionCreatedService implements Consumer<CourseSubscriptionCreated> {

  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final Mailer mailer;

  @Override
  public void accept(CourseSubscriptionCreated event) {
    var user =
        userRepository
            .findById(event.getUserId())
            .orElseThrow(() -> new IllegalStateException("User not found: " + event.getUserId()));
    var course =
        courseRepository
            .findById(event.getCourseId())
            .orElseThrow(
                () -> new IllegalStateException("Course not found: " + event.getCourseId()));

    mailer.accept(toConfirmationEmail(user, course));
    log.info("Confirmation email sent to {} for course {}", user.getEmail(), course.getName());
  }

  @SneakyThrows
  private Email toConfirmationEmail(User user, Course course) {
    var to = new InternetAddress(user.getEmail());
    var subject = "Confirmation d'inscription - " + course.getName();
    var body =
        "<p>Bonjour "
            + user.getFirstName()
            + ",</p>"
            + "<p>Votre inscription au cours <b>"
            + course.getName()
            + "</b> a bien été enregistrée.</p>"
            + "<p>À bientôt !</p>";
    return new Email(to, List.of(), List.of(), subject, body, List.of());
  }
}
