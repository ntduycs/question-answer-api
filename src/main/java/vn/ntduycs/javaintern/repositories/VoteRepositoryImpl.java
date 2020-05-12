package vn.ntduycs.javaintern.repositories;

import org.springframework.stereotype.Repository;
import vn.ntduycs.javaintern.models.Vote;
import vn.ntduycs.javaintern.payloads.ChoiceResponse;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class VoteRepositoryImpl implements VoteRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ChoiceResponse> countByQuestionIdGroupByChoiceId(Long questionId) {
        TypedQuery<ChoiceResponse> query = em.createQuery(
                "select new vn.ntduycs.javaintern.payloads.ChoiceResponse(c.id, c.content, count(v.id)) " +
                        "from Choice c left outer join Vote v on c.id = v.choice.id " +
                        "where c.question.id = :questionId " +
                        "group by c.id", ChoiceResponse.class);

        query.setParameter("questionId", questionId);

        return query.getResultList();
    }

    @Override
    public boolean existByChoiceIdAndVoterId(Long choiceId, Long voterId) {
        TypedQuery<Boolean> query = em.createQuery(
                "select case when count(v) > 0 " +
                        "then true " +
                        "else false " +
                        "end " +
                        "from Vote v " +
                        "where v.choice.id = :choiceId and v.voter.id = :voterId"
                , Boolean.class);

        query.setParameter("choiceId", choiceId);
        query.setParameter("voterId", voterId);

        return query.getSingleResult();
    }

    @Override
    public Optional<Vote> findByChoiceIdAnVoterId(Long choiceId, Long voterId) {
        TypedQuery<Vote> query = em.createQuery(
                "select v " +
                        "from Vote v " +
                        "where v.choice.id = :choiceId and v.voter.id = :voterId"
                , Vote.class);

        query.setParameter("choiceId", choiceId);
        query.setParameter("voterId", voterId);

       try {
           return Optional.of(query.getSingleResult());
       } catch (NoResultException e) {
           return Optional.empty();
       }
    }
}
