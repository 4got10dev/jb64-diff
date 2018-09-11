package s4got10dev.diff.storage.eventsourcing;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import s4got10dev.diff.model.Part;

/**
 * @author Serhii Homeniuk
 */
public interface DiffPartRepository extends MongoRepository<DiffPart, Long> {

    Optional<DiffPart> findFirstByDiffIdAndPartOrderByTimestampDesc(Long diffId, Part part);

}
