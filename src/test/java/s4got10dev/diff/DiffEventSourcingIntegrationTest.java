package s4got10dev.diff;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Serhii Homeniuk
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("es")
@SpringBootTest(classes = DiffApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class DiffEventSourcingIntegrationTest  extends DiffIntegrationTest {

}
