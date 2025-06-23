package com.viettridao.cafe.model;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class NhanVien {

	@Id
	@Column(name = "username", nullable = false)
	private String user;

	@Column(nullable = false)
	private String pass;

	@Column(nullable = false)
	@Nationalized
	private String hoVaTen;

	@Column(nullable = false)
	@Nationalized
	private String chucVu;

	@Column(nullable = false)
	@Nationalized
	private String diaChi;

	@Column(nullable = false)
	private String sdt;

	@Column(nullable = false)
	private Integer luong;
	
	@Enumerated(EnumType.STRING)
    private VaiTro vaiTro; 

	public VaiTro getVaiTro() {
		return vaiTro;
	}

	public void setVaiTro(VaiTro vaiTro) {
		this.vaiTro = vaiTro;
	}

	@Column(nullable = false)
	private Boolean daXoa = false;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getHoVaTen() {
		return hoVaTen;
	}

	public void setHoVaTen(String hoVaTen) {
		this.hoVaTen = hoVaTen;
	}

	public String getChucVu() {
		return chucVu;
	}

	public void setChucVu(String chucVu) {
		this.chucVu = chucVu;
	}

	public String getDiaChi() {
		return diaChi;
	}

	public void setDiaChi(String diaChi) {
		this.diaChi = diaChi;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public Integer getLuong() {
		return luong;
	}

	public void setLuong(Integer luong) {
		this.luong = luong;
	}

	public Boolean getDaXoa() {
		return daXoa;
	}

	public void setDaXoa(Boolean daXoa) {
		this.daXoa = daXoa;
	}
}