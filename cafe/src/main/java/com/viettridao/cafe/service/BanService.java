package com.viettridao.cafe.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.model.Ban;
import com.viettridao.cafe.repository.BanRepository;
import com.viettridao.cafe.repository.DatBanRepository;
import com.viettridao.cafe.repository.ThucDonRepository;

@Service
public class BanService {

	@Autowired
	private BanRepository banRepo;

	@Autowired
	private ThucDonRepository thucDonRepo;

	@Autowired
	private DatBanRepository datBanRepo;

	public Ban timTheoId(Long id) {
		return banRepo.findById(id).orElse(null);
	}

	public List<Ban> layTatCaBanChuaXoa() {
		return banRepo.findAllByDaXoaFalse();
	}
	
	public void capNhat(Ban ban) {
        banRepo.save(ban);
    }

	// Tự động xác định lại trạng thái bàn dựa vào món & đặt bàn
	public void capNhatTrangThaiBan(Long banId) {
		Ban ban = banRepo.findById(banId).orElse(null);
		if (ban == null)
			return;

		boolean daDatTruoc = datBanRepo.findByBan_IdAndDaXoaFalse(banId).isPresent();
		int soLuongMon = thucDonRepo.findByBan_IdAndDaXoaFalse(banId).size();

		if (soLuongMon > 0) {
			ban.setTinhTrang("Đang Phục Vụ");
		} else if (daDatTruoc) {
			ban.setTinhTrang("Đã Đặt Trước");
		} else {
			ban.setTinhTrang("Trống");
		}

		banRepo.save(ban);
	}

}
