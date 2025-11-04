package mks.myworkspace.hellomws.controller.config;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice 
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public String handleBindingException(BindException ex, RedirectAttributes redirectAttributes) {
        
        // Log lỗi gốc
        log.error("--- [GlobalExceptionHandler] ĐÃ BẮT ĐƯỢC LỖI BINDING (400 BAD REQUEST) ---", ex);

        // Cố gắng tìm lỗi cụ thể trên trường 'testDate'
        FieldError fieldError = ex.getFieldError("testDate");
        
        String errorMessage;
        if (fieldError != null) {
            // Lấy giá trị sai mà người dùng đã nhập
            String rejectedValue = String.valueOf(fieldError.getRejectedValue());
            
            log.error("--- [GlobalExceptionHandler] Lỗi cụ thể trên trường: 'testDate'. Giá trị bị từ chối: '{}'", rejectedValue);
            errorMessage = "Lỗi format ngày. Giá trị bạn nhập là: '" + rejectedValue + "' không hợp lệ.";
        } else {
            errorMessage = "Có lỗi xảy ra, không thể xử lý dữ liệu.";
        }

        // Thêm thông báo lỗi vào Flash (để redirect không bị mất)
        redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
        
        // Trả người dùng về trang chủ (thay vì trang lỗi trắng)
        return "redirect:/";
    }
}