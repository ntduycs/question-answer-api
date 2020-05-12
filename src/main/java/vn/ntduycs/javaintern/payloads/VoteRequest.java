package vn.ntduycs.javaintern.payloads;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class VoteRequest extends BaseRequest {

    @NotNull(message = "'choiceId' is required but not be given")
    private Long choiceId;

}
