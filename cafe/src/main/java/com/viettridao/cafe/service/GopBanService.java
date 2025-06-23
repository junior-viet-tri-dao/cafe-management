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
public class GopBanService {

	@Autowired
	private BanRepository banRepo;

	@Autowired
	private ThucDonRepository thucDonRepo;

	@Autowired
	private DatBanRepository datBanRepo;

	public void gopBan(Long banChinhId, List<Long> banPhuIds) {
		Ban banChinh = banRepo.findById(banChinhId).orElse(null);
		if (banChinh == null)
			return;

		List<ThucDon> monChinh = thucDonRepo.findByBan_IdAndDaXoaFalse(banChinhId);

		for (Long id : banPhuIds) {
			if (id.equals(banChinhId))
				continue;

			Ban banPhu = banRepo.findById(id).orElse(null);
			if (banPhu == null)
				continue;

			List<ThucDon> monPhu = thucDonRepo.findByBan_IdAndDaXoaFalse(id);
			for (ThucDon tdPhu : monPhu) {
				ThucDon trung = monChinh.stream().filter(m -> m.getTenMon().equalsIgnoreCase(tdPhu.getTenMon()))
						.findFirst().orElse(null);

				if (trung != null) {
					trung.setSoLuong(trung.getSoLuong() + tdPhu.getSoLuong());
					thucDonRepo.save(trung);
					tdPhu.setDaXoa(true);
					thucDonRepo.save(tdPhu);
				} else {
					tdPhu.setBan(banChinh);
					thucDonRepo.save(tdPhu);
					monChinh.add(tdPhu);
				}
			}

			// Chuyển đặt bàn nếu bàn phụ có mà bàn chính chưa có
			Optional<DatBan> datPhu = datBanRepo.findByBan_IdAndDaXoaFalse(banPhu.getId());
			Optional<DatBan> datChinh = datBanRepo.findByBan_IdAndDaXoaFalse(banChinh.getId());

			if (datPhu.isPresent() && datChinh.isEmpty()) {
				DatBan dat = datPhu.get();
				dat.setBan(banChinh);
				datBanRepo.save(dat);
			}

			// Cập nhật bàn phụ về Trống
			banPhu.setTinhTrang("Trống");
			banRepo.save(banPhu);
		}

		// Bàn chính chuyển về Đang Phục Vụ
		banChinh.setTinhTrang("Đang Phục Vụ");
		banRepo.save(banChinh);
	}
}
