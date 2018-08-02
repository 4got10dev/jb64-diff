package com.s4got10dev.waes.diff.storage.eventsourcing;

import static java.time.Instant.now;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.s4got10dev.waes.diff.model.Part;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Serhii Homeniuk
 */
@Document
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class DiffPart {

    @Id
    private ObjectId id;
    private Long diffId;
    private Part part;
    private String data;
    private long timestamp;

    static DiffPart instance(Long id, Part part, String data) {
        return new DiffPart(null, id, part, data, now().toEpochMilli());
    }


}
