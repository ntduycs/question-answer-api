package vn.ntduycs.javaintern.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;

import static org.springframework.data.domain.Sort.Direction;

public class String2DirectionConverter implements Converter<String, Direction> {
    @Override
    public Direction convert(@NonNull String s) {
        return Direction.fromString(s);
    }
}
