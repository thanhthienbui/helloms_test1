package mks.myworkspace.hellomws.entity;

import java.time.LocalDate;
//import org.springframework.format.annotation.DateTimeFormat; 
import lombok.Getter;
import lombok.Setter;

/**
 * Model (POJO) to hold the date from the test form.
 */
@Getter
@Setter
public class DateTestForm {

    /**
     * This annotation tells Spring to expect the "dd/MM/yyyy" format
     * when it receives data for this field.
     * This MUST match the 'format' in the JavaScript.
     */
    //@DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate testDate;
    
}

