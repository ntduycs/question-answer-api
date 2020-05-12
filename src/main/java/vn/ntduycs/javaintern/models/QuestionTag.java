package vn.ntduycs.javaintern.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "question_tags")
public class QuestionTag implements Serializable {

    @EmbeddedId
    private QuestionTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("questionId")
    private Question question;

    @ManyToOne
    @MapsId("tagId")
    private Tag tag;

    public QuestionTag(Question question, Tag tag) {
        this.question = question;
        this.tag = tag;
        this.id = new QuestionTagId(question.getId(), tag.getId());
    }

}
