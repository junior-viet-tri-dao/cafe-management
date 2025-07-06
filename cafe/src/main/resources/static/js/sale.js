
        window.addEventListener('load', function () {
                  const viewTableBtn = document.getElementById('viewTableBtn'); // Nút "Xem bàn"
                  const viewTableModal = document.getElementById('viewTableModal'); // Toàn bộ popup
                  const closeModalBtn = document.getElementById('closeModalBtn'); // Nút "Đóng" trong popup
        })



        // Khi nút "Xem bàn" được click
        viewTableBtn.addEventListener('click', () => {
            viewTableModal.classList.add('active'); // Thêm lớp 'active' để hiển thị popup
            document.body.style.overflow = 'hidden'; // Ngăn cuộn trang chính
        });

        // Khi nút "Đóng" trong popup được click
        closeModalBtn.addEventListener('click', () => {
            viewTableModal.classList.remove('active'); // Xóa lớp 'active' để ẩn popup
            document.body.style.overflow = ''; // Cho phép cuộn trang chính trở lại
        });

        // Khi người dùng click ra ngoài popup (lớp nền mờ)
        window.addEventListener('click', (event) => {
            if (event.target == viewTableModal) {
                viewTableModal.classList.remove('active'); // Xóa lớp 'active' để ẩn popup
                document.body.style.overflow = ''; // Cho phép cuộn trang chính trở lại
            }
        });