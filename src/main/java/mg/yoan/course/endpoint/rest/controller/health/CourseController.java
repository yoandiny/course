package mg.yoan.course.endpoint.rest.controller.health;

import lombok.AllArgsConstructor;
import mg.yoan.course.model.Course;
import mg.yoan.course.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
@AllArgsConstructor
public class CourseController {

  private final CourseService courseService;

  @PostMapping
  public ResponseEntity<Course> create(@RequestBody Course course) {
    var created = courseService.create(course);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }
}
