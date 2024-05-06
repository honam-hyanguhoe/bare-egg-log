package org.egglog.api.global.util.converter;
import org.reflections.Reflections;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Set;

@Configuration
public class EnumConverterRegistrationConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        Reflections reflections = new Reflections("org.egglog.api");
        Set<Class<?>> enumClasses = reflections.getTypesAnnotatedWith(NormalizeEnumCase.class);

        for (Class<?> enumClass : enumClasses) {
            if (enumClass.isEnum()) {
                @SuppressWarnings("unchecked")
                Class<? extends Enum<?>> enumType = (Class<? extends Enum<?>>) enumClass;
                registry.addConverter(String.class, enumType, new StringToEnumConverter(enumType));
            }
        }
    }

    private static class StringToEnumConverter<T extends Enum<T>> implements Converter<String, T> {
        private final Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        @Override
        public T convert(String source) {
            return Enum.valueOf(enumType, source.toUpperCase());
        }
    }
}
