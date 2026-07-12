package mg.yoan.course.service.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URI;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import mg.yoan.course.endpoint.event.model.CourseSubscriptionCreated;
import mg.yoan.course.file.bucket.BucketComponent;
import mg.yoan.course.file.pdf.PdfGeneratorService;
import mg.yoan.course.mail.Email;
import mg.yoan.course.mail.Mailer;
import mg.yoan.course.repository.CourseRepository;
import mg.yoan.course.repository.UserRepository;
import mg.yoan.course.repository.model.Course;
import mg.yoan.course.repository.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CourseSubscriptionCreatedServiceTest {

  private UserRepository userRepository;
  private CourseRepository courseRepository;
  private Mailer mailer;
  private BucketComponent bucketComponent;
  private PdfGeneratorService pdfGeneratorService;
  private CourseSubscriptionCreatedService service;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    courseRepository = mock(CourseRepository.class);
    mailer = mock(Mailer.class);
    bucketComponent = mock(BucketComponent.class);
    pdfGeneratorService = mock(PdfGeneratorService.class);
    service =
        new CourseSubscriptionCreatedService(
            userRepository, courseRepository, mailer, bucketComponent, pdfGeneratorService);
  }

  @Test
  void testAccept_generatesPdfUploadsAndSendsMail() throws Exception {
    UUID userId = UUID.randomUUID();
    UUID courseId = UUID.randomUUID();
    CourseSubscriptionCreated event = new CourseSubscriptionCreated();
    event.setUserId(userId);
    event.setCourseId(courseId);

    User user =
        User.builder()
            .id(userId)
            .firstName("John")
            .lastName("Doe")
            .email("john.doe@example.com")
            .build();
    Course course = Course.builder().id(courseId).name("Java Programming").build();

    File tempPdf = File.createTempFile("test", ".pdf");

    var bucketKey = "tickets/" + courseId + "/" + userId + "/ticket.pdf";
    var ticketUrl =
        URI.create("https://dummy-bucket.s3.eu-west-3.amazonaws.com/" + bucketKey).toURL();

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
    when(pdfGeneratorService.generatePdf(anyString(), anyString())).thenReturn(tempPdf);
    when(bucketComponent.presign(eq(bucketKey), any(Duration.class))).thenReturn(ticketUrl);

    service.accept(event);

    verify(pdfGeneratorService).generatePdf(anyString(), eq("ticket-" + courseId));
    verify(bucketComponent).upload(tempPdf, bucketKey);
    verify(bucketComponent).presign(bucketKey, Duration.ofDays(7));

    ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
    verify(mailer).accept(emailCaptor.capture());
    Email email = emailCaptor.getValue();
    assertTrue(email.htmlBody().contains(ticketUrl.toString()));
    assertEquals(0, email.attachments().size());
  }
}
