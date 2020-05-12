package vn.ntduycs.javaintern.payloads;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ChoiceResponse {
    @NonNull
    private Long id;

    @NonNull
    private String content;

    @NonNull
    private Long voteCount;
}
