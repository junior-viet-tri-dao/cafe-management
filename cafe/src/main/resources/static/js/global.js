document.querySelectorAll('.btn-delete').forEach(button => {
    button.addEventListener('click', function () {
        const id = this.getAttribute('data-id');
        Swal.fire({
            title: 'Xác nhận xoá?',
            text: 'Bạn có chắc muốn xóa không?',
            icon: 'question',
            showCancelButton: true,
            confirmButtonText: 'Có',
            cancelButtonText: 'Không',
            confirmButtonColor: '#16a34a',
            cancelButtonColor: '#ef4444'
        }).then((result) => {
            if (result.isConfirmed) {
                document.getElementById('deleteForm-' + id).submit();
            }
        });
    });
});