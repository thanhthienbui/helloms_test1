package mks.myworkspace.hellomws.controller.formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component 
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

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
    	
    	log.info("--- [Formatter PARSE] Bắt đầu parse. Giá trị (text) = '{}', Ngôn ngữ (locale) = '{}'", text, locale.getLanguage());
        if (text == null || text.isEmpty()) {
        	log.warn("--- [Formatter PARSE] Text rỗng hoặc null, trả về null.");
            return null;
        }
        
        String lang = locale.getLanguage(); // e.g., "vi"
        String format = FORMAT_MAPPING.getOrDefault(lang, DEFAULT_FORMAT);
        
        log.info("--- [Formatter PARSE] Đã quyết định dùng format: '{}'", format);
        
        /*try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(text, formatter);
        } catch (Exception e) {
        	log.error("--- [Formatter PARSE] LỖI NGHIÊM TRỌNG: Không thể parse date: '" + text + "' với format '" + format + "'", e);
            throw new ParseException("Cannot parse date: " + text + " with format " + format, 0);
        }*/
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDate date = LocalDate.parse(text, formatter);
            log.info("--- [Formatter PARSE] Parse THÀNH CÔNG. Kết quả: {}", date);
            return date;
        } catch (Exception e) {
        	log.error("--- [Formatter PARSE] LỖI NGHIÊM TRỌNG: Không thể parse date: '" + text + "' với format '" + format + "'", e);
            throw new ParseException("Cannot parse date: " + text + " with format " + format, 0);
        }
    }

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

