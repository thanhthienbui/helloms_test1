package mks.myworkspace.hellomws.controller;

import java.time.LocalDate;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

import mks.myworkspace.hellomws.controller.formatter.DynamicLocalDateFormatter;
import mks.myworkspace.hellomws.entity.DateTestForm;

import lombok.extern.slf4j.Slf4j; 


@Slf4j 
@Controller
public class HelloController extends BaseController {
	
	@Autowired
    private DynamicLocalDateFormatter dateFormatter;

	@InitBinder
    public void initBinder(WebDataBinder binder) {

        binder.addCustomFormatter(dateFormatter);
    }
	
    @GetMapping("/")
    public String showHome(Model model, HttpServletRequest request) {
        initSession(request, request.getSession()); //
        
        // Chuẩn bị một đối tượng form trống
        if (!model.containsAttribute("dateTestForm")) {
            model.addAttribute("dateTestForm", new DateTestForm());
        }
        // Lấy ngôn ngữ hiện tại (từ CookieLocaleResolver)
        Locale currentLocale = LocaleContextHolder.getLocale();
        String lang = currentLocale.getLanguage(); 
        String format = dateFormatter.getFormatPattern(currentLocale);
        
        model.addAttribute("currentFormat", format);
        model.addAttribute("currentLocale", lang);


        return "home"; // Trỏ tới file 'home.html'
    }
    

    @PostMapping("/")
    public String handleDateTestForm(
            @ModelAttribute DateTestForm dateTestForm,
            RedirectAttributes redirectAttributes) {

  
        LocalDate parsedDate = dateTestForm.getTestDate();
        

        log.info(">>>> [DYNAMIC TEST] Successfully parsed date: " + parsedDate);

        Locale currentLocale = LocaleContextHolder.getLocale();
        
        if (parsedDate != null) {
        	
        	String formattedDate = dateFormatter.print(parsedDate, currentLocale);
            // 2. Gửi thông báo thành công ra frontend
            redirectAttributes.addFlashAttribute("successMessage",
                "SUCCESS! Server parsed date: " + formattedDate);
        } else {
            // 3. Gửi thông báo lỗi ra frontend
            redirectAttributes.addFlashAttribute("errorMessage",
                "Error. Date was null or invalid format.");
        }
        
        return "redirect:/"; // Quay lại trang chủ
    }
}