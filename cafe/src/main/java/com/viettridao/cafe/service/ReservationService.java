package com.viettridao.cafe.service;

import com.viettridao.cafe.dto.request.tables.TableBookingRequest;
import com.viettridao.cafe.dto.response.tables.TableBookingResponse;

public interface ReservationService {
	TableBookingResponse bookTable(TableBookingRequest request);
}
