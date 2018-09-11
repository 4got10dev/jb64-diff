package s4got10dev.diff.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Base64;

import org.junit.Before;
import org.junit.Test;

import s4got10dev.diff.model.DiffComparisonResult;
import s4got10dev.diff.model.Offset;
import s4got10dev.diff.model.Result;

/**
 * @author Serhii Homeniuk
 */
public class DiffCheckerTest {

    private DiffChecker checker;
    private String data;
    private String sameData;
    private String differentSizeData;
    private String sameSizeDifferentContentData;

    @Before
    public void setUp() {
        checker = new DiffChecker();
        data = Base64.getEncoder().encodeToString("SomeTestString12345".getBytes());
        sameData = Base64.getEncoder().encodeToString("SomeTestString12345".getBytes());
        differentSizeData = Base64.getEncoder().encodeToString("SomeTestString123456".getBytes());
        sameSizeDifferentContentData = Base64.getEncoder().encodeToString("EmosTestGnirts12354".getBytes());
    }

    private void doComparisonEncoded(String left, String right, Result expectedResult, Offset... expectedOffsets) {
        DiffComparisonResult result = checker.makeComparisonEncoded(left, right);
                assertThat(result).isNotNull();
                assertThat(result.getResult()).isEqualTo(expectedResult);
                if (expectedOffsets.length > 0) {
                    assertThat(result.getOffsets())
                            .hasSameSizeAs(expectedOffsets)
                            .containsExactlyInAnyOrder(expectedOffsets);
                } else {
                    assertThat(result.getOffsets()).isNullOrEmpty();
                }
    }

    @Test
    public void testLeftPartIsNull() {
        doComparisonEncoded(null, data, Result.LEFT_SIDE_NOT_PROVIDED);
    }

    @Test
    public void testRightPartIsNull() {
        doComparisonEncoded(data, null, Result.RIGHT_SIDE_NOT_PROVIDED);
    }

    @Test
    public void testDataIsTheSame() {
        doComparisonEncoded(data, sameData, Result.EQUAL);
    }

    @Test
    public void testDataDifferentSize() {
        doComparisonEncoded(data, differentSizeData, Result.NOT_EQUAL_SIZE);
    }

    @Test
    public void testDataSameSizeDifferentContent() {
        doComparisonEncoded(data, sameSizeDifferentContentData, Result.DIFFERENT,
                Offset.builder().start(0).length(4).build(),
                Offset.builder().start(8).length(6).build(),
                Offset.builder().start(17).length(2).build()
                );
    }
}
