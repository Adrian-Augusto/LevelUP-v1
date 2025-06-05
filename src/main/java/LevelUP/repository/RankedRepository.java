package LevelUP.repository;

import LevelUP.entity.Ranked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface RankedRepository extends JpaRepository<Ranked, Long> {

    Ranked findRankedById(long id);
    Ranked deleteById(long id);

    List<Ranked> id(Long id);
}
