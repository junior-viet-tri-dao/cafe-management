package com.viettridao.cafe.service;

import java.util.List;

public interface TableMergeService {
	void mergeTables(Integer targetTableId, List<Integer> sourceTableIds, String customerName, String customerPhone);

}
