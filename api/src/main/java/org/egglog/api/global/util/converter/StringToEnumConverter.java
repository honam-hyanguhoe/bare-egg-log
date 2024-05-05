package org.egglog.api.global.util.converter;

import org.springframework.core.convert.converter.Converter;


public class StringToEnumConverter<T extends Enum<T>> implements Converter<String, T> {
    private final Class<T> enumType;

    public StringToEnumConverter(Class<T> enumType) {
        this.enumType = enumType;
    }

    @Override
    public T convert(String source) {
        return Enum.valueOf(enumType, source.toUpperCase());
    }
}
