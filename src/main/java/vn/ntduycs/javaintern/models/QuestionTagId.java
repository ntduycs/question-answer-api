package vn.ntduycs.javaintern.models;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Embeddable
public class QuestionTagId implements Serializable {

    @NonNull
    private Long questionId;

    @NonNull
    private Long tagId;

}
