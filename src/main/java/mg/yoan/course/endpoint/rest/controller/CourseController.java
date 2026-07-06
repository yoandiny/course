package mg.yoan.course.endpoint.rest.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import mg.yoan.course.repository.model.Course;
import mg.yoan.course.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping
  public ResponseEntity<List<Course>> getAllCourses() {
    return ResponseEntity.ok(courseService.getAll());
  }
}
