package mg.yoan.course.repository;

import java.util.List;
import mg.yoan.course.PojaGenerated;
import mg.yoan.course.repository.model.Dummy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@PojaGenerated
@Repository
public interface DummyRepository extends JpaRepository<Dummy, String> {

  @Override
  List<Dummy> findAll();
}
