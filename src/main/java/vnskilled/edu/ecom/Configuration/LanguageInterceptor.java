package vnskilled.edu.ecom.Configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Locale;
@Component
public class LanguageInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String language = request.getHeader("Accept-Language");
        if (language != null && !language.isEmpty()) {
            LocaleContextHolder.setLocale(new Locale(language));
        } else {
            LocaleContextHolder.setLocale(new Locale("vn")); // Mặc định là tiếng Việt
        }
        return true;
    }
}
