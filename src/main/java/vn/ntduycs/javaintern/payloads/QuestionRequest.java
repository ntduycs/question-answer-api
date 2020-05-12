package vn.ntduycs.javaintern.payloads;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class QuestionRequest extends BaseRequest {

    @NotBlank(message = "'content' is required but not be given")
    private String content;

    @NotNull(message = "'choices' is required but not be given")
    @Size(min = 2, message = "'choices' must contain at least 2 items")
    @Valid
    private List<ChoiceRequest> choices;

    @NotNull(message = "'expiration' is required but not be given")
    @Valid
    private QuestionExpirationRequest expiration;

    @NotNull(message = "'open' is required but not be given")
    private boolean open;

    private Set<String> tags;

}
