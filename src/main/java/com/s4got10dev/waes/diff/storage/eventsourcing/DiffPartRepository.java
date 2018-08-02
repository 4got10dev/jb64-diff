package com.s4got10dev.waes.diff.storage.eventsourcing;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.s4got10dev.waes.diff.model.Part;

/**
 * @author Serhii Homeniuk
 */
public interface DiffPartRepository extends MongoRepository<DiffPart, Long> {

    Optional<DiffPart> findFirstByDiffIdAndPartOrderByTimestampDesc(Long diffId, Part part);

}
