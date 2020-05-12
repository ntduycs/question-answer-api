package vn.ntduycs.javaintern.services;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.ntduycs.javaintern.exceptions.BadRequestException;
import vn.ntduycs.javaintern.exceptions.ResourceNotFoundException;
import vn.ntduycs.javaintern.models.*;
import vn.ntduycs.javaintern.payloads.*;
import vn.ntduycs.javaintern.repositories.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    private final ChoiceRepository choiceRepository;

    private final UserRepository userRepository;

    private final VoteRepository voteRepository;

    private final TagRepository tagRepository;

    private final QuestionTagRepository questionTagRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository,
                               ChoiceRepository choiceRepository,
                               UserRepository userRepository,
                               VoteRepository voteRepository,
                               TagRepository tagRepository,
                               QuestionTagRepository questionTagRepository) {
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.tagRepository = tagRepository;
        this.questionTagRepository = questionTagRepository;
    }

    @Override
    @Transactional
    public Long store(QuestionRequest request) {
        QuestionExpirationRequest expiration = request.getExpiration();

        LocalDateTime expiredAt = LocalDateTime.from(LocalDateTime.now())
                .plusDays(expiration.getDays())
                .plusHours(expiration.getHours());

        Question question = new Question(request.getContent(), expiredAt, request.isOpen());

        for (ChoiceRequest choice : request.getChoices()) {
            question.addChoice(new Choice(choice.getContent()));
        }

        question = questionRepository.save(question);

        // When finishing storing new question, begin to handle storing tags for it

        List<Tag> existingTags = tagRepository.findByNameIn(request.getTags());

        List<Tag> nonExistingTags = request.getTags().stream()
                .map(Tag::new)
                .filter(newTag -> !existingTags.contains(newTag))
                .collect(Collectors.toList());

        existingTags.addAll(tagRepository.saveAll(nonExistingTags));

        question.addTags(existingTags);

        return questionRepository.save(question).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionResponse show(Long id) {
        Question question = questionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + id));

        List<ChoiceResponse> choiceResponses = getChoiceResponsesByQuestionId(id);

        User creator = userRepository.findById(question.getCreatedBy())
                .orElseThrow(() -> new ResourceNotFoundException("Question creator not found with id " + question.getCreatedBy()));

        List<String> tagNames = question.getTags().stream()
                .map(tag -> tag.getTag().getName())
                .collect(Collectors.toList());

        return new QuestionResponse(
                question.getId(),
                question.getContent(),
                choiceResponses,
                user2QuestionCreatorResponse(creator),
                question.getCreatedAt(),
                question.getExpiredAt(),
                isExpiredQuestion(question),
                calculateTotalVotes(choiceResponses),
                tagNames
        );
    }

    private List<ChoiceResponse> getChoiceResponsesByQuestionId(Long questionId) {
        return voteRepository.countByQuestionIdGroupByChoiceId(questionId);
    }

    private boolean isExpiredQuestion(Question question) {
        return LocalDateTime.now().isAfter(question.getExpiredAt());
    }

    private QuestionCreatorResponse user2QuestionCreatorResponse(User user) {
        return new QuestionCreatorResponse(user.getId(), user.getFullName());
    }

    private long calculateTotalVotes(List<ChoiceResponse> choices) {
        return choices.stream().mapToLong(ChoiceResponse::getVoteCount).sum();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getByTagName(Pageable pageable, String tagName) {
        Optional<Tag> tag = tagRepository.findByName(tagName);

        if (tag.isEmpty()) {
            return buildEmptyPaginatedQuestionResponse(pageable);
        }

        QuestionTag questionTag = questionTagRepository.findByTag(tag.get());

        Page<Question> questions = questionRepository.findAllByTagsAndDeletedAtIsNull(questionTag, pageable);

        List<QuestionResponse> questionResponses = buildQuestionResponses(questions);

        return buildPaginatedQuestionResponse(questionResponses, questions, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> index(Pageable pageable) {

        Page<Question> questions = questionRepository.findAllByDeletedAtIsNull(pageable);

        List<QuestionResponse> questionResponses = buildQuestionResponses(questions);

        return buildPaginatedQuestionResponse(questionResponses, questions, pageable);
    }

    private List<QuestionResponse> buildQuestionResponses(Page<Question> questions) {
        Map<Long, QuestionCreatorResponse> questionCreatorResponseMap = getQuestionCreatorResponseMap(questions.getContent());

        Map<Long, List<ChoiceResponse>> choiceResponsesMap = getChoiceResponseMap(questions.getContent());

        List<QuestionResponse> questionResponses = new ArrayList<>();

        for (Question question : questions) {
            List<ChoiceResponse> choiceResponses = choiceResponsesMap.get(question.getId());

            QuestionCreatorResponse questionCreatorResponse = questionCreatorResponseMap.get(question.getId());

            List<String> tagNames = question.getTags().stream()
                    .map(tag -> tag.getTag().getName())
                    .collect(Collectors.toList());

            questionResponses.add(new QuestionResponse(
                            question.getId(),
                            question.getContent(),
                            choiceResponses,
                            questionCreatorResponse,
                            question.getCreatedAt(),
                            question.getExpiredAt(),
                            isExpiredQuestion(question),
                            calculateTotalVotes(choiceResponses),
                            tagNames
                    )
            );
        }

        return questionResponses;
    }

    private Map<String, Object> buildPaginatedQuestionResponse(List<QuestionResponse> items,
                                                               Page<Question> page,
                                                               Pageable pageable) {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("page", pageable.getPageNumber());
        response.put("size", pageable.getPageSize());
        response.put("totalElements", page.getTotalElements());
        response.put("totalPages", page.getTotalPages());
        response.put("first", page.isFirst());
        response.put("last", page.isLast());
        response.put("items", items);

        return response;
    }

    private Map<String, Object> buildEmptyPaginatedQuestionResponse(Pageable pageable) {
        Map<String, Object> response = new LinkedHashMap<>();

        response.put("page", pageable.getPageNumber());
        response.put("size", pageable.getPageSize());
        response.put("totalElements", 0);
        response.put("totalPages", 1);
        response.put("first", true);
        response.put("last", true);
        response.put("items", Collections.emptyList());

        return response;
    }

    private Map<Long /*questionId*/, List<ChoiceResponse>> getChoiceResponseMap(List<Question> questions) {
        Map<Long, List<ChoiceResponse>> ret = new LinkedHashMap<>();

        for (Question question : questions) {
            ret.put(question.getId(), getChoiceResponsesByQuestionId(question.getId()));
        }

        return ret;
    }

    private Map<Long /*questionId*/, QuestionCreatorResponse> getQuestionCreatorResponseMap(List<Question> questions) {
        Map<Long, QuestionCreatorResponse> ret = new LinkedHashMap<>();

        Set<Long> creatorIds = questions.stream()
                .map(Question::getCreatedBy)
                .collect(Collectors.toSet());

        List<User> creators = userRepository.findAllById(creatorIds);

        for (Question question : questions) {
            Optional<User> found = Optional.empty();

            for (User creator : creators) {
                if (creator.getId().equals(question.getCreatedBy())) {
                    found = Optional.of(creator);
                    break;
                }
            }

            assert found.isPresent();

            ret.put(question.getId(), user2QuestionCreatorResponse(found.get()));
        }

        return ret;
    }

    @Override
    public void update(Long id, BaseRequest request) {

    }

    @Override
    public void forceRemove(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with id " + id);
        }

        questionRepository.deleteById(id);
    }

    @Override
    public void softRemove(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + id));

        question.setDeletedAt(LocalDateTime.now());

        questionRepository.save(question);
    }

    @Override
    public void addChoice(Long id, ChoiceRequest choice) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + id));

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!question.isOpenQuestion() && !currentUser.getId().equals(question.getCreatedBy())) {
            throw new BadRequestException("Only question owner can add choice for this question");
        }

        for (Choice c : question.getChoices()) {
            if (c.getContent().equalsIgnoreCase(choice.getContent())) {
                throw new BadRequestException("Choice has been already existing");
            }
        }

        question.addChoice(new Choice(choice.getContent()));

        questionRepository.save(question);
    }

    @Override
    @Transactional
    public void castVote(Long id, VoteRequest vote) {
        Question question = questionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with id " + id));

        if (question.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("This question has been expired");
        }

        Choice choice = choiceRepository.findById(vote.getChoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Choice not found with id " + id));

        User voter = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        Optional<Vote> voteEntity = voteRepository.findByChoiceIdAnVoterId(vote.getChoiceId(), voter.getId());

        if (voteEntity.isPresent()) {
            question.removeVote(voteEntity.get());
        } else {
            question.addVote(new Vote(choice, voter));
        }

        questionRepository.save(question);
    }
}
