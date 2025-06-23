package com.viettridao.cafe.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettridao.cafe.model.Ban;
import com.viettridao.cafe.model.DatBan;
import com.viettridao.cafe.model.ThucDon;
import com.viettridao.cafe.repository.BanRepository;
import com.viettridao.cafe.repository.DatBanRepository;
import com.viettridao.cafe.repository.ThucDonRepository;

@Service
public class ChuyenBanService {

	@Autowired
	private BanRepository banRepo;

	@Autowired
	private ThucDonRepository thucDonRepo;

	@Autowired
	private DatBanRepository datBanRepo;

	public void chuyenBan(Long banCuId, Long banMoiId) {
		Ban banCu = banRepo.findById(banCuId).orElse(null);
		Ban banMoi = banRepo.findById(banMoiId).orElse(null);
		if (banCu == null || banMoi == null)
			return;

		// ✅ Trường hợp đặc biệt 1: Trống → Đã Đặt Trước (chuyển đặt bàn ngược)
		if ("Trống".equalsIgnoreCase(banCu.getTinhTrang()) && "Đã Đặt Trước".equalsIgnoreCase(banMoi.getTinhTrang())) {

			Optional<DatBan> datBan = datBanRepo.findByBan_IdAndDaXoaFalse(banMoiId);
			if (datBan.isPresent()) {
				DatBan dat = datBan.get();
				dat.setBan(banCu);
				datBanRepo.save(dat);

				banCu.setTinhTrang("Đã Đặt Trước");
				banMoi.setTinhTrang("Trống");

				banRepo.save(banCu);
				banRepo.save(banMoi);
				return;
			}
		}

		// ✅ Trường hợp đặc biệt 2: Trống → Đang Phục Vụ (chuyển món ngược)
		if ("Trống".equalsIgnoreCase(banCu.getTinhTrang()) && "Đang Phục Vụ".equalsIgnoreCase(banMoi.getTinhTrang())) {

			List<ThucDon> monMoi = thucDonRepo.findByBan_IdAndDaXoaFalse(banMoiId);
			if (!monMoi.isEmpty()) {
				for (ThucDon td : monMoi) {
					td.setBan(banCu);
					thucDonRepo.save(td);
				}

				banCu.setTinhTrang("Đang Phục Vụ");
				banMoi.setTinhTrang("Trống");

				banRepo.save(banCu);
				banRepo.save(banMoi);
				return;
			}
		}

		// ✅ Trường hợp chuẩn: chuyển món và/hoặc đặt bàn từ bànCu → bànMoi
		List<ThucDon> danhSachMon = thucDonRepo.findByBan_IdAndDaXoaFalse(banCuId);
		for (ThucDon td : danhSachMon) {
			td.setBan(banMoi);
			thucDonRepo.save(td);
		}

		// ✅ Chuyển đặt bàn nếu có
		Optional<DatBan> datBanCu = datBanRepo.findByBan_IdAndDaXoaFalse(banCuId);
		Optional<DatBan> datBanMoi = datBanRepo.findByBan_IdAndDaXoaFalse(banMoiId);
		if (datBanCu.isPresent() && datBanMoi.isEmpty()) {
			DatBan dat = datBanCu.get();
			dat.setBan(banMoi);
			datBanRepo.save(dat);
		}

		// ✅ Bàn cũ luôn → Trống
		banCu.setTinhTrang("Trống");

		// ✅ Bàn mới cập nhật theo dữ liệu thực tế
		boolean coMonMoi = thucDonRepo.findByBan_IdAndDaXoaFalse(banMoiId).size() > 0;
		boolean coDatMoi = datBanRepo.findByBan_IdAndDaXoaFalse(banMoiId).isPresent();

		if (coMonMoi) {
			banMoi.setTinhTrang("Đang Phục Vụ");
		} else if (coDatMoi) {
			banMoi.setTinhTrang("Đã Đặt Trước");
		} else {
			banMoi.setTinhTrang("Trống");
		}

		banRepo.save(banCu);
		banRepo.save(banMoi);
	}
}
