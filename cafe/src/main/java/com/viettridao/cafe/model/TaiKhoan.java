package com.viettridao.cafe.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TaiKhoan")
public class TaiKhoan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer maTaiKhoan;

	@Column(nullable = false, unique = true)
	private String tenDangNhap;

	@Column(nullable = false)
	private String matKhau;

	private String quyenHan;

	private String anh;

	public Integer getMaTaiKhoan() {
		return maTaiKhoan;
	}

	public void setMaTaiKhoan(Integer maTaiKhoan) {
		this.maTaiKhoan = maTaiKhoan;
	}

	public String getTenDangNhap() {
		return tenDangNhap;
	}

	public void setTenDangNhap(String tenDangNhap) {
		this.tenDangNhap = tenDangNhap;
	}

	public String getMatKhau() {
		return matKhau;
	}

	public void setMatKhau(String matKhau) {
		this.matKhau = matKhau;
	}

	public String getQuyenHan() {
		return quyenHan;
	}

	public void setQuyenHan(String quyenHan) {
		this.quyenHan = quyenHan;
	}

	public String getAnh() {
		return anh;
	}

	public void setAnh(String anh) {
		this.anh = anh;
	}

}
