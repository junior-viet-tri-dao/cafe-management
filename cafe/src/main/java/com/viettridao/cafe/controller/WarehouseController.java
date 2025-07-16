package com.viettridao.cafe.controller;



import com.viettridao.cafe.dto.request.export.CreateExportRequest;
import com.viettridao.cafe.dto.request.export.UpdateExportRequest;
import com.viettridao.cafe.dto.request.imports.CreateImportRequest;
import com.viettridao.cafe.dto.request.imports.UpdateImportRequest;
import com.viettridao.cafe.model.ExportEntity;
import com.viettridao.cafe.model.ImportEntity;
import com.viettridao.cafe.service.ExportService;
import com.viettridao.cafe.service.ImportService;
import com.viettridao.cafe.service.ProductService;
import com.viettridao.cafe.service.UnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/warehouse")
public class WarehouseController {
    private final ImportService importService;
    private final ProductService productService;
    private final ExportService exportService;


    // export section


    @GetMapping("/list_export")
    public String show_ds_don_xuat(@RequestParam(required = false) String keyword,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   Model model){
        model.addAttribute("exports",exportService.getAllIExportPage(keyword,page,size));


        return "/warehouse/list_export";
    }


    @GetMapping("/insert_export")
    public String show_form_insert_export(Model model){
        model.addAttribute("export", new CreateExportRequest());
        model.addAttribute("products", productService.getAllProduct());
        return "/warehouse/insert_export";
    }


    @PostMapping("/insert_export")
    public String insert_export(@Valid @ModelAttribute("export") CreateExportRequest request, BindingResult result){
        if(result.hasErrors()){
            return "/warehouse/insert_export";
        }

        exportService.createExport(request);
        return "redirect:/warehouse/list_export";
    }
//
//    @GetMapping("/edit_export/{id}")
//    public String show_form_edit_export(@PathVariable("id") Integer id, Model model){
//        ExportEntity exportEntity = exportService.getExportbyId(id);
//        model.addAttribute("export", exportEntity);
//        return "/warehouse/edit_export";
//    }
//
//
//    @PostMapping("edit_export")
//    public String edit_export(@Valid @ModelAttribute("export") UpdateExportRequest request, BindingResult result ){
//        if(result.hasErrors()){
//            return "/warehouse/list_export";
//        }
//
//        exportService.updateExport(request);
//
//        return "/warehouse/list_export";
//    }
//
//
//
//
//    @GetMapping("/delete_export/{id}")
//    public String delete_export(@PathVariable("id") Integer id){
//        exportService.deleteExportbyId(id);
//        return "/warehouse/list_export";
//    }

    // import section


    @GetMapping("/list_import")
    public String show_ds_don_nhap(@RequestParam(required = false) String keyword,
                                   @RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "5") int size,
                                   Model model){
        model.addAttribute("imports",importService.getAllImportPage(keyword,page,size));


        return "/warehouse/list_import";
    }



    @GetMapping("/insert_import")
    public String show_form_insert_import(Model model){
        model.addAttribute("import", new CreateImportRequest());
        model.addAttribute("products", productService.getAllProduct());
        return "/warehouse/insert_import";
    }

    @PostMapping("/insert_import")
    public String insert_import(@Valid @ModelAttribute("import") CreateImportRequest request, BindingResult result){
        if(result.hasErrors()){
            return "/warehouse/list_import";
        }

        importService.createImport(request);

        return "redirect:/warehouse/list_import";
    }
//
//    @GetMapping("/edit_import/{id}")
//    public String show_form_edit_import(@PathVariable("id") Integer id, Model model){
//        ImportEntity importEntity = importService.getImportbyId(id);
//        model.addAttribute("import", importEntity);
//        return "/warehouse/edit_import";
//    }
//
//    @PostMapping("edit_import")
//    public String edit_import(@Valid @ModelAttribute("import") UpdateImportRequest request, BindingResult result ){
//        if(result.hasErrors()){
//            return "/warehouse/list_import";
//        }
//
//        importService.updateImport(request);
//
//        return "/warehouse/list_import";
//    }
//
//    @GetMapping("/delete_import/{id}")
//    public String delete_import(@PathVariable("id") Integer id){
//        importService.deleteImportbyId(id);
//        return "/warehouse/list_import";
//    }
}

