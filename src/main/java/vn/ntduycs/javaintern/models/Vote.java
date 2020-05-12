package vn.ntduycs.javaintern.models;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "votes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "voter_id",
                "choice_id"
        })
})
public class Vote extends BaseModel {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NonNull
    private Choice choice;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @NonNull
    private User voter;

}
