package com.viettridao.cafe.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.model.Ban;
import com.viettridao.cafe.model.ThucDon;
import com.viettridao.cafe.repository.ThucDonRepository;

@Service
public class ThucDonService {

	@Autowired
	private ThucDonRepository thucDonRepo;
	@Autowired
	private BanService banService;

	public void themMon(Long banId, ThucDon monMoi) {
		Ban ban = banService.timTheoId(banId);
		monMoi.setBan(ban);
		monMoi.setDaXoa(false);
		thucDonRepo.save(monMoi);
		banService.capNhatTrangThaiBan(banId);
	}

	public void xoaMon(Long id) {
		ThucDon mon = thucDonRepo.findById(id).orElse(null);
		if (mon != null) {
			mon.setDaXoa(true);
			thucDonRepo.save(mon);
			banService.capNhatTrangThaiBan(mon.getBan().getId());
		}
	}
}
