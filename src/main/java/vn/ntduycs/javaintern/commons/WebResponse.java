package vn.ntduycs.javaintern.commons;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class WebResponse implements Serializable {

    public static final long serialVersionUID = 1L;

    @NonNull
    private int code;

    @NonNull
    private String message;

    @NonNull
    private Object result;

    public static WebResponse build(Object data, HttpStatus status) {
        return new WebResponse(status.value(), status.getReasonPhrase(), data);
    }

    public static WebResponse build(Page<?> page, HttpStatus status) {
        Map<String, Object> data = Map.of(
                "page", page.getNumber(),
                "size", page.getSize(),
                "totalElements", page.getTotalElements(),
                "totalPages", page.getTotalPages(),
                "first", page.isFirst(),
                "last", page.isLast(),
                "items", page.getContent()
        );

        return new WebResponse(status.value(), status.getReasonPhrase(), data);
    }

}
