function confirmDelete(id) {
  if (confirm("Bạn có chắc chắn muốn xóa món này không?")) {
    window.location.href = "/menu/delete/" + id;
  }
}
