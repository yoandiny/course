package mg.yoan.course.service;

import lombok.AllArgsConstructor;
import mg.yoan.course.model.Course;
import mg.yoan.course.repository.CourseRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CourseService {

  private final CourseRepository courseRepository;

  public Course create(Course course) {
    return courseRepository.save(course);
  }
}
