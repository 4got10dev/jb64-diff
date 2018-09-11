package s4got10dev.diff.storage.eventsourcing;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import s4got10dev.diff.config.MongoConfig;
import s4got10dev.diff.model.Part;

/**
 * @author Serhii Homeniuk
 */
@ActiveProfiles("es")
@RunWith(SpringRunner.class)
@DataMongoTest
@Import(MongoConfig.class)
public class EventSourcingRepositoryTest {

    @Autowired
    private DiffPartRepository diffPartRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void whenFindNonExistingPart_thenReturnEmptyOptional() {
        assertThat(diffPartRepository.findFirstByDiffIdAndPartOrderByTimestampDesc(1L, Part.RIGHT)).isEmpty();
    }

    @Test
    public void whenAddLeftPartTwoTimes_thenBothAreStored() {
        Query query = new Query()
                .addCriteria(Criteria.where("diffId").is(2L))
                .addCriteria(Criteria.where("part").is(Part.LEFT));
        long records = mongoTemplate.count(query, DiffPart.class);
        diffPartRepository.save(DiffPart.instance(2L, Part.LEFT, "testData"));
        diffPartRepository.save(DiffPart.instance(2L, Part.LEFT, "newTestData"));
        assertThat(mongoTemplate.count(query, DiffPart.class)).isEqualTo(records + 2);
        assertThat(diffPartRepository.findFirstByDiffIdAndPartOrderByTimestampDesc(2L, Part.LEFT))
                .isPresent()
                .hasValueSatisfying(diffPart -> assertThat(diffPart.getData()).isEqualTo("newTestData"));
    }

}
