package com.viettridao.cafe.mapper;

import com.viettridao.cafe.dto.response.reservation.MenuItemReservationResponse;
import com.viettridao.cafe.dto.response.reservation.ReservationResponse;
import com.viettridao.cafe.model.InvoiceDetailEntity;
import com.viettridao.cafe.model.ReservationEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationMapper {
    private final ModelMapper modelMapper;

    public ReservationResponse toResponse(ReservationEntity entity){
        ReservationResponse response = new ReservationResponse();
        response.setReservationDate(entity.getReservationDate());
        response.setCustomerName(entity.getCustomerName());
        response.setCustomerPhone(entity.getCustomerPhone());

        if(entity.getTable() != null){
            response.setTableName(entity.getTable().getTableName());
            response.setStatus(entity.getTable().getStatus());
        }

        if(entity.getInvoice() != null){
            response.setTotalPrice(entity.getInvoice().getTotalAmount());
        }

        List<MenuItemReservationResponse> menuItemResponses = new ArrayList<>();
        if(entity.getInvoice().getInvoiceDetails() != null){
           for(InvoiceDetailEntity invoiceDetail : entity.getInvoice().getInvoiceDetails()){
               MenuItemReservationResponse menuItemResponse = new MenuItemReservationResponse();

               menuItemResponse.setItemName(invoiceDetail.getMenuItem().getItemName());
               menuItemResponse.setQuantity(invoiceDetail.getQuantity());
               menuItemResponse.setPrice(invoiceDetail.getPrice());

               menuItemResponses.add(menuItemResponse);
           }
        }
        response.setMenuItems(menuItemResponses);

        return response;
    }
}
