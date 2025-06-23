package com.viettridao.cafe.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.hibernate.annotations.Nationalized;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class DatBan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Ban ban;

    @Nationalized
    private String tenKhach;

    private String sdt;

    private LocalDate ngay;

    private LocalTime gio;

    private boolean daXoa = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ban getBan() {
		return ban;
	}

	public void setBan(Ban ban) {
		this.ban = ban;
	}

	public String getTenKhach() {
		return tenKhach;
	}

	public void setTenKhach(String tenKhach) {
		this.tenKhach = tenKhach;
	}

	public String getSdt() {
		return sdt;
	}

	public void setSdt(String sdt) {
		this.sdt = sdt;
	}

	public LocalDate getNgay() {
		return ngay;
	}

	public void setNgay(LocalDate ngay) {
		this.ngay = ngay;
	}

	public LocalTime getGio() {
		return gio;
	}

	public void setGio(LocalTime gio) {
		this.gio = gio;
	}

	public boolean isDaXoa() {
		return daXoa;
	}

	public void setDaXoa(boolean daXoa) {
		this.daXoa = daXoa;
	}
}

