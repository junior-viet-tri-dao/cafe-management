package com.viettridao.cafe.dto.response.table;

import com.viettridao.cafe.dto.response.PageResponse;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.model.TableEntity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TableResponsePage extends PageResponse {

    @NotNull(message = "ID không được để trống")
    @Min(value = 1, message = "ID phải là số nguyên dương")
    private Integer id;

    @NotBlank(message = "Trạng thái không được để trống") // Đảm bảo không rỗng và không chỉ chứa khoảng trắng
    @Size(min = 2, max = 50, message = "Trạng thái phải từ 2 đến 50 ký tự")
    private String status;

    @NotBlank(message = "Tên bàn không được để trống")
    @Size(min = 2, max = 100, message = "Tên bàn phải từ 2 đến 100 ký tự")
    private String tableName;

    @NotNull(message = "Trạng thái xóa không được để trống")
    private Boolean deleted;
}
