package com.viettridao.cafe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.viettridao.cafe.service.TableClearService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sale") // ✅ Gốc URL là /sale
public class SalesController {

    private final TableClearService tableClearService;

    /**
     * Hủy dữ liệu trên bàn (thực đơn, hóa đơn...) nếu bàn không ở trạng thái OCCUPIED.
     */
    @PostMapping("/clear")
    public String clearTable(
            @RequestParam("tableId") Integer tableId,
            RedirectAttributes redirectAttributes
    ) {
        try {
            tableClearService.clearTable(tableId);
            redirectAttributes.addFlashAttribute("success", "Đã hủy bàn thành công.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/sale";
    }

}
