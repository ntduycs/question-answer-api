package vn.ntduycs.javaintern.payloads;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class ChoiceRequest extends BaseRequest {

    @NotBlank(message = "Choice 'content' is required but not be given")
    private String content;

}
