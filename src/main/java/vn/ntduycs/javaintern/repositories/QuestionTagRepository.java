package vn.ntduycs.javaintern.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.ntduycs.javaintern.models.QuestionTag;
import vn.ntduycs.javaintern.models.QuestionTagId;
import vn.ntduycs.javaintern.models.Tag;

public interface QuestionTagRepository extends JpaRepository<QuestionTag, QuestionTagId> {

    QuestionTag findByTag(Tag tag);

}
