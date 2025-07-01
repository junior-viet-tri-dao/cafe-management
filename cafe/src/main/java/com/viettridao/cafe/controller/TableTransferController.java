package com.viettridao.cafe.controller;

import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.repository.TableRepository;
import com.viettridao.cafe.service.TableTransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class TableTransferController {

    private final TableRepository tableRepository;
    private final TableTransferService tableTransferService;

    // ✅ Hiển thị giao diện chuyển bàn
    @GetMapping("/transfer-table")
    public String showTransferTableForm(@RequestParam("from") Integer fromTableId, Model model) {
        TableEntity fromTable = tableRepository.findById(fromTableId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn nguồn."));

        List<TableEntity> allTables = tableRepository.findByIsDeletedFalse();

        model.addAttribute("fromTableId", fromTableId);
        model.addAttribute("fromTableName", fromTable.getTableName());
        model.addAttribute("availableTables", allTables);
        return "sale/transfer-table";
    }

    // ✅ Xử lý chuyển bàn
    @PostMapping("/transfer-table")
    public String transferTable(@RequestParam("fromTableId") Integer fromTableId,
                                @RequestParam("toTableId") Integer toTableId,
                                RedirectAttributes redirectAttributes) {
        try {
            tableTransferService.transferTable(fromTableId, toTableId);
            redirectAttributes.addFlashAttribute("success", "Chuyển bàn thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi chuyển bàn: " + e.getMessage());
        }

        // ✅ Chuyển về danh sách bàn
        return "redirect:/sale";
    }
}
