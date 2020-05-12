package vn.ntduycs.javaintern.services;

import org.springframework.data.domain.Pageable;
import vn.ntduycs.javaintern.payloads.*;

import java.util.Map;

public interface QuestionService {

    Long store(QuestionRequest request);

    QuestionResponse show(Long id);

    Map<String, Object> index(Pageable pageable);

    Map<String, Object> getByTagName(Pageable pageable, String tag);

    void update(Long id, BaseRequest request);

    void forceRemove(Long id);

    void softRemove(Long id);

    void addChoice(Long questionId, ChoiceRequest choiceRequest);

    /**
     * Up-vote if the user hasn't voted for given choice and down-vote in otherwise
     *
     * @param questionId
     * @param vote
     */
    void castVote(Long questionId, VoteRequest vote);
}
