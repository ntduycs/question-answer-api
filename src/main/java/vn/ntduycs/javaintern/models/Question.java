package vn.ntduycs.javaintern.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(of = {"choices"}, callSuper = true)
@Entity
@Table(name = "questions")
@EntityListeners(AuditingEntityListener.class)
public class Question extends BaseModel {

    @Column(nullable = false)
    @NonNull
    private String content;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SS")
    @NonNull
    private LocalDateTime expiredAt;

    @Column(nullable = false)
    @NonNull
    private boolean openQuestion;

    @CreatedBy
    private Long createdBy;

    @LastModifiedBy
    private Long updatedBy;

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    private List<Choice> choices = new ArrayList<>();

    public void addChoice(Choice choice) {
        choices.add(choice);
        choice.setQuestion(this);
    }

    public void removeChoice(Choice choice) {
        choices.remove(choice);
        choice.setQuestion(null);
    }

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Vote> votes = new ArrayList<>();

    public void addVote(Vote vote) {
        votes.add(vote);
        vote.setQuestion(this);
    }

    public void removeVote(Vote vote) {
        votes.remove(vote);
        vote.setQuestion(null);
    }

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<QuestionTag> tags = new ArrayList<>();

    public void addTag(Tag tag) {
        tags.add(new QuestionTag(this, tag));
    }

    public void addTags(List<Tag> tags) {
        this.tags.addAll(tags.stream().map(tag -> new QuestionTag(this, tag)).collect(Collectors.toList()));
    }

    public void removeTag(Tag tag) {
        for (Iterator<QuestionTag> iterator = tags.iterator(); iterator.hasNext();) {
            QuestionTag questionTag = iterator.next();

            if (questionTag.getQuestion().equals(this) && questionTag.getTag().equals(tag)) {
                iterator.remove();
                questionTag.setQuestion(null);
                questionTag.setTag(null);
            }
        }
    }

}
