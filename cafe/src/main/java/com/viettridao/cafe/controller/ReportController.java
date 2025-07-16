package com.viettridao.cafe.controller;

import java.io.ByteArrayOutputStream;
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

import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.viettridao.cafe.common.ReportType;
import com.viettridao.cafe.dto.request.reportstatistics.ReportFilterRequest;
import com.viettridao.cafe.dto.response.employee.EmployeeDailySalaryResponse;
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

		if (type == ReportType.EMPLOYEE_INFO) {
			List<EmployeeDailySalaryResponse> employeeSalaries = reportService.getEmployeeDailySalaries(from, to);
			model.addAttribute("employeeSalaries", employeeSalaries);
		} else {
			List<ReportItemResponse> reports = reportService.getReport(from, to, type);
			double totalRevenue = reports.stream().mapToDouble(r -> r.getRevenue() != null ? r.getRevenue() : 0.0)
					.sum();
			double totalExpense = reports.stream().mapToDouble(r -> r.getExpense() != null ? r.getExpense() : 0.0)
					.sum();

			model.addAttribute("reports", reports);
			model.addAttribute("totalRevenue", totalRevenue);
			model.addAttribute("totalExpense", totalExpense);
		}

		model.addAttribute("from", from);
		model.addAttribute("to", to);
		model.addAttribute("type", type);
		model.addAttribute("types", Arrays.asList(ReportType.values()));
		return "report/statistics";
	}

	@GetMapping("/export")
	public ResponseEntity<Resource> exportReport(@RequestParam("from") LocalDate from, @RequestParam("to") LocalDate to,
			@RequestParam("type") ReportType type, @RequestParam("format") String format) {
		try {
			byte[] data;
			String fileName;

			if ("PDF".equalsIgnoreCase(format)) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Document document = new Document(PageSize.A4);
				PdfWriter.getInstance(document, out);
				document.open();

				if (type == ReportType.EMPLOYEE_INFO) {
					List<EmployeeDailySalaryResponse> employeeSalaries = reportService.getEmployeeDailySalaries(from,
							to);

					document.add(new Paragraph("📊 Báo cáo lương nhân viên",
							FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
					document.add(new Paragraph(" "));

					PdfPTable table = new PdfPTable(3);
					table.setWidthPercentage(100);
					table.addCell("Tên nhân viên");
					table.addCell("Ngày");
					table.addCell("Lương/ngày");

					for (EmployeeDailySalaryResponse e : employeeSalaries) {
						table.addCell(e.getFullName());
						table.addCell(e.getDate().toString());
						table.addCell(String.format("%.0f đ", e.getDailySalary()));
					}
					document.add(table);
				} else {
					List<ReportItemResponse> reports = reportService.getReport(from, to, type);

					document.add(
							new Paragraph("📊 Báo cáo tài chính", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
					document.add(new Paragraph(" "));

					PdfPTable table = new PdfPTable(3);
					table.setWidthPercentage(100);
					table.addCell("Ngày");
					table.addCell("Thu");
					table.addCell("Chi");

					for (ReportItemResponse r : reports) {
						table.addCell(r.getDate().toString());
						table.addCell(String.format("%.0f đ", r.getRevenue() != null ? r.getRevenue() : 0.0));
						table.addCell(String.format("%.0f đ", r.getExpense() != null ? r.getExpense() : 0.0));
					}
					document.add(table);
				}

				document.close();
				data = out.toByteArray();
				fileName = "report.pdf";
			} else {
				StringBuilder content = new StringBuilder();

				if (type == ReportType.EMPLOYEE_INFO) {
					List<EmployeeDailySalaryResponse> employeeSalaries = reportService.getEmployeeDailySalaries(from,
							to);
					content.append("Tên nhân viên\tNgày\tLương/ngày\n");
					for (EmployeeDailySalaryResponse e : employeeSalaries) {
						content.append(e.getFullName()).append("\t").append(e.getDate()).append("\t")
								.append(e.getDailySalary()).append("\n");
					}
				} else {
					List<ReportItemResponse> reports = reportService.getReport(from, to, type);
					content.append("Ngày\tThu\tChi\n");
					for (ReportItemResponse r : reports) {
						content.append(r.getDate()).append("\t").append(r.getRevenue() != null ? r.getRevenue() : 0)
								.append("\t").append(r.getExpense() != null ? r.getExpense() : 0).append("\n");
					}
				}

				data = content.toString().getBytes(StandardCharsets.UTF_8);
				fileName = "report." + format.toLowerCase();
			}

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
