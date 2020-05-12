package vn.ntduycs.javaintern.repositories;

import vn.ntduycs.javaintern.models.Vote;
import vn.ntduycs.javaintern.payloads.ChoiceResponse;

import java.util.List;
import java.util.Optional;

public interface VoteRepository {

    List<ChoiceResponse> countByQuestionIdGroupByChoiceId(Long questionId);

    boolean existByChoiceIdAndVoterId(Long choiceId, Long voterId);

    Optional<Vote> findByChoiceIdAnVoterId(Long choiceId, Long voterId);

}
