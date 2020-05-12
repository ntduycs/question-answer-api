package vn.ntduycs.javaintern.payloads;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class QuestionCreatorResponse {

    @NonNull
    private Long id;

    @NonNull
    private String fullName;

}
