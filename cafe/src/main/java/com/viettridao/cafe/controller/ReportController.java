package com.viettridao.cafe.controller;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.common.ReportType;
import com.viettridao.cafe.dto.request.reportstatistics.ReportFilterRequest;
import com.viettridao.cafe.dto.response.reportstatistics.ReportItemResponse;
import com.viettridao.cafe.service.ReportService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;

	@GetMapping("/statistics")
	public String getReport(Model model) {
		model.addAttribute("reportFilterRequest", new ReportFilterRequest());
		model.addAttribute("types", Arrays.asList(ReportType.values()));
		return "report/statistics";
	}

	@PostMapping("/statistics")
	public String postReport(@Valid @ModelAttribute("reportFilterRequest") ReportFilterRequest request,
			BindingResult bindingResult, Model model) {

		if (!request.isValidDateRange()) {
			bindingResult.rejectValue("toDate", "invalidDateRange", "Ngày kết thúc phải sau hoặc bằng ngày bắt đầu");
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("types", Arrays.asList(ReportType.values()));
			return "report/statistics";
		}

		LocalDate from = request.getFromDate();
		LocalDate to = request.getToDate();
		ReportType type = request.getCategory() != null ? ReportType.valueOf(request.getCategory()) : ReportType.ALL;

		List<ReportItemResponse> reports = reportService.getReport(from, to, type);

		double totalRevenue = reports.stream().mapToDouble(r -> r.getRevenue() != null ? r.getRevenue() : 0.0).sum();
		double totalExpense = reports.stream().mapToDouble(r -> r.getExpense() != null ? r.getExpense() : 0.0).sum();

		model.addAttribute("reports", reports);
		model.addAttribute("from", from);
		model.addAttribute("to", to);
		model.addAttribute("type", type);
		model.addAttribute("types", Arrays.asList(ReportType.values()));
		model.addAttribute("totalRevenue", totalRevenue);
		model.addAttribute("totalExpense", totalExpense);

		return "report/statistics";
	}

	@GetMapping("/export")
	public ResponseEntity<Resource> exportReport(@RequestParam("from") LocalDate from, @RequestParam("to") LocalDate to,
			@RequestParam("type") ReportType type, @RequestParam("format") String format) {

		try {
			List<ReportItemResponse> reports = reportService.getReport(from, to, type);

			StringBuilder content = new StringBuilder("Ngày\tThu\tChi\n");
			for (ReportItemResponse r : reports) {
				content.append(r.getDate()).append("\t").append(r.getRevenue() != null ? r.getRevenue() : 0)
						.append("\t").append(r.getExpense() != null ? r.getExpense() : 0).append("\n");
			}

			byte[] data = content.toString().getBytes(StandardCharsets.UTF_8);
			String fileName = "report." + format.toLowerCase();
			ByteArrayResource resource = new ByteArrayResource(data);

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
					.contentLength(data.length).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);

		} catch (Exception e) {
			byte[] errorData = "Lỗi khi xuất báo cáo.".getBytes(StandardCharsets.UTF_8);
			ByteArrayResource resource = new ByteArrayResource(errorData);

			return ResponseEntity.internalServerError()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=error.txt")
					.contentLength(errorData.length).contentType(MediaType.TEXT_PLAIN).body(resource);
		}
	}

	@PostMapping("/print")
	public String printReport(@RequestParam("paperSize") String paperSize,
			@RequestParam("printerName") String printerName, @RequestParam("copies") int copies,
			RedirectAttributes redirectAttributes) {

		try {
			String message = String.format("Đã gửi lệnh in: %d bản, khổ giấy %s, máy in %s", copies, paperSize,
					printerName);
			redirectAttributes.addFlashAttribute("success", message);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Đã xảy ra lỗi khi in báo cáo.");
		}

		return "redirect:/report/statistics";
	}
}