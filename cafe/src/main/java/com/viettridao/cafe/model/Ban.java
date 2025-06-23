package com.viettridao.cafe.model;

import java.util.List;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Ban {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Nationalized
	private String tenBan;

	@Nationalized
	private String tinhTrang;
	
	@ManyToOne
	@JoinColumn(name = "nhanvien_id")
	private NhanVien nhanVien;

	private boolean daXoa = false;
	
	@OneToMany(mappedBy = "ban", cascade = CascadeType.ALL)
    private List<ThucDon> thucDon;

  
    public void setThucDon(List<ThucDon> thucDon) {
		this.thucDon = thucDon;
	}
	public List<ThucDon> getThucDon() {
        return thucDon;
    }
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTenBan() {
		return tenBan;
	}

	public void setTenBan(String tenBan) {
		this.tenBan = tenBan;
	}

	public String getTinhTrang() {
		return tinhTrang;
	}

	public void setTinhTrang(String tinhTrang) {
		this.tinhTrang = tinhTrang;
	}

	public boolean isDaXoa() {
		return daXoa;
	}

	public void setDaXoa(boolean daXoa) {
		this.daXoa = daXoa;
	}

}
