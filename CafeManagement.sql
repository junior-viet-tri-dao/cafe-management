USE master;
GO
ALTER DATABASE CafeManagementSDG4 SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
GO
DROP DATABASE CafeManagementSDG4
GO

CREATE DATABASE CafeManagementSDG4
GO
USE CafeManagementSDG4
GO



INSERT INTO dbo.chuc_vu (ma_chuc_vu, ten_chuc_vu, luong) VALUES
(1, N'Quản Lý', 15000000),
(2, N'Trưởng Ca', 10000000),
(3, N'Pha Chế', 8500000),
(4, N'Phục Vụ', 6500000),
(5, N'Thu Ngân', 7000000),
(6, N'Bảo Vệ', 6000000),
(7, N'Tạp Vụ', 5500000),
(8, N'Marketing', 9000000),
(9, N'Kế Toán', 9500000),
(10, N'Phụ Bếp', 6000000);

-- 2. Dữ liệu cho bảng `ban`
INSERT INTO dbo.ban (ma_ban, ten_ban, tinh_trang) VALUES
(1, N'Bàn 1', 'Ranh'),
(2, N'Bàn 2', 'Ranh'),
(3, N'Bàn 3', 'Ranh'),
(4, N'Bàn 4', 'Ranh'),
(5, N'Bàn 5', 'Ranh'),
(6, N'Bàn 6', 'Ranh'),
(7, N'Bàn 7', 'Ranh'),
(8, N'Bàn 8', 'Ranh'),
(9, N'Bàn 9', 'Ranh'),
(10, N'Bàn 10 VIP', 'Ranh');

- 3. Dữ liệu cho bảng `don_vi_tinh`
INSERT INTO dbo.don_vi_tinh (ma_don_vi_tinh, ten_don_vi) VALUES
(1, N'Kg'),
(2, N'Gram'),
(3, N'Lít'),
(4, N'ml'),
(5, N'Chai'),
(6, N'Hộp'),
(7, N'Túi'),
(8, N'Cái'),
(9, N'Quả'),
(10, N'Thùng');

-- 4. Dữ liệu cho bảng `khuyen_mai`
-- (ma_khuyen_mai là IDENTITY, không cần chèn)
INSERT INTO dbo.khuyen_mai (ten_khuyen_mai, loai_khuyen_mai, gia_tri_giam, ngay_bat_dau, ngay_ket_thuc) VALUES
(N'Giảm giá 10% tổng hóa đơn', N'Phần trăm', 0.1, '2025-06-01', '2025-06-30'),
(N'Mua 2 tặng 1 cho dòng trà', N'Sản phẩm', 1, '2025-07-01', '2025-07-15'),
(N'Happy Hour (14h-16h)', N'Phần trăm', 0.2, '2025-06-15', '2025-08-15'),
(N'Giảm 20k cho hóa đơn trên 100k', N'Giá cố định', 20000, '2025-06-20', '2025-07-20'),
(N'Ngày của phở - Đồng giá 29k', N'Giá cố định', 29000, '2025-09-09', '2025-09-09'),
(N'Chào hè rực rỡ', N'Phần trăm', 0.15, '2025-06-01', '2025-08-31'),
(N'Miễn phí ship', N'Giá cố định', 0, '2025-01-01', '2025-12-31'),
(N'Đi 4 tính tiền 3', N'Sản phẩm', 1, '2025-07-01', '2025-07-31'),
(N'Check-in giảm ngay 5%', N'Phần trăm', 0.05, '2025-01-01', '2025-12-31'),
(N'Không có khuyến mãi', N'Không', 0, '2000-01-01', '2999-12-31');

-- 5. Dữ liệu cho bảng `thiet_bi`
INSERT INTO dbo.thiet_bi (ma_thiet_bi, ten_thiet_bi, so_luong, don_gia_mua, ngay_nhap, ghi_chu) VALUES
(1, N'Máy pha cà phê Faema E71', 2, 150000000, '2024-01-15', N'Bảo hành 2 năm'),
(2, N'Máy xay cà phê Mahlkönig EK43', 2, 60000000, '2024-01-15', N''),
(3, N'Máy xay sinh tố Vitamix', 3, 25000000, '2024-02-01', N'Công suất lớn'),
(4, N'Tủ lạnh công nghiệp Berjaya', 2, 45000000, '2024-01-10', N'4 cánh'),
(5, N'Lò vi sóng Sharp', 1, 3500000, '2024-03-05', NULL),
(6, N'Bàn gỗ sồi 4 người', 10, 2500000, '2024-01-20', N''),
(7, N'Ghế gỗ bọc da', 40, 800000, '2024-01-20', N''),
(8, N'Hệ thống POS iPOS', 1, 30000000, '2024-03-01', N'Gói phần mềm 3 năm'),
(9, N'Dàn loa Marshall', 1, 15000000, '2024-03-10', N''),
(10, N'Điều hòa Daikin 24000BTU', 4, 22000000, '2024-04-01', N'Lắp đặt tại các góc quán');


-- 6. Dữ liệu cho bảng `thuc_don`
-- (ma_thuc_don là IDENTITY, không cần chèn)
INSERT INTO dbo.thuc_don (ten_mon, loai_mon, gia_tien_hien_tai, is_deleted) VALUES
(N'Cà phê Đen', N'Cà phê', 29000, 0),
(N'Cà phê Sữa', N'Cà phê', 35000, 0),
(N'Bạc Xỉu', N'Cà phê', 39000, 0),
(N'Espresso', N'Cà phê', 35000, 0),
(N'Trà Đào Cam Sả', N'Trà', 45000, 0),
(N'Trà Vải', N'Trà', 45000, 0),
(N'Nước ép Ổi', N'Nước ép', 40000, 0),
(N'Sinh tố Bơ', N'Sinh tố', 50000, 0),
(N'Bánh Tiramisu', N'Bánh ngọt', 35000, 0),
(N'Bánh Mousse Chanh dây', N'Bánh ngọt', 35000, 0);


-- =============================================================================
-- BẢNG CÓ KHÓA NGOẠI (PHỤ THUỘC)
-- =============================================================================

-- 7. Dữ liệu cho bảng `hang_hoa`
INSERT INTO dbo.hang_hoa (ma_hang_hoa, ten_hang_hoa, so_luong, don_gia, ma_don_vi_tinh) VALUES
(1, N'Hạt cà phê Robusta', 50, 150000, 1),
(2, N'Hạt cà phê Arabica', 50, 250000, 1),
(3, N'Sữa đặc Ông Thọ', 100, 25000, 6),
(4, N'Sữa tươi Vinamilk', 20, 40000, 6),
(5, N'Đào ngâm', 30, 80000, 6),
(6, N'Trà túi lọc Lipton', 5, 50000, 10),
(7, N'Siro Vải', 10, 120000, 5),
(8, N'Chanh tươi không hạt', 15, 30000, 1),
(9, N'Bơ sáp Đắk Lắk', 20, 60000, 1),
(10, N'Bột phô mai Mascarpone', 5, 180000, 6);

-- 8. Dữ liệu cho bảng `users`
-- (ma_nhan_vien là IDENTITY, không cần chèn)
INSERT INTO dbo.users (ho_ten, gioi_tinh, user_name, password, email, so_dien_thoai, dia_chi, cmnd, ma_chuc_vu, role, is_deleted) VALUES
(N'Nguyễn Văn An', 'Male', 'an_nguyen', '123456', 'an.nguyen@email.com', '0905111222', N'123 Lê Lợi, Đà Nẵng', '201888999', 1, 'Admin', 0),
(N'Trần Thị Bình', 'Female', 'binh_tran', '123456', 'binh.tran@email.com', '0913333444', N'45 Phan Châu Trinh, Đà Nẵng', '201777666', 2, 'Employee', 0),
(N'Lê Minh Cường', 'Male', 'cuong_le', '123456', 'cuong.le@email.com', '0989555666', N'78 Hùng Vương, Đà Nẵng', '201555444', 3, 'Employee', 0),
(N'Phạm Thị Dung', 'Female', 'dung_pham', '123456', 'dung.pham@email.com', '0979777888', N'322 Ông Ích Khiêm, Đà Nẵng', '201444333', 4, 'Employee', 0),
(N'Võ Thành Long', 'Male', 'long_vo', '123456', 'long.vo@email.com', '0935999000', N'12 Nguyễn Văn Linh, Đà Nẵng', '201222111', 5, 'Employee', 0),
(N'Hoàng Thị Giang', 'Female', 'giang_hoang', '123456', 'giang.hoang@email.com', '0905123456', N'210 Trần Phú, Đà Nẵng', '206789123', 4, 'Employee', 0),
(N'Đặng Minh Hùng', 'Male', 'hung_dang', '123456', 'hung.dang@email.com', '0918234567', N'55 Hoàng Diệu, Đà Nẵng', '205678123', 3, 'Employee', 0),
(N'Bùi Thúy Kiều', 'Female', 'kieu_bui', '123456', 'kieu.bui@email.com', '0947345678', N'90 Trưng Nữ Vương, Đà Nẵng', '204567123', 5, 'Employee', 0),
(N'Ngô Gia Phúc', 'Male', 'phuc_ngo', '123456', 'phuc.ngo@email.com', '0966456789', N'34 Pasteur, Đà Nẵng', '203456123', 6, 'Employee', 0),
(N'Lý Mỹ Tâm', 'Female', 'tam_ly', '123456', 'tam.ly@email.com', '0988567890', N'180 Nguyễn Chí Thanh, Đà Nẵng', '202345123', 9, 'Employee', 0);



-- 11. Dữ liệu cho bảng `chi_tiet_thuc_don` (Công thức)
INSERT INTO dbo.chi_tiet_thuc_don (ma_thuc_don, ma_hang_hoa, khoi_luong, don_vi_tinh) VALUES
(1, 1, 25, 'Gram'), -- Cà phê Đen = Hạt Robusta
(2, 1, 25, 'Gram'), -- Cà phê Sữa = Hạt Robusta
(2, 3, 30, 'ml'), -- Cà phê Sữa = Sữa đặc
(3, 2, 20, 'Gram'), -- Bạc Xỉu = Hạt Arabica
(3, 3, 40, 'ml'), -- Bạc Xỉu = Sữa đặc
(3, 4, 20, 'ml'), -- Bạc Xỉu = Sữa tươi
(5, 5, 50, 'Gram'), -- Trà Đào Cam Sả = Đào ngâm
(5, 6, 1, N'Túi'), -- Trà Đào Cam Sả = Trà túi lọc
(8, 9, 100, 'Gram'), -- Sinh tố Bơ = Bơ sáp
(10, 10, 50, 'Gram'); -- Bánh Mousse = Bột phô mai

-- 12. Dữ liệu cho bảng `chi_tieu`
INSERT INTO dbo.chi_tieu (ma_chi_tieu, ma_nhan_vien, ngay_chi, so_tien, ten_khoan_chi) VALUES
(1, 1, '2025-06-25', 5000000, N'Trả tiền điện tháng 5'),
(2, 1, '2025-06-25', 1000000, N'Trả tiền nước tháng 5'),
(3, 9, '2025-06-20', 3000000, N'Mua văn phòng phẩm'),
(4, 2, '2025-06-15', 500000, N'Sửa chữa máy xay'),
(5, 1, '2025-06-10', 10000000, N'Tạm ứng lương nhân viên'),
(6, 9, '2025-06-05', 2500000, N'Chi phí quảng cáo Facebook'),
(7, 1, '2025-05-30', 750000, N'Mua đồng phục mới'),
(8, 2, '2025-05-28', 1200000, N'Tổ chức tiệc nhỏ cho nhân viên'),
(9, 1, '2025-05-25', 300000, N'Tiền rác'),
(10, 1, '2025-05-20', 2000000, N'Trả tiền internet tháng 5');

-- 13. Dữ liệu cho bảng `don_nhap`
INSERT INTO dbo.don_nhap (ma_don_nhap, ngay_nhap, so_luong, tong_tien, ma_nhan_vien, ma_hang_hoa) VALUES
('DN001', '2025-06-20', 20, 3000000, 1, 1),
('DN002', '2025-06-20', 20, 5000000, 1, 2),
('DN003', '2025-06-21', 50, 1250000, 2, 3),
('DN004', '2025-06-22', 10, 800000, 2, 5),
('DN005', '2025-06-23', 5, 250000, 7, 6),
('DN006', '2025-06-24', 5, 600000, 7, 7),
('DN007', '2025-06-25', 10, 300000, 7, 8),
('DN008', '2025-06-26', 15, 900000, 7, 9),
('DN009', '2025-06-27', 5, 900000, 1, 10),
('DN010', '2025-06-27', 10, 400000, 2, 4);

-- 14. Dữ liệu cho bảng `don_xuat` (Xuất kho để sử dụng)
INSERT INTO dbo.don_xuat (ma_don_xuat, ngay_xuat, so_luong, ma_nhan_vien, ma_hang_hoa) VALUES
(1, '2025-06-26', 2, 3, 1),
(2, '2025-06-26', 5, 3, 3),
(3, '2025-06-27', 1, 7, 9),
(4, '2025-06-27', 1, 7, 5),
(5, '2025-06-27', 1, 3, 2),
(6, '2025-06-27', 3, 3, 4),
(7, '2025-06-28', 2, 7, 8),
(8, '2025-06-28', 1, 3, 7),
(9, '2025-06-28', 2, 3, 10),
(10, '2025-06-28', 1, 7, 6);


--- chưa chèn

-- 15. Dữ liệu cho bảng `chi_tiet_dat_ban`
INSERT INTO dbo.chi_tiet_dat_ban (ma_ban, ma_hoa_don, ma_nhan_vien, ten_khach_hang, sdt_khach_hang, ngay_gio_dat) VALUES
(5, 4, 4, N'Anh Tuấn', '0987654321', '2025-06-28 19:00:00'),
(10, 7, 5, N'Chị Mai (Công ty ABC)', '0912345678', '2025-06-29 12:00:00'),
(2, 10, 4, N'Gia đình chị Hà', '0909876543', '2025-06-30 18:30:00'),
-- Giả sử các hóa đơn còn lại không phải là đặt bàn trước mà là khách vãng lai
-- Để đủ 10 dòng, có thể thêm các bản ghi đặt bàn khác trong tương lai
(1, 1, 2, N'Khách vãng lai', 0987675670, '2025-06-26 08:15:00'),
(3, 2, 4, N'Khách vãng lai', 0987675671, '2025-06-26 10:30:00'),
(7, 3, 6, N'Khách vãng lai', 0987675672, '2025-06-26 14:15:00'),
(8, 5, 2, N'Khách vãng lai', 0987675673, '2025-06-27 09:10:00'),
(9, 6, 4, N'Khách vãng lai', 0987675674, '2025-06-27 11:45:00'),
(1, 8, 6, N'Khách vãng lai', 0987675675, '2025-06-27 16:30:00'),
(3, 9, 2, N'Khách vãng lai', 0987675676, '2025-06-27 17:00:00');


-- 9. Dữ liệu cho bảng `hoa_don`
-- (ma_hoa_don là IDENTITY, không cần chèn)
-- tong_tien có thể để NULL hoặc 0 và sẽ được cập nhật sau khi thêm chi tiết hóa đơn
INSERT INTO dbo.hoa_don (ngay_gio_tao, trang_thai, tong_tien, ma_khuyen_mai) VALUES
(GETDATE(), 1, 84000, 10), -- HD1
('2025-06-26 10:30:00', 1, 129000, 10), -- HD2
('2025-06-26 14:15:00', 1, 72000, 3), -- HD3 (KM Happy Hour)
('2025-06-27 08:00:00', 0, 0, NULL), -- HD4
('2025-06-27 09:10:00', 1, 95000, 10), -- HD5
('2025-06-27 11:45:00', 1, 120000, 4), -- HD6 (KM giảm 20k)
('2025-06-27 15:00:00', 0, 0, 3), -- HD7
('2025-06-27 16:30:00', 1, 70000, 10), -- HD8
('2025-06-27 17:00:00', 1, 70000, 9), -- HD9 (KM check-in)
('2025-06-27 18:00:00', 0, 0, NULL); -- HD10

-- 10. Dữ liệu cho bảng `chi_tiet_hoa_don`
INSERT INTO dbo.chi_tiet_hoa_don (ma_hoa_don, ma_thuc_don, so_luong, gia_tai_thoi_diem_ban, thanh_tien) VALUES
(1, 2, 1, 35000, 35000),
(1, 5, 1, 45000, 45000),
(2, 3, 2, 39000, 78000),
(2, 1, 1, 29000, 29000),
(3, 8, 1, 50000, 50000),
(3, 9, 1, 35000, 35000),
(4, 6, 2, 45000, 90000), -- Hóa đơn chưa thanh toán
(5, 7, 1, 40000, 40000),
(5, 10, 1, 35000, 35000),
(6, 4, 1, 35000, 35000),
(6, 5, 2, 45000, 90000);