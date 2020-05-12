package vn.ntduycs.javaintern.models;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"content"})
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "choices")
@EntityListeners(AuditingEntityListener.class)
public class Choice extends BaseModel {

    @Column(nullable = false)
    @NonNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Question question;

    @CreatedBy
    private Long createdBy;

}
