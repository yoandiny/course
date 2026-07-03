package mg.yoan.course.repository.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.*;
import mg.yoan.course.PojaGenerated;

@PojaGenerated
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
@Getter
@Setter
public class Course {
  @Id private UUID id;
  private String name;
  private Instant startDate;
  private Instant endDate;
}
