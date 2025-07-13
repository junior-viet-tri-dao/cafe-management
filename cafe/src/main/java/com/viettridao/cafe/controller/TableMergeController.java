package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.common.TableStatus;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableMergeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/sale")
@RequiredArgsConstructor
public class TableMergeController {

	private final TableRepository tableRepository;
	private final TableMergeService tableMergeService;

	@GetMapping("/merge-tables")
	public String showMergePage(Model model) {
		try {
			model.addAttribute("tables", tableRepository.findByIsDeletedFalse());
		} catch (Exception e) {
			model.addAttribute("error", "Lỗi khi tải danh sách bàn: " + e.getMessage());
		}
		return "sale/merge-tables";
	}

	@PostMapping("/merge-tables")
	public String mergeTables(@RequestParam("targetTableId") Integer targetId,
			@RequestParam(value = "sourceTableIds", required = false) List<Integer> sourceIds,
			@RequestParam("customerName") String customerName, @RequestParam("customerPhone") String customerPhone,
			RedirectAttributes redirectAttributes) {

		try {
			if (sourceIds == null || sourceIds.isEmpty()) {
				redirectAttributes.addFlashAttribute("error", "Vui lòng chọn ít nhất một bàn cần gộp (nguồn).");
				redirectAttributes.addFlashAttribute("targetTableId", targetId);
				redirectAttributes.addFlashAttribute("customerName", customerName);
				redirectAttributes.addFlashAttribute("customerPhone", customerPhone);
				return "redirect:/sale/merge-tables";
			}

			TableEntity targetTable = tableRepository.findById(targetId).orElse(null);
			if (targetTable == null) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy bàn đích.");
				return "redirect:/sale/merge-tables";
			}
			if (targetTable.getStatus() == TableStatus.RESERVED) {
				redirectAttributes.addFlashAttribute("error", "Không thể gộp vào bàn đã được đặt trước.");
				return "redirect:/sale/merge-tables";
			}

			for (Integer sourceId : sourceIds) {
				TableEntity sourceTable = tableRepository.findById(sourceId).orElse(null);
				if (sourceTable == null) {
					redirectAttributes.addFlashAttribute("error", "Không tìm thấy bàn nguồn.");
					return "redirect:/sale/merge-tables";
				}
				if (sourceTable.getStatus() == TableStatus.RESERVED) {
					redirectAttributes.addFlashAttribute("error", "Không thể gộp từ bàn đã được đặt trước.");
					return "redirect:/sale/merge-tables";
				}
			}

			boolean targetIsOccupied = targetTable.getStatus() == TableStatus.OCCUPIED;
			boolean hasSourceOccupied = sourceIds.stream().map(id -> tableRepository.findById(id).orElse(null))
					.anyMatch(t -> t != null && t.getStatus() == TableStatus.OCCUPIED);

			boolean needCustomerInfo = false;

			if (sourceIds.size() > 1) {
				needCustomerInfo = true;
			} else if (hasSourceOccupied && targetIsOccupied) {
				needCustomerInfo = true;
			} else if (!targetIsOccupied && hasSourceOccupied) {
				needCustomerInfo = false;
			} else if (targetIsOccupied && !hasSourceOccupied) {
				needCustomerInfo = true;
			}

			if (needCustomerInfo && (customerName == null || customerName.trim().isEmpty() || customerPhone == null
					|| customerPhone.trim().isEmpty())) {
				redirectAttributes.addFlashAttribute("error",
						"Vui lòng nhập Tên khách và Số điện thoại theo yêu cầu gộp.");
				redirectAttributes.addFlashAttribute("targetTableId", targetId);
				redirectAttributes.addFlashAttribute("sourceTableIds", sourceIds);
				redirectAttributes.addFlashAttribute("customerName", customerName);
				redirectAttributes.addFlashAttribute("customerPhone", customerPhone);
				return "redirect:/sale/merge-tables";
			}

			tableMergeService.mergeTables(targetId, sourceIds, customerName, customerPhone);
			redirectAttributes.addFlashAttribute("success", " Gộp bàn thành công.");
			return "redirect:/sale";

		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "❌ " + e.getMessage());
			redirectAttributes.addFlashAttribute("targetTableId", targetId);
			redirectAttributes.addFlashAttribute("sourceTableIds", sourceIds);
			redirectAttributes.addFlashAttribute("customerName", customerName);
			redirectAttributes.addFlashAttribute("customerPhone", customerPhone);
			return "redirect:/sale/merge-tables";
		}
	}
}
