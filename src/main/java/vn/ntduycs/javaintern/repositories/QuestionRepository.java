package vn.ntduycs.javaintern.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.ntduycs.javaintern.models.Question;
import vn.ntduycs.javaintern.models.QuestionTag;

import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Page<Question> findAllByTagsAndDeletedAtIsNull(QuestionTag tag, Pageable pageable);

    Page<Question> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Question> findByIdAndDeletedAtIsNull(Long id);
}
