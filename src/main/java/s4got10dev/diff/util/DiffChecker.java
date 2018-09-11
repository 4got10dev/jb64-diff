package s4got10dev.diff.util;

import static org.springframework.util.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import s4got10dev.diff.model.DiffComparisonResult;
import s4got10dev.diff.model.Offset;
import s4got10dev.diff.model.Result;

/**
 * Utility class that encode and compare two Base64 encoded data
 * and prepare {@link DiffComparisonResult} that contain result and list of offsets in case they are found
 *
 * @author Serhii Homeniuk
 */
@Service
public class DiffChecker {

    public DiffComparisonResult noData() {
        return DiffComparisonResult.builder().result(Result.NO_DATA_PROVIDED).build();
    }

    public DiffComparisonResult makeComparisonEncoded(String left, String right) {
        if (isEmpty(left))
            if (isEmpty(right))
                return noData();
            else
                return DiffComparisonResult.builder().result(Result.LEFT_SIDE_NOT_PROVIDED).build();
        if (isEmpty(right))
            return DiffComparisonResult.builder().result(Result.RIGHT_SIDE_NOT_PROVIDED).build();

        byte[] leftBytes = Base64.getDecoder().decode(left);
        byte[] rightBytes = Base64.getDecoder().decode(right);

        List<Offset> offsetList = new ArrayList<>();

        int offset = -1;
        int length = 0;
        if (leftBytes.length == rightBytes.length) {
            for (int i = 0; i < leftBytes.length; i++) {
                //if bytes are equal, but offset was bigger than -1 it means that we achieved end of difference
                if (leftBytes[i] == rightBytes[i] && offset > -1) {
                    offsetList.add(Offset.builder().start(offset).length(length).build());
                    offset = -1;
                    length = 0;
                }

                //if bytes are not equal, it means that we are inside difference
                if (leftBytes[i] != rightBytes[i]) {
                    offset = offset == -1 ? i : offset;
                    length++;
                }
            }
            //if after iterating through content we are having offset bigger than -1 it means that we end of content is different
            if (offset != -1) {
                offsetList.add(Offset.builder().start(offset).length(length).build());
            }

            if (offsetList.isEmpty())
                return DiffComparisonResult.builder().result(Result.EQUAL).build();
            else
                return DiffComparisonResult.builder().result(Result.DIFFERENT).offsets(offsetList).build();
        } else {
            return DiffComparisonResult.builder().result(Result.NOT_EQUAL_SIZE).build();
        }

    }
}
