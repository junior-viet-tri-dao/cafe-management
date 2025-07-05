package com.viettridao.cafe.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableTransferService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class TableTransferController {

	private final TableRepository tableRepository;
	private final TableTransferService tableTransferService;

	@GetMapping("/transfer-table")
	public String showTransferTableForm(@RequestParam("from") Integer fromTableId, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			TableEntity fromTable = tableRepository.findById(fromTableId)
					.orElseThrow(() -> new RuntimeException("Không tìm thấy bàn nguồn."));

			List<TableEntity> allTables = tableRepository.findByIsDeletedFalse();

			model.addAttribute("fromTableId", fromTableId);
			model.addAttribute("fromTableName", fromTable.getTableName());
			model.addAttribute("availableTables", allTables);
			return "sale/transfer-table";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi khi hiển thị chuyển bàn: " + e.getMessage());
			return "redirect:/sale";
		}
	}

	@PostMapping("/transfer-table")
	public String transferTable(@RequestParam("fromTableId") Integer fromTableId,
			@RequestParam("toTableId") Integer toTableId, RedirectAttributes redirectAttributes) {
		try {
			tableTransferService.transferTable(fromTableId, toTableId);
			redirectAttributes.addFlashAttribute("success", "Chuyển bàn thành công.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Lỗi khi chuyển bàn: " + e.getMessage());
		}

		return "redirect:/sale";
	}
}
