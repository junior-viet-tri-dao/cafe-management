package com.viettridao.cafe.exception;

import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	// Lỗi 404 - Không tìm thấy trang
	@ExceptionHandler(NoHandlerFoundException.class)
	public String handleNotFound(NoHandlerFoundException ex, Model model) {
		model.addAttribute("error", "Không tìm thấy trang bạn yêu cầu.");
		model.addAttribute("message", ex.getMessage());
		return "error/404";
	}

	// Lỗi validation @Valid nhưng không có BindingResult → fallback
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleValidationException(MethodArgumentNotValidException ex, Model model) {
		String errorMessages = ex.getBindingResult().getFieldErrors().stream()
			.map(error -> error.getField() + ": " + error.getDefaultMessage())
			.collect(Collectors.joining("<br/>"));

		model.addAttribute("error", "Dữ liệu không hợp lệ");
		model.addAttribute("message", errorMessages);
		return "error/validation";
	}

	// Vi phạm ràng buộc @PathVariable, @RequestParam
	@ExceptionHandler(ConstraintViolationException.class)
	public String handleConstraintViolation(ConstraintViolationException ex, Model model) {
		String errorMessages = ex.getConstraintViolations().stream()
			.map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
			.collect(Collectors.joining("<br/>"));

		model.addAttribute("error", "Vi phạm ràng buộc dữ liệu");
		model.addAttribute("message", errorMessages);
		return "error/validation";
	}

	// Lỗi hệ thống chung
	@ExceptionHandler(Exception.class)
	public String handleGeneralException(Exception ex, Model model) {
		model.addAttribute("error", "Lỗi hệ thống");
		model.addAttribute("message", ex.getMessage());
		return "error/500";
	}
}
