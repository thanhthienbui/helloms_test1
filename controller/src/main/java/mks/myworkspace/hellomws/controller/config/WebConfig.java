package mks.myworkspace.hellomws.controller.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import mks.myworkspace.hellomws.controller.formatter.DynamicLocalDateFormatter;

/**
 * Spring MVC Configuration file.
 * This registers the custom date formatter.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private DynamicLocalDateFormatter dynamicLocalDateFormatter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Tell Spring to use this formatter for all LocalDate fields
        registry.addFormatter(dynamicLocalDateFormatter);
    }
}

