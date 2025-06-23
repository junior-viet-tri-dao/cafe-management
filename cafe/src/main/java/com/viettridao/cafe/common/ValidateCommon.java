package com.viettridao.cafe.common;

public class ValidateCommon {

    // Kiểm tra các trường bắt buộc (không rỗng/null)
    public static boolean checkRequiredFields(String... fields) {
        for (String field : fields) {
            if (field == null || field.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Kiểm tra số điện thoại (10-11 chữ số)
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("\\d{10,11}");
    }

    // Kiểm tra định dạng số hợp lệ (dương)
    public static boolean isPositiveNumber(Integer number) {
        return number != null && number >= 0;
    }

    // Kiểm tra định dạng ngày và giờ nếu cần sau này
}