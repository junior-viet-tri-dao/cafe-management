package com.viettridao.cafe.service.invoice;

import com.viettridao.cafe.model.InvoiceEntity;

import java.util.Optional;

public interface IInvoiceService {

    Optional<InvoiceEntity> findByTableId(Integer tableId);

    void payment(Integer tableId);
}
