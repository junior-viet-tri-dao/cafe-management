package com.viettridao.cafe.service;

public interface TableTransferService {
    /**
     * Chuyển hóa đơn chưa thanh toán từ bàn nguồn sang bàn đích.
     *
     * @param fromTableId ID bàn nguồn (bàn đang chứa món).
     * @param toTableId   ID bàn đích (bàn cần chuyển sang).
     */
    void transferTable(Integer fromTableId, Integer toTableId);
}