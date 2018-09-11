package s4got10dev.diff.storage.eventsourcing;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import s4got10dev.diff.model.DiffComparisonResult;

import lombok.Builder;
import lombok.Getter;

/**
 * @author Serhii Homeniuk
 */
@Getter
@Document
class DiffResult {

    @Id
    private ObjectId objectId;
    private Long diffId;
    private DiffComparisonResult comparisonResult;
    private long timestamp;

    @Builder
    DiffResult(Long diffId, DiffComparisonResult comparisonResult) {
        this.comparisonResult = comparisonResult;
        this.diffId = diffId;
        this.timestamp = Instant.now().toEpochMilli();
    }

}
