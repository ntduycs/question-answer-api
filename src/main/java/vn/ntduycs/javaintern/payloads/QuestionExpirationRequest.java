package vn.ntduycs.javaintern.payloads;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class QuestionExpirationRequest extends BaseRequest {

    @NotNull(message = "'days' is required but not be given")
    @Min(value = 0, message = "'days' must be greater than or equal to 0 day")
    @Max(value = 7, message = "'days' must be less than or equal 7 days")
    private Integer days;

    @NotNull(message = "'hours' is required but not be given")
    @Min(value = 0, message = "'hours' must be greater than or equal to 0 hour")
    @Max(value = 23, message = "'hours' must be less than or equal to 23 hours")
    private Integer hours;

}
