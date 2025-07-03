package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.request.employee.CreateEmployeeRequest;
import com.viettridao.cafe.dto.request.table.TableRequest;
import com.viettridao.cafe.dto.response.table.TableResponse;
import com.viettridao.cafe.mapper.TableMapper;
import com.viettridao.cafe.service.TableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sale")
public class SaleController {
    private final TableService tableService;
    private final TableMapper tableMapper;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("tables", tableMapper.toListResponse(tableService.getAllTables()));
        return "/sales/sale";
    }

    @GetMapping("/create")
    public String showFormCreate(Model model) {
        model.addAttribute("table", new TableRequest());
        return "/sales/create_table";
    }

    @PostMapping("/create")
    public String createEmployee(@Valid @ModelAttribute("table") TableRequest table, BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        try{
            if (result.hasErrors()) {

            }

            tableService.create(table);
            redirectAttributes.addFlashAttribute("success", "Thêm bàn thành công");
            return "redirect:/sale";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/sale/create";
        }
    }

    @GetMapping("/view/{id}")
    public String getTable(@PathVariable Integer id, Model model) {
        model.addAttribute("", tableMapper.toResponse(tableService.getTableById(id)));
        return "/sales/create_table";
    }
}
