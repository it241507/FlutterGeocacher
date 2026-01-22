package at.ac.fhstp.awp_bad.groupxx.repository;

import at.ac.fhstp.awp_bad.groupxx.entities.Cache;
import at.ac.fhstp.awp_bad.groupxx.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CacheRepository extends CrudRepository<Cache, Long> {

@Query("SELECT c FROM Cache c WHERE " +
        "c.coordinate.lat <= :north AND "
        + "c.coordinate.lng <= :east AND "
        + "c.coordinate.lat >= :south AND "
        + "c.coordinate.lng >= :west ")
List<Cache> getCachesInArea(@Param("north") Double north,
                             @Param("east") Double east,
                             @Param("south") Double south,
                             @Param("west") Double west);

List<Cache> findByUser(User user);
}
