package s4got10dev.diff.storage.activerecord;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import junit.framework.AssertionFailedError;

/**
 * @author Serhii Homeniuk
 */
@ActiveProfiles("ar")
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace=NONE)
@DataJpaTest
public class ActiveRecordDiffRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DiffRepository diffRepository;


    @Test
    public void whenFindById_thenReturnDiff() {
        Diff testDiff = Diff.builder().id(1L).left("left").right("right").build();
        entityManager.persist(testDiff);
        entityManager.flush();

        Diff found = diffRepository.findById(1L).orElseThrow(AssertionFailedError::new);

        assertThat(testDiff).isEqualTo(found);
    }

}
