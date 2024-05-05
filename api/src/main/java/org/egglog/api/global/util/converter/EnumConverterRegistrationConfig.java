package org.egglog.api.global.util.converter;

import org.reflections.Reflections;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class EnumConverterRegistrationConfig implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Reflections reflections = new Reflections("org.egglog.api");
        Set<Class<?>> enumClasses = reflections.getTypesAnnotatedWith(EnumToUpperCase.class);

        for (Class<?> enumClass : enumClasses) {
            if (enumClass.isEnum()) {
                registerConverter(beanFactory, enumClass);
            }
        }
    }

    private void registerConverter(ConfigurableListableBeanFactory beanFactory, Class<?> enumType) {
        StringToEnumConverter<?> converter = new StringToEnumConverter(enumType);
        beanFactory.registerSingleton(enumType.getSimpleName() + "Converter", converter);
    }
}
