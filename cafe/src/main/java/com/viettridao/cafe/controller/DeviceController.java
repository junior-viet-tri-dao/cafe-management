package com.viettridao.cafe.controller;

import com.viettridao.cafe.dto.device.DeviceDTO;
import com.viettridao.cafe.dto.device.UpdateDeviceDTO;
import com.viettridao.cafe.dto.employee.UpdateEmployeeDTO;
import com.viettridao.cafe.model.ThietBi;
import com.viettridao.cafe.service.ThietBiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/device")
public class DeviceController {
    private final ThietBiService thietBiService;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("devices", thietBiService.getAllThietBi());
        return "device/device";
    }

    @GetMapping("/create")
    public String showFromCreateDevice() {
        return "/device/create_device";
    }

    @PostMapping("/create")
    public String createDevice(@ModelAttribute DeviceDTO deviceDTO, RedirectAttributes redirectAttributes) {
        try{
            thietBiService.createDevice(deviceDTO);
            redirectAttributes.addFlashAttribute("success", "Thêm thiết bị thành công");
            return "redirect:/device";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/device";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteDevice(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try{
            thietBiService.deleteDevice(id);
            redirectAttributes.addFlashAttribute("success", "Xoá thiết bị thành công");
            return "redirect:/device";
        }catch (Exception e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/device";
        }
    }

    @GetMapping("/edit/{id}")
    public String updateDeviceForm(@PathVariable("id") Integer id, Model model) {
        ThietBi tb = thietBiService.getDeviceById(id);

        if (tb == null) {
            throw new RuntimeException("Không tìm thấy thiết bị với ID: " + id);
        }
        model.addAttribute("tb", tb);
        return "device/update_device";
    }

    @PostMapping("/update")
    public String updateDevice(@ModelAttribute UpdateDeviceDTO deviceDTO, RedirectAttributes redirectAttributes) {
        try{
            thietBiService.updateDevice(deviceDTO);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thiết bị thành công");
            return "redirect:/device";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/device";
        }
    }

}
