package com.viettridao.cafe.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.viettridao.cafe.dto.response.tables.TableMenuItemResponse;
import com.viettridao.cafe.dto.response.tables.TableResponse;
import com.viettridao.cafe.model.InvoiceEntity;
import com.viettridao.cafe.model.ReservationEntity;

public interface TableService {
	Page<TableResponse> getAllTables(int page, int size);

	List<TableMenuItemResponse> getTableMenuItems(Integer tableId);

	Integer getOrCreateInvoiceIdByTableId(Integer tableId);

	ReservationEntity getLatestReservationByTableId(Integer tableId);

	InvoiceEntity getLatestUnpaidInvoiceByTableId(Integer tableId);

	InvoiceEntity getLatestInvoiceByTableId(Integer tableId);

}
