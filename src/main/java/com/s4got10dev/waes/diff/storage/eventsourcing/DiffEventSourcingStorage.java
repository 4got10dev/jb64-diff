package com.s4got10dev.waes.diff.storage.eventsourcing;

import static com.s4got10dev.waes.diff.model.Part.LEFT;
import static com.s4got10dev.waes.diff.model.Part.RIGHT;

import javax.validation.constraints.NotNull;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.s4got10dev.waes.diff.model.AddDiffPartRequest;
import com.s4got10dev.waes.diff.model.AddDiffPartResponse;
import com.s4got10dev.waes.diff.model.DiffComparisonResult;
import com.s4got10dev.waes.diff.storage.DiffStorage;
import com.s4got10dev.waes.diff.util.DiffChecker;

/**
 * @author Serhii Homeniuk
 */
@Service
@Profile("es")
public class DiffEventSourcingStorage implements DiffStorage {

    private DiffPartRepository diffPartRepository;
    private DiffResultRepository diffResultRepository;
    private DiffChecker checker;

    public DiffEventSourcingStorage(DiffPartRepository diffPartRepository, DiffResultRepository diffResultRepository,
            DiffChecker checker) {
        this.diffPartRepository = diffPartRepository;
        this.diffResultRepository = diffResultRepository;
        this.checker = checker;
    }

    @Override
    public DiffComparisonResult getDiffResult(Long id) {
        return diffResultRepository.findFirstByDiffIdOrderByTimestampDesc(id)
                .map(DiffResult::getComparisonResult).orElse(checker.noData());
    }

    @Override
    public AddDiffPartResponse addLeft(@NotNull Long id, @NotNull AddDiffPartRequest request) {
        String leftPart = diffPartRepository.save(DiffPart.instance(id, LEFT, request.getData())).getData();
        String rightPart = diffPartRepository.findFirstByDiffIdAndPartOrderByTimestampDesc(id, RIGHT)
                .map(DiffPart::getData).orElse("");
        saveResult(id, leftPart, rightPart);
        return AddDiffPartResponse.successful().id(id).part(LEFT).build();
    }

    @Override
    public AddDiffPartResponse addRight(@NotNull Long id, @NotNull AddDiffPartRequest request) {
        String rightPart = diffPartRepository.save(DiffPart.instance(id, RIGHT, request.getData())).getData();
        String leftPart = diffPartRepository.findFirstByDiffIdAndPartOrderByTimestampDesc(id, LEFT)
                .map(DiffPart::getData).orElse("");
        saveResult(id, leftPart, rightPart);
        return AddDiffPartResponse.successful().id(id).part(RIGHT).build();
    }

    private void saveResult(Long id, String leftPart, String rightPart) {
        DiffComparisonResult comparisonResult = checker.makeComparisonEncoded(leftPart, rightPart);
        diffResultRepository.save(DiffResult.builder()
                .diffId(id)
                .comparisonResult(comparisonResult)
                .build());
    }

}
