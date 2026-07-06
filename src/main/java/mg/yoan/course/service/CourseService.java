package mg.yoan.course.service;

import lombok.AllArgsConstructor;
import mg.yoan.course.repository.CourseRepository;
import mg.yoan.course.repository.model.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;

    public Course create(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> getAll(){
        return courseRepository.findAll();
    }
}