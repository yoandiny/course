package mg.yoan.course.service.event;

import jakarta.mail.internet.InternetAddress;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import mg.yoan.course.endpoint.event.model.CourseSubscriptionCreated;
import mg.yoan.course.file.bucket.BucketComponent;
import mg.yoan.course.file.pdf.PdfGeneratorService;
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

  private static final Duration TICKET_URL_EXPIRATION = Duration.ofDays(7);

  private final UserRepository userRepository;
  private final CourseRepository courseRepository;
  private final Mailer mailer;
  private final BucketComponent bucketComponent;
  private final PdfGeneratorService pdfGeneratorService;

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

    String ticketHtml =
        "<html><body><h1>Ticket d'inscription</h1><p>Participant : "
            + user.getFirstName()
            + " "
            + user.getLastName()
            + "</p><p>Cours : "
            + course.getName()
            + "</p></body></html>";
    File ticketPdf = pdfGeneratorService.generatePdf(ticketHtml, "ticket-" + course.getId());

    String bucketKey = "tickets/" + course.getId() + "/" + user.getId() + "/ticket.pdf";
    bucketComponent.upload(ticketPdf, bucketKey);
    URL ticketUrl = bucketComponent.presign(bucketKey, TICKET_URL_EXPIRATION);

    mailer.accept(toConfirmationEmail(user, course, ticketUrl));
    log.info("Confirmation email sent to {} for course {}", user.getEmail(), course.getName());
  }

  @SneakyThrows
  private Email toConfirmationEmail(User user, Course course, URL ticketUrl) {
    var to = new InternetAddress(user.getEmail());
    var subject = "Confirmation d'inscription - " + course.getName();
    var body =
        "<p>Bonjour "
            + user.getFirstName()
            + ",</p>"
            + "<p>Votre inscription au cours <b>"
            + course.getName()
            + "</b> a bien été enregistrée.</p>"
            + "<p>Votre ticket est disponible à l'adresse suivante : "
            + "<a href=\""
            + ticketUrl
            + "\">"
            + ticketUrl
            + "</a></p>"
            + "<p>À bientôt !</p>";
    return new Email(to, List.of(), List.of(), subject, body, List.of());
  }
}
