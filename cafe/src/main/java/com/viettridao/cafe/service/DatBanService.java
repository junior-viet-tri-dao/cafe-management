package com.viettridao.cafe.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.model.Ban;
import com.viettridao.cafe.model.DatBan;
import com.viettridao.cafe.repository.BanRepository;
import com.viettridao.cafe.repository.DatBanRepository;

@Service
public class DatBanService {

	@Autowired
	private DatBanRepository datBanRepo;

	@Autowired
	private BanRepository banRepo;

	@Autowired
	private BanService banService;

	public void datTruocBan(Long banId, DatBan datBan) {
		Ban ban = banRepo.findById(banId).orElse(null);
		if (ban == null)
			return;

		datBan.setBan(ban);
		datBan.setDaXoa(false);
		datBanRepo.save(datBan);

		// ✅ Cập nhật trạng thái ngay sau khi đặt
		ban.setTinhTrang("Đã Đặt Trước");
		banRepo.save(ban);
	}

	public String xoaDatBan(Long datBanId) {
		DatBan dat = datBanRepo.findById(datBanId).orElse(null);
		if (dat == null)
			return "Không tìm thấy đặt bàn";

		Ban ban = dat.getBan();
		String tinhTrang = ban.getTinhTrang();

		// ❌ Không cho hủy nếu đang phục vụ
		if ("Đang Phục Vụ".equalsIgnoreCase(tinhTrang)) {
			return "Không thể hủy: bàn đang phục vụ";
		}

		// Cho hủy nếu còn lại là Đã Đặt Trước hoặc có món
		dat.setDaXoa(true);
		datBanRepo.save(dat);

		// Cập nhật trạng thái bàn lại (nếu cần)
		banService.capNhatTrangThaiBan(ban.getId());

		return "Hủy đặt bàn thành công";
	}

	public Optional<DatBan> timTheoBan(Long banId) {
		return datBanRepo.findByBan_IdAndDaXoaFalse(banId);
	}
}
