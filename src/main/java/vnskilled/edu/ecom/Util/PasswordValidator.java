package vnskilled.edu.ecom.Util;

import org.apache.commons.lang3.StringUtils;

public class PasswordValidator {
    public static void validatePassword(String password) {
        // Kiểm tra mật khẩu không chứa khoảng trắng
        if (StringUtils.containsWhitespace(password)) {
            throw new RuntimeException("Mật khẩu không thể chứa ký tự trống");
        }

        // Kiểm tra độ dài mật khẩu tối thiểu và tối đa
        if (password.length() < 8) {
            throw new RuntimeException("Mật khẩu phải có tối thiểu 8 ký tự");
        }
        if (password.length() > 255) {
            throw new RuntimeException("Mật khẩu không thể có nhiều hơn 255 ký tự");
        }

        // Kiểm tra các yêu cầu khác
        if (!StringUtils.containsAny(password, "ABCDEFGHIJKLMNOPQRSTUVWXYZ")) {
            throw new RuntimeException("Mật khẩu phải chứa ít nhất một chữ cái viết hoa");
        }
        if (!StringUtils.containsAny(password, "abcdefghijklmnopqrstuvwxyz")) {
            throw new RuntimeException("Mật khẩu phải chứa ít nhất một chữ cái viết thường");
        }
        if (!StringUtils.containsAny(password, "0123456789")) {
            throw new RuntimeException("Mật khẩu phải chứa ít nhất một chữ số");
        }
        if (!StringUtils.containsAny(password, "!@#$%^&*(),.?\\\":{}|<>")) {
            throw new RuntimeException("Mật khẩu phải chứa ít nhất một ký tự đặc biệt (!@#$%^&*(),.?\":{}|<>)");
        }
    }
}
