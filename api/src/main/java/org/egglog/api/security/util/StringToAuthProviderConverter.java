package org.egglog.api.security.util;

import org.egglog.api.user.model.entity.enums.AuthProvider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToAuthProviderConverter implements Converter<String, AuthProvider> {
    @Override
    public AuthProvider convert(String source) {
        return AuthProvider.valueOf(source.toUpperCase());
    }
}
