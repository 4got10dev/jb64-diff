package com.s4got10dev.waes.diff.storage.eventsourcing;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Serhii Homeniuk
 */
public interface DiffResultRepository extends MongoRepository<DiffResult, Long> {

    Optional<DiffResult> findFirstByDiffIdOrderByTimestampDesc(Long diffId);

}
