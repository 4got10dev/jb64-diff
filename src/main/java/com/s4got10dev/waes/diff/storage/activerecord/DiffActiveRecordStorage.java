package com.s4got10dev.waes.diff.storage.activerecord;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.s4got10dev.waes.diff.model.AddDiffPartRequest;
import com.s4got10dev.waes.diff.model.AddDiffPartResponse;
import com.s4got10dev.waes.diff.model.DiffComparisonResult;
import com.s4got10dev.waes.diff.model.Part;
import com.s4got10dev.waes.diff.storage.DiffStorage;
import com.s4got10dev.waes.diff.util.DiffChecker;

/**
 * @author Serhii Homeniuk
 */
@Service
@Profile("ar")
public class DiffActiveRecordStorage implements DiffStorage {

    private DiffRepository repository;
    private DiffChecker checker;

    public DiffActiveRecordStorage(DiffRepository repository, DiffChecker checker) {
        this.repository = repository;
        this.checker = checker;
    }

    @Override
    public DiffComparisonResult getDiffResult(Long id) {
        return repository.findById(id).map(diff -> checker.makeComparisonEncoded(diff.getLeft(), diff.getRight()))
                .orElse(checker.noData());
    }

    @Override
    public AddDiffPartResponse addLeft(Long id, AddDiffPartRequest request) {
        return addDiffPart(id, request, Part.LEFT);
    }

    @Override
    public AddDiffPartResponse addRight(Long id, AddDiffPartRequest request) {
        return addDiffPart(id, request, Part.RIGHT);
    }

    private AddDiffPartResponse addDiffPart(Long id, AddDiffPartRequest request, Part part) {
        Diff diff = repository.findById(id).orElseGet(Diff.builder().id(id)::build);

        if (part == Part.LEFT)
            diff.setLeft(request.getData());
        else if (part == Part.RIGHT)
            diff.setRight(request.getData());

        repository.save(diff);

        return AddDiffPartResponse.successful().id(id).part(part).build();
    }
}
