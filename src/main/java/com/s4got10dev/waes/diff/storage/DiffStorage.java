package com.s4got10dev.waes.diff.storage;

import com.s4got10dev.waes.diff.model.AddDiffPartRequest;
import com.s4got10dev.waes.diff.model.AddDiffPartResponse;
import com.s4got10dev.waes.diff.model.DiffComparisonResult;

/**
 * @author Serhii Homeniuk
 */
public interface DiffStorage {

    DiffComparisonResult getDiffResult(Long id);

    AddDiffPartResponse addLeft(Long id, AddDiffPartRequest request);

    AddDiffPartResponse addRight(Long id, AddDiffPartRequest request);

}
