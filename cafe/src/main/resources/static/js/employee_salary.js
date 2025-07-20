
        function formatVND(number) {
            if (!number) return '';
            return Number(number).toLocaleString('vi-VN') + ' VNĐ';
        }

        function updateSalaryDisplay() {
            const select = document.getElementById('positionSelect');
            const salary = select.options[select.selectedIndex].getAttribute('data-luong');
            document.getElementById('salaryInput').value = salary || '';
            document.getElementById('salaryDisplay').textContent = formatVND(salary);
        }

        document.addEventListener('DOMContentLoaded', () => {
            document.getElementById('positionSelect').addEventListener('change', updateSalaryDisplay);
            updateSalaryDisplay();
        });