package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.menuitem.MenuItemCreateRequest;
import com.viettridao.cafe.dto.request.menuitem.MenuItemUpdateRequest;
import com.viettridao.cafe.model.MenuItemEntity;
import com.viettridao.cafe.repository.MenuItemRepository;
import com.viettridao.cafe.service.impl.MenuItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class MenuItemServiceTest {
    @Mock
    private MenuItemRepository menuItemRepository;
    @InjectMocks
    private MenuItemServiceImpl menuItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Tạo món thành công")
    void testCreateMenuItem_Success() {
        MenuItemCreateRequest request = new MenuItemCreateRequest();
        request.setItemName("Cà phê sữa");
        request.setCurrentPrice(30000.0);
        request.setDetails(new java.util.ArrayList<>()); // Giả lập chi tiết rỗng
        MenuItemEntity entity = new MenuItemEntity();
        entity.setId(1);
        when(menuItemRepository.save(any(MenuItemEntity.class))).thenReturn(entity);
        assertThatCode(() -> menuItemService.createMenuItem(request)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Cập nhật món không tồn tại")
    void testUpdateMenuItem_NotFound() {
        MenuItemUpdateRequest request = new MenuItemUpdateRequest();
        request.setId(99);
        request.setItemName("Cà phê sữa");
        request.setCurrentPrice(30000.0);
        request.setDetails(new java.util.ArrayList<>());
        when(menuItemRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> menuItemService.updateMenuItem(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy thực đơn");
    }

    @Test
    @DisplayName("Xóa món không tồn tại")
    void testDeleteMenuItem_NotFound() {
        when(menuItemRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> menuItemService.deleteMenuItem(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Không tìm thấy thực đơn");
    }
}
