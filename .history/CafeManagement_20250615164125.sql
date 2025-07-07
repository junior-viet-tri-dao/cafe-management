USE master;
GO
ALTER DATABASE MOCKFINAL SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
GO
DROP DATABASE MOCKFINAL
GO

CREATE DATABASE MOCKFINAL
GO
USE MOCKFINAL
GO

select * from users

delete from users where username = 'admin'

INSERT INTO users (image, address, date_of_birth, email, full_name, gender, identity_card, password, phone, role, username)
VALUES (
		'uploads\965d1be0b8f98b6de2009a4945cfb598.jpg',
		'Viet Nam', 
		'2001-06-15', 
		'admin@gmail.com', 
		'Admin', 
		'Male',
		'123', 
		'$2a$10$zgHpCrJ1STeJO0i8jb5gRuC/DN5JMvvTWVBBiEdDE5TiHx2LTj7/G', 
		'1233456789', 
		'Admin', 
		'admin')


		INSERT INTO VACCINE_TYPE (VACCINE_TYPE_ID, DESCRIPTION, VACCINE_TYPE_NAME, ACTIVE)
VALUES
('VT1', N'Vaccine phòng Covid-19', N'Vaccine Type 1', 1),
('VT2', N'Vaccine phòng Cúm mùa', N'Vaccine Type 2', 1),
('VT3', N'Vaccine phòng Viêm gan B', N'Vaccine Type 3', 1),
('VT4', N'Vaccine phòng Uốn ván', N'Vaccine Type 4', 1),
('VT5', N'Vaccine phòng Sởi', N'Vaccine Type 5', 1),
('VT6', N'Vaccine phòng Bạch hầu', N'Vaccine Type 6', 1),
('VT7', N'Vaccine phòng Thủy đậu', N'Vaccine Type 7', 1),
('VT8', N'Vaccine phòng Rubella', N'Vaccine Type 8', 1),
('VT9', N'Vaccine phòng Sốt xuất huyết', N'Vaccine Type 9', 1),
('VT10', N'Vaccine phòng Tiêu chảy', N'Vaccine Type 10', 1),
('VT11', N'Vaccine phòng Bệnh dại', N'Vaccine Type 11', 1),
('VT12', N'Vaccine phòng Phế cầu', N'Vaccine Type 12', 1),
('VT13', N'Vaccine phòng Viêm màng não', N'Vaccine Type 13', 1),
('VT14', N'Vaccine phòng Sởi – Quai bị', N'Vaccine Type 14', 1),
('VT15', N'Vaccine phòng Cúm gia cầm', N'Vaccine Type 15', 1),
('VT16', N'Vaccine phòng HPV', N'Vaccine Type 16', 1),
('VT17', N'Vaccine phòng Viêm gan A', N'Vaccine Type 17', 1),
('VT18', N'Vaccine phòng Bạch hầu', N'Vaccine Type 18', 1),
('VT19', N'Vaccine phòng Thủy đậu', N'Vaccine Type 19', 1),
('VT20', N'Vaccine phòng Dại', N'Vaccine Type 20', 1);


INSERT INTO VACCINE (VACCINE_ID, STATUS, CONTRAINDICATION, INDICATION, NUMBER_OF_INJECTION, ORIGIN, TIME_BEGIN_NEXT_INJECTION, TIME_END_NEXT_INJECTION, USAGE, VACCINE_NAME, VACCINE_TYPE_ID)
VALUES
('V1', 1, N'Không tiêm cho phụ nữ mang thai', N'Tiêm cho người lớn', 2, N'USA', '2023-01-01', '2024-01-01', N'Tiêm bắp', N'Vaccine 1', 'VT1'),
('V2', 1, N'Không tiêm cho trẻ em dưới 6 tháng', N'Tiêm cho trẻ em', 1, N'Japan', '2023-01-02', '2024-01-02', N'Tiêm dưới da', N'Vaccine 2', 'VT2'),
('V3', 1, N'Tránh tiêm khi bị dị ứng', N'Tiêm cho người lớn', 2, N'Germany', '2023-01-03', '2024-01-03', N'Tiêm bắp', N'Vaccine 3', 'VT3'),
('V4', 1, N'Không tiêm cho người có bệnh mãn tính', N'Tiêm cho người lớn', 1, N'France', '2023-01-04', '2024-01-04', N'Tiêm dưới da', N'Vaccine 4', 'VT4'),
('V5', 1, N'Không tiêm cho người có bệnh tim', N'Tiêm cho trẻ em', 2, N'USA', '2023-01-05', '2024-01-05', N'Tiêm bắp', N'Vaccine 5', 'VT5'),
('V6', 1, N'Tránh tiêm cho người có tiền sử sốc phản vệ', N'Tiêm cho người lớn', 1, N'China', '2023-01-06', '2024-01-06', N'Tiêm dưới da', N'Vaccine 6', 'VT6'),
('V7', 1, N'Không tiêm cho người có tiền sử co giật', N'Tiêm cho người lớn', 2, N'USA', '2023-01-07', '2024-01-07', N'Tiêm bắp', N'Vaccine 7', 'VT7'),
('V8', 1, N'Tránh tiêm khi đang mang thai', N'Tiêm cho trẻ em', 1, N'Japan', '2023-01-08', '2024-01-08', N'Tiêm dưới da', N'Vaccine 8', 'VT8'),
('V9', 1, N'Không tiêm khi đang sốt cao', N'Tiêm cho người lớn', 2, N'Germany', '2023-01-09', '2024-01-09', N'Tiêm bắp', N'Vaccine 9', 'VT9'),
('V10', 1, N'Không tiêm cho người có bệnh về gan', N'Tiêm cho người lớn', 1, N'France', '2023-01-10', '2024-01-10', N'Tiêm dưới da', N'Vaccine 10', 'VT10'),
('V11', 1, N'Tránh tiêm cho người có bệnh mãn tính', N'Tiêm cho trẻ em', 2, N'USA', '2023-01-11', '2024-01-11', N'Tiêm bắp', N'Vaccine 11', 'VT11'),
('V12', 1, N'Không tiêm cho người bị suy giảm miễn dịch', N'Tiêm cho người lớn', 1, N'China', '2023-01-12', '2024-01-12', N'Tiêm dưới da', N'Vaccine 12', 'VT12'),
('V13', 1, N'Không tiêm cho trẻ em dưới 3 tuổi', N'Tiêm cho người lớn', 2, N'Germany', '2023-01-13', '2024-01-13', N'Tiêm bắp', N'Vaccine 13', 'VT13'),
('V14', 1, N'Tránh tiêm cho người có tiền sử sốc phản vệ', N'Tiêm cho trẻ em', 1, N'France', '2023-01-14', '2024-01-14', N'Tiêm dưới da', N'Vaccine 14', 'VT14'),
('V15', 1, N'Tránh tiêm khi đang mang thai', N'Tiêm cho người lớn', 2, N'USA', '2023-01-15', '2024-01-15', N'Tiêm bắp', N'Vaccine 15', 'VT15'),
('V16', 1, N'Không tiêm cho người có bệnh mãn tính', N'Tiêm cho người lớn', 1, N'China', '2023-01-16', '2024-01-16', N'Tiêm dưới da', N'Vaccine 16', 'VT16'),
('V17', 1, N'Không tiêm cho người có tiền sử co giật', N'Tiêm cho trẻ em', 2, N'USA', '2023-01-17', '2024-01-17', N'Tiêm bắp', N'Vaccine 17', 'VT17'),
('V18', 1, N'Tránh tiêm khi đang sốt cao', N'Tiêm cho người lớn', 1, N'Japan', '2023-01-18', '2024-01-18', N'Tiêm dưới da', N'Vaccine 18', 'VT18'),
('V19', 1, N'Không tiêm cho người có bệnh về gan', N'Tiêm cho trẻ em', 2, N'Germany', '2023-01-19', '2024-01-19', N'Tiêm bắp', N'Vaccine 19', 'VT19'),
('V20', 1, N'Tránh tiêm khi đang mang thai', N'Tiêm cho người lớn', 1, N'France', '2023-01-20', '2024-01-20', N'Tiêm dưới da', N'Vaccine 20', 'VT20');





INSERT INTO USERS (ADDRESS, DATE_OF_BIRTH, EMAIL, FULL_NAME, IDENTITY_CARD, GENDER, PASSWORD, PHONE, USERNAME, ROLE)
VALUES
(N'123 Main St', '1990-05-12', 'user1@example.com', N'Nguyen Van A', '1234568789', 'Male', 'password1', '0987654321', 'user1', 'Customer'),
(N'456 Oak St', '1985-04-22', 'user2@example.com', N'Tran Thi B', '9876548321', 'Female', 'password2', '0987654322', 'user2', 'Employee'),
(N'789 Pine St', '1992-09-13', 'user3@example.com', N'Le Van C', '1928378465', 'Male', 'password3', '0987654323', 'user3', 'Admin'),
(N'101 Elm St', '1988-03-05', 'user4@example.com', N'Hoang Thi D', '5647387291', 'Female', 'password4', '0987654324', 'user4', 'Customer'),
(N'202 Maple St', '1995-06-15', 'user5@example.com', N'Pham Van E', '1092836746', 'Male', 'password5', '0987654325', 'user5', 'Employee'),
(N'303 Birch St', '1991-02-17', 'user6@example.com', N'Nguyen Thi F', '8765543210', 'Female', 'password6', '0987654326', 'user6', 'Customer'),
(N'404 Cedar St', '1993-07-29', 'user7@example.com', N'Le Van G', '2039483576', 'Male', 'password7', '0987654327', 'user7', 'Employee'),
(N'505 Spruce St', '1989-12-25', 'user8@example.com', N'Tran Thi H', '65733483920', 'Female', 'password8', '0987654328', 'user8', 'Customer'),
(N'606 Willow St', '1994-11-11', 'user9@example.com', N'Hoang Van I', '1122433445', 'Male', 'password9', '0987654329', 'user9', 'Employee'),
(N'707 Ash St', '1997-01-10', 'user10@example.com', N'Pham Thi J', '9988772665', 'Female', 'password10', '0987654330', 'user10', 'Customer'),
(N'808 Walnut St', '1990-08-23', 'user11@example.com', N'Nguyen Van K', '5647358920', 'Male', 'password11', '0987654331', 'user11', 'Admin'),
(N'909 Fir St', '1983-05-08', 'user12@example.com', N'Le Thi L', '9182736445', 'Female', 'password12', '0987654332', 'user12', 'Customer'),
(N'1010 Pine St', '1986-10-18', 'user13@example.com', N'Tran Van M', '8273642910', 'Male', 'password13', '0987654333', 'user13', 'Employee'),
(N'1111 Oak St', '1989-02-03', 'user14@example.com', N'Pham Thi N', '7364910928', 'Female', 'password14', '0987654334', 'user14', 'Customer'),
(N'1212 Maple St', '1993-04-19', 'user15@example.com', N'Nguyen Van O', '1828736495', 'Male', 'password15', '0987654335', 'user15', 'Employee'),
(N'1313 Elm St', '1987-11-30', 'user16@example.com', N'Le Thi P', '5647378192', 'Female', 'password16', '0987654336', 'user16', 'Customer'),
(N'1414 Birch St', '1991-03-22', 'user17@example.com', N'Tran Van Q', '8473662910', 'Male', 'password17', '0987654337', 'user17', 'Admin'),
(N'1515 Cedar St', '1988-09-09', 'user18@example.com', N'Pham Thi R', '1029384575', 'Female', 'password18', '0987654338', 'user18', 'Employee'),
(N'1616 Spruce St', '1996-06-06', 'user19@example.com', N'Nguyen Van S', '6758449302', 'Male', 'password19', '0987654339', 'user19', 'Customer'),
(N'1717 Willow St', '1985-01-01', 'user20@example.com', N'Hoang Thi T', '5637328190', 'Female', 'password20', '0987654340', 'user20', 'Employee'),
(N'1818 Ash St', '1990-07-14', 'user21@example.com', N'Le Van U', '9483726215', 'Male', 'password21', '0987654341', 'user21', 'Customer'),
(N'1919 Walnut St', '1994-02-20', 'user22@example.com', N'Pham Thi V', '3928117465', 'Female', 'password22', '0987654342', 'user22', 'Admin'),
(N'2020 Fir St', '1983-12-01', 'user23@example.com', N'Nguyen Van W', '8473622918', 'Male', 'password23', '0987654343', 'user23', 'Employee'),
(N'2121 Pine St', '1997-03-07', 'user24@example.com', N'Tran Thi X', '1928337465', 'Female', 'password24', '0987654344', 'user24', 'Customer'),
(N'2222 Oak St', '1989-06-15', 'user25@example.com', N'Le Van Y', '8374921205', 'Male', 'password25', '0987654345', 'user25', 'Employee'),
(N'2323 Maple St', '1991-08-11', 'user26@example.com', N'Hoang Thi Z', '3847562931', 'Female', 'password26', '0987654346', 'user26', 'Customer'),
(N'2424 Elm St', '1984-10-05', 'user27@example.com', N'Pham Van AA', '9384756204', 'Male', 'password27', '0987654347', 'user27', 'Admin'),
(N'2525 Birch St', '1993-12-22', 'user28@example.com', N'Nguyen Thi BB', '1928375645', 'Female', 'password28', '0987654348', 'user28', 'Customer'),
(N'2626 Cedar St', '1987-05-30', 'user29@example.com', N'Le Van CC', '8374652915', 'Male', 'password29', '0987654349', 'user29', 'Employee'),
(N'2727 Spruce St', '1994-01-13', 'user30@example.com', N'Tran Thi DD', '3948572616', 'Female', 'password30', '0987654350', 'user30', 'Customer'),
(N'2828 Willow St', '1996-02-18', 'user31@example.com', N'Pham Van EE', '2938471628', 'Male', 'password31', '0987654351', 'user31', 'Employee'),
(N'2929 Ash St', '1985-09-07', 'user32@example.com', N'Nguyen Thi FF', '4938275619', 'Female', 'password32', '0987654352', 'user32', 'Customer'),
(N'3030 Walnut St', '1992-11-25', 'user33@example.com', N'Le Van GG', '7283645918', 'Male', 'password33', '0987654353', 'user33', 'Admin'),
(N'3131 Fir St', '1990-08-09', 'user34@example.com', N'Tran Thi HH', '8493627187', 'Female', 'password34', '0987654354', 'user34', 'Employee'),
(N'3232 Pine St', '1997-12-19', 'user35@example.com', N'Pham Van II', '1928475636', 'Male', 'password35', '0987654355', 'user35', 'Customer'),
(N'3333 Oak St', '1989-01-05', 'user36@example.com', N'Nguyen Thi JJ', '8473628195', 'Female', 'password36', '0987654356', 'user36', 'Employee'),
(N'3434 Maple St', '1994-07-22', 'user37@example.com', N'Le Van KK', '8374652914', 'Male', 'password37', '0987654357', 'user37', 'Customer'),
(N'3535 Elm St', '1986-03-14', 'user38@example.com', N'Tran Thi LL', '3847569213', 'Female', 'password38', '0987654358', 'user38', 'Employee'),
(N'3636 Birch St', '1993-06-29', 'user39@example.com', N'Pham Van MM', '2948576312', 'Male', 'password39', '0987654359', 'user39', 'Customer'),
(N'3737 Cedar St', '1991-12-10', 'user40@example.com', N'Nguyen Thi NN', '4938276151', 'Female', 'password40', '0987654360', 'user40', 'Admin');

INSERT INTO INJECTION_RESULT (INJECTION_DATE, INJECTION_PLACE, NEXT_INJECTION_DATE, NUMBER_OF_INJECTION, PREVENTION, USER_ID, VACCINE_ID)
VALUES
('2024-01-01', N'Place A', '2025-01-01', 1, N'Covid-19', 1, 'V1'),
('2024-01-02', N'Place B', '2025-01-02', 2, N'Cúm mùa', 2, 'V2'),
('2024-01-03', N'Place C', '2025-01-03', 3, N'Viêm gan B', 3, 'V3'),
('2024-01-04', N'Place D', '2025-01-04', 1, N'Uốn ván', 4, 'V4'),
('2024-01-05', N'Place E', '2025-01-05', 2, N'Sởi', 5, 'V5'),
('2024-01-06', N'Place F', '2025-01-06', 3, N'Bạch hầu', 6, 'V6'),
('2024-01-07', N'Place G', '2025-01-07', 1, N'Thủy đậu', 6, 'V7'),
('2024-01-08', N'Place H', '2025-01-08', 2, N'Rubella', 8, 'V8'),
('2024-01-09', N'Place I', '2025-01-09', 3, N'Sốt xuất huyết', 9, 'V9'),
('2024-01-10', N'Place J', '2025-01-10', 1, N'Tiêu chảy', 10, 'V10'),
('2024-01-11', N'Place K', '2025-01-11', 2, N'Bệnh dại', 11, 'V11'),
('2024-01-12', N'Place L', '2025-01-12', 3, N'Phế cầu', 12, 'V12'),
('2024-01-13', N'Place M', '2025-01-13', 1, N'Viêm màng não', 13, 'V13'),
('2024-01-14', N'Place N', '2025-01-14', 2, N'Sởi – Quai bị', 14, 'V14'),
('2024-01-15', N'Place O', '2025-01-15', 3, N'Cúm gia cầm', 15, 'V15'),
('2024-01-16', N'Place P', '2025-01-16', 1, N'HPV', 16, 'V16'),
('2024-01-17', N'Place Q', '2025-01-17', 2, N'Covid-19', 17, 'V17'),
('2024-01-18', N'Place R', '2025-01-18', 3, N'Cúm mùa', 18, 'V18'),
('2024-01-19', N'Place S', '2025-01-19', 1, N'Viêm gan B', 19, 'V19'),
('2024-01-20', N'Place T', '2025-01-20', 2, N'Uốn ván', 20, 'V20');





INSERT INTO INJECTION_SCHEDULE (DESCRIPTION, END_DATE, PLACE, START_DATE, STATUS, NOTE, VACCINE_ID)
VALUES
(N'Lịch tiêm số 1', '2024-12-31', N'Place A', '2024-01-01', N'open', N'Ghi chú 1', 'V1'),
(N'Lịch tiêm số 2', '2024-5-31', N'Place B', '2024-01-02', N'closed', N'Ghi chú 2', 'V2'),
(N'Lịch tiêm số 3', '2024-12-31', N'Place C', '2024-01-03', N'open', N'Ghi chú 3', 'V3'),
(N'Lịch tiêm số 4', '2024-12-31', N'Place D', '2024-01-04', N'open', N'Ghi chú 4', 'V4'),
(N'Lịch tiêm số 5', '2024-12-31', N'Place E', '2024-01-05', N'open', N'Ghi chú 5', 'V5'),
(N'Lịch tiêm số 6', '2024-5-31', N'Place F', '2024-01-06', N'closed', N'Ghi chú 6', 'V6'),
(N'Lịch tiêm số 7', '2024-12-31', N'Place G', '2024-01-07', N'open', N'Ghi chú 7', 'V7'),
(N'Lịch tiêm số 8', '2024-12-31', N'Place H', '2024-01-08', N'open', N'Ghi chú 8', 'V8'),
(N'Lịch tiêm số 9', '2024-12-31', N'Place I', '2024-01-09', N'open', N'Ghi chú 9', 'V9'),
(N'Lịch tiêm số 10', '2024-12-31', N'Place J', '2024-01-10', N'open', N'Ghi chú 10', 'V10'),
(N'Lịch tiêm số 11', '2024-5-31', N'Place K', '2024-01-11', N'closed', N'Ghi chú 11', 'V11'),
(N'Lịch tiêm số 12', '2024-12-31', N'Place L', '2024-01-12', N'open', N'Ghi chú 12', 'V12'),
(N'Lịch tiêm số 13', '2024-12-31', N'Place M', '2024-01-13', N'open', N'Ghi chú 13', 'V13'),
(N'Lịch tiêm số 14', '2024-12-31', N'Place N', '2024-01-14', N'open', N'Ghi chú 14', 'V14'),
(N'Lịch tiêm số 15', '2024-12-31', N'Place O', '2024-01-15', N'open', N'Ghi chú 15', 'V15'),
(N'Lịch tiêm số 16', '2024-5-31', N'Place P', '2024-01-16', N'closed', N'Ghi chú 16', 'V16'),
(N'Lịch tiêm số 17', '2024-12-31', N'Place Q', '2024-01-17', N'open', N'Ghi chú 17', 'V17'),
(N'Lịch tiêm số 18', '2024-12-31', N'Place R', '2024-01-18', N'open', N'Ghi chú 18', 'V18'),
(N'Lịch tiêm số 19', '2024-12-31', N'Place S', '2024-01-19', N'open', N'Ghi chú 19', 'V19'),
(N'Lịch tiêm số 20', '2024-12-31', N'Place T', '2024-01-20', N'open', N'Ghi chú 20', 'V20');