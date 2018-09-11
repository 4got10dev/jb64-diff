package s4got10dev.diff.storage.eventsourcing;

import javax.validation.constraints.NotNull;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import s4got10dev.diff.model.AddDiffPartRequest;
import s4got10dev.diff.model.AddDiffPartResponse;
import s4got10dev.diff.model.DiffComparisonResult;
import s4got10dev.diff.storage.DiffStorage;
import s4got10dev.diff.util.DiffChecker;
import s4got10dev.diff.model.Part;

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
        String leftPart = diffPartRepository.save(DiffPart.instance(id, Part.LEFT, request.getData())).getData();
        String rightPart = diffPartRepository.findFirstByDiffIdAndPartOrderByTimestampDesc(id, Part.RIGHT)
                .map(DiffPart::getData).orElse("");
        saveResult(id, leftPart, rightPart);
        return AddDiffPartResponse.successful().id(id).part(Part.LEFT).build();
    }

    @Override
    public AddDiffPartResponse addRight(@NotNull Long id, @NotNull AddDiffPartRequest request) {
        String rightPart = diffPartRepository.save(DiffPart.instance(id, Part.RIGHT, request.getData())).getData();
        String leftPart = diffPartRepository.findFirstByDiffIdAndPartOrderByTimestampDesc(id, Part.LEFT)
                .map(DiffPart::getData).orElse("");
        saveResult(id, leftPart, rightPart);
        return AddDiffPartResponse.successful().id(id).part(Part.RIGHT).build();
    }

    private void saveResult(Long id, String leftPart, String rightPart) {
        DiffComparisonResult comparisonResult = checker.makeComparisonEncoded(leftPart, rightPart);
        diffResultRepository.save(DiffResult.builder()
                .diffId(id)
                .comparisonResult(comparisonResult)
                .build());
    }

}
