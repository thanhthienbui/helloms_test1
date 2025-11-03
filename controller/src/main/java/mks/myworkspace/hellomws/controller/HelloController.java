package mks.myworkspace.hellomws.controller;

import java.time.LocalDate;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Import 2 class mới này
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.beans.factory.annotation.Autowired;

import mks.myworkspace.hellomws.controller.formatter.DynamicLocalDateFormatter;
import mks.myworkspace.hellomws.entity.DateTestForm;

import lombok.extern.slf4j.Slf4j; 

/**
 * Controller for the simple, hardcoded datepicker test.
 * Based on BaseController.
 */
@Slf4j 
@Controller
public class HelloController extends BaseController {
	
	@Autowired
    private DynamicLocalDateFormatter dateFormatter;

    /**
     * GET /: Hiển thị trang chủ và form test
     */
    @GetMapping("/")
    public String showHome(Model model, HttpServletRequest request) {
        initSession(request, request.getSession()); //
        
        // Chuẩn bị một đối tượng form trống
        if (!model.containsAttribute("dateTestForm")) {
            model.addAttribute("dateTestForm", new DateTestForm());
        }
      
        // ========== START: CODE ĐÃ GỘP (ĐỂ FIX LỖI JS) ==========
        // Lấy ngôn ngữ hiện tại (từ CookieLocaleResolver)
        Locale currentLocale = LocaleContextHolder.getLocale();
        String lang = currentLocale.getLanguage(); // "vi", "en", v.v.

     // Hỏi Formatter để lấy format
        String format = dateFormatter.getFormatPattern(currentLocale);

        // Thêm 2 biến này vào Model để home.html (Thymeleaf) có thể đọc
        model.addAttribute("currentFormat", format);
        model.addAttribute("currentLocale", lang);
        // ========== END: CODE ĐÃ GỘP ==========

        return "home"; // Trỏ tới file 'home.html'
    }
    
    /**
     * POST /: Nhận dữ liệu từ form test
     */
    @PostMapping("/")
    public String handleDateTestForm(
            @ModelAttribute DateTestForm dateTestForm,
            RedirectAttributes redirectAttributes) {

        // 'dateTestForm.getTestDate()' đã là một đối tượng LocalDate
        // (nhờ vào DynamicLocalDateFormatter của bạn)
        LocalDate parsedDate = dateTestForm.getTestDate();
        
        // =========================================================
        // 1. ĐÂY LÀ NƠI BẠN XEM DATA (TRONG ECLIPSE CONSOLE)
        log.info(">>>> [DYNAMIC TEST] Successfully parsed date: " + parsedDate);
        // =========================================================
        
        if (parsedDate != null) {
            // 2. Gửi thông báo thành công ra frontend
            redirectAttributes.addFlashAttribute("successMessage",
                "SUCCESS! Server parsed date: " + parsedDate.toString());
        } else {
            // 3. Gửi thông báo lỗi ra frontend
            redirectAttributes.addFlashAttribute("errorMessage",
                "Error. Date was null or invalid format.");
        }
        
        return "redirect:/"; // Quay lại trang chủ
    }
}