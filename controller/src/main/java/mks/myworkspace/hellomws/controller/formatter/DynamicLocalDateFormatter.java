package mks.myworkspace.hellomws.controller.formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

/**
 * This Formatter dynamically parses/prints LocalDate
 * based on the user's current Locale (from CookieLocaleResolver).
 */
@Component // Mark as a Spring bean
public class DynamicLocalDateFormatter implements Formatter<LocalDate> {

    // 1. Define the format mapping (Java uses 'MM' for month)
    private static final Map<String, String> FORMAT_MAPPING = new HashMap<>();
    static {
        FORMAT_MAPPING.put("vi", "dd/MM/yyyy"); // Vietnamese
        FORMAT_MAPPING.put("en", "MM/dd/yyyy"); // English (US)
    }
    
    // Use 'vi' format as default (based on your springapp-servlet.xml [cite: springapp-servlet.xml])
    private static final String DEFAULT_FORMAT = "MM/dd/yyyy";
    
    public String getFormatPattern(Locale locale) {
        String lang = locale.getLanguage(); // e.g., "vi"
        return FORMAT_MAPPING.getOrDefault(lang, DEFAULT_FORMAT);
    }

    /**
     * Converts String -> LocalDate (when receiving data from browser)
     */
    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        if (text == null || text.isEmpty()) {
            return null;
        }
        
        String lang = locale.getLanguage(); // e.g., "vi"
        String format = FORMAT_MAPPING.getOrDefault(lang, DEFAULT_FORMAT);
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(text, formatter);
        } catch (Exception e) {
            throw new ParseException("Cannot parse date: " + text + " with format " + format, 0);
        }
    }

    /**
     * Converts LocalDate -> String (when sending data to browser)
     */
    @Override
    public String print(LocalDate object, Locale locale) {
        if (object == null) {
            return "";
        }
        
        String lang = locale.getLanguage();
        String format = FORMAT_MAPPING.getOrDefault(lang, DEFAULT_FORMAT);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        return object.format(formatter);
    }
}

