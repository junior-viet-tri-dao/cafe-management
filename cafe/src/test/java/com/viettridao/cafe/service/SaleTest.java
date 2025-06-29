package com.viettridao.cafe.service;

import com.viettridao.cafe.controller.SaleController;
import com.viettridao.cafe.model.OrderEntity;
import com.viettridao.cafe.model.TableEntity;
import com.viettridao.cafe.service.PaymentService;
import com.viettridao.cafe.service.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SaleTest {
    @Test
    void testPrintInvoice_OrderFound() {
        // Giả lập order và orderDetails
        OrderEntity order = new OrderEntity();
        order.setId(1);
        order.setOrderDetails(java.util.Collections.emptyList());
        TableEntity table = new TableEntity();
        table.setId(1);
        order.setTable(table);
        when(paymentService.findUnpaidOrderByTableId(1)).thenReturn(Optional.of(order));
        when(paymentService.calculateOrderTotal(1)).thenReturn(100000L);
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String result = saleController.printInvoice(1, model, redirectAttributes);
        assertEquals("sales/invoice", result);
        verify(model).addAttribute(eq("order"), eq(order));
        verify(model).addAttribute(eq("orderDetails"), eq(order.getOrderDetails()));
        verify(model).addAttribute(eq("table"), eq(table));
        verify(model).addAttribute(eq("total"), eq(100000L));
    }

    @Test
    void testPrintInvoice_OrderNotFound() {
        when(paymentService.findUnpaidOrderByTableId(1)).thenReturn(Optional.empty());
        Model model = mock(Model.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        String result = saleController.printInvoice(1, model, redirectAttributes);
        assertEquals("redirect:/tables", result);
        verify(redirectAttributes).addFlashAttribute(eq("error"), contains("Không tìm thấy order"));
    }

    @Mock
    private com.viettridao.cafe.service.ReservationService reservationService;
    @Mock
    private com.viettridao.cafe.repository.ReservationRepository reservationRepository;

    @Test
    void testReserveTable_Success() {
        Integer tableId = 1;
        String customerName = "Nguyen Van A";
        String customerPhone = "0123456789";
        String reservationDate = "2025-07-01";
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        when(reservationRepository.findAllByTable_IdAndIsDeletedFalse(tableId))
                .thenReturn(java.util.Collections.emptyList());
        when(reservationService.createReservation(any())).thenReturn(new com.viettridao.cafe.model.ReservationEntity());

        String result = saleController.reserveTable(tableId, customerName, customerPhone, reservationDate,
                redirectAttributes);
        assertEquals("redirect:/tables", result);
        verify(redirectAttributes).addFlashAttribute(eq("success"), contains("Đặt bàn thành công"));
    }

    @Test
    void testReserveTable_TableAlreadyReserved() {
        Integer tableId = 1;
        String customerName = "Nguyen Van B";
        String customerPhone = "0987654321";
        String reservationDate = "2025-07-01";
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        java.util.List<com.viettridao.cafe.model.ReservationEntity> reservations = java.util.Arrays
                .asList(new com.viettridao.cafe.model.ReservationEntity());
        when(reservationRepository.findAllByTable_IdAndIsDeletedFalse(tableId)).thenReturn(reservations);

        String result = saleController.reserveTable(tableId, customerName, customerPhone, reservationDate,
                redirectAttributes);
        assertEquals("redirect:/tables", result);
        verify(redirectAttributes).addFlashAttribute(eq("error"), contains("Bàn đã có người đặt trước"));
    }

    @Mock
    private TableService tableService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private Model model;
    @Mock
    private RedirectAttributes redirectAttributes;
    @Mock
    private OrderService orderService;
    @Mock
    private com.viettridao.cafe.repository.OrderDetailRepository orderDetailRepository;
    @InjectMocks
    private SaleController saleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testShowPaymentForm() {
        String view = saleController.showPaymentForm(1, model);
        assertEquals("sales/payment_table", view);
        verify(model).addAttribute("tableId", 1);
    }

    @Test
    void testPaymentTable_OrderNotFound() {
        when(paymentService.findUnpaidOrderByTableId(1)).thenReturn(Optional.empty());
        String result = saleController.paymentTable(1, 100000L, null, redirectAttributes);
        assertEquals("redirect:/tables", result);
        verify(redirectAttributes).addFlashAttribute(eq("error"), contains("Không tìm thấy order"));
    }

    @Test
    void testPaymentTable_CustomerPaidNotEnough() {
        OrderEntity order = new OrderEntity();
        order.setId(1);
        when(paymentService.findUnpaidOrderByTableId(1)).thenReturn(Optional.of(order));
        when(paymentService.calculateOrderTotal(1)).thenReturn(200000L);
        String result = saleController.paymentTable(1, 100000L, null, redirectAttributes);
        assertEquals("redirect:/tables", result);
        verify(redirectAttributes).addFlashAttribute(eq("error"), contains("Số tiền khách trả không đủ"));
    }

    @Test
    void testPaymentTable_Success() {
        OrderEntity order = new OrderEntity();
        order.setId(1);
        when(paymentService.findUnpaidOrderByTableId(1)).thenReturn(Optional.of(order));
        when(paymentService.calculateOrderTotal(1)).thenReturn(100000L);
        doNothing().when(paymentService).processPayment(order, 100000L, null);
        doNothing().when(paymentService).updateOrderAndTableStatusAfterPayment(order, 1);
        String result = saleController.paymentTable(1, 100000L, null, redirectAttributes);
        assertEquals("redirect:/tables", result);
        verify(redirectAttributes).addFlashAttribute(eq("success"), contains("Thanh toán thành công"));
    }

    @Test
    void testOrderTable_Success() {
        // Giả lập dữ liệu đầu vào
        Integer tableId = 1;
        java.util.List<Integer> selectedItems = java.util.Arrays.asList(1, 2);
        java.util.Map<String, String> quantities = new java.util.HashMap<>();
        quantities.put("1", "2");
        quantities.put("2", "1");
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);

        // Gọi hàm
        String result = saleController.orderTable(tableId, selectedItems, quantities, redirectAttributes);
        // Kết quả mong đợi
        assertEquals("redirect:/tables", result);
        verify(redirectAttributes).addFlashAttribute(eq("success"), contains("Lưu order thành công"));
    }

    @Test
    void testListTables() {
        // Giả lập dữ liệu
        java.util.List<TableEntity> tables = java.util.Arrays.asList(new TableEntity(), new TableEntity());
        when(tableService.getAllTables()).thenReturn(tables);
        Model model = mock(Model.class);
        String result = saleController.listTables(model);
        assertEquals("tables/tables", result);
        verify(model).addAttribute(eq("tables"), eq(tables));
    }
}
