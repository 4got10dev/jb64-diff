package s4got10dev.diff.storage;

import s4got10dev.diff.model.AddDiffPartRequest;
import s4got10dev.diff.model.AddDiffPartResponse;
import s4got10dev.diff.model.DiffComparisonResult;

/**
 * @author Serhii Homeniuk
 */
public interface DiffStorage {

    DiffComparisonResult getDiffResult(Long id);

    AddDiffPartResponse addLeft(Long id, AddDiffPartRequest request);

    AddDiffPartResponse addRight(Long id, AddDiffPartRequest request);

}
