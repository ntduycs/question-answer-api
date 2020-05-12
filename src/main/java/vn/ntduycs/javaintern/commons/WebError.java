package vn.ntduycs.javaintern.commons;

import lombok.*;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class WebError {

    @NonNull
    private int code;

    @NonNull
    private String message;

    @NonNull
    private String reason;

    @NonNull
    private String exception;

    @NonNull
    private String path;

    private List<SubError> errors;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new LinkedHashMap<>();

        map.put("code", code);
        map.put("message", message);
        map.put("reason", reason);
        map.put("exception", exception);
        map.put("path", path);

        if (errors != null) {
            map.put("errors", errors);
        }

        return map;
    }

    public static WebError fromMap(Map<String, Object> errorAttributes) {
        return new WebError(
                (Integer) errorAttributes.get("status"),
                (String) errorAttributes.get("error"),
                (String) errorAttributes.get("message"),
                (String) errorAttributes.getOrDefault("exception", "Exception not available"),
                (String) errorAttributes.getOrDefault("path", "Path not available")
        );
    }

    @AllArgsConstructor
    @Getter
    public static final class SubError {
        private final String message;
        private final String reason;
    }

    public static final class ErrorAttributes extends DefaultErrorAttributes {
        public ErrorAttributes() {
            super(true);
        }

        @Override
        public Map<String, Object> getErrorAttributes(WebRequest request, boolean includeStackTrace) {
            Map<String, Object> errorAttributes = super.getErrorAttributes(request, false);

            WebError error = fromMap(errorAttributes);

            return error.toMap();
        }
    }

}
