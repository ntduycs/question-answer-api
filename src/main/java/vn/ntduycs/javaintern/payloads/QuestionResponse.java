package vn.ntduycs.javaintern.payloads;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class QuestionResponse {

    @NonNull
    private Long id;

    @NonNull
    private String content;

    @NonNull
    private List<ChoiceResponse> choices;

    @NonNull
    private QuestionCreatorResponse creator;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SS")
    private LocalDateTime createdAt;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SS")
    private LocalDateTime expiredAt;

    @NonNull
    private boolean isExpired;

    @NonNull
    private Long totalVotes;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long myChoice;

    @NonNull
    private List<String> tags;
}
