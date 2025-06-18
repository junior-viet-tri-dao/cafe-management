// Initialize Lucide icons when page loads
document.addEventListener("DOMContentLoaded", function () {
  lucide.createIcons();
});

// Tab switching functionality
function showTab(tabName) {
  // Hide all tabs
  const tabs = document.querySelectorAll(".tab-content");
  tabs.forEach((tab) => tab.classList.add("hidden"));

  // Show selected tab
  const targetTab = document.getElementById(tabName + "-tab");
  if (targetTab) {
    targetTab.classList.remove("hidden");
  }

  // Reset ALL sidebar items (including main sidebar and employee submenu)
  resetAllSidebarItems();

  // Set active main sidebar item
  if (event && event.target) {
    const sidebarItem = event.target.closest(".sidebar-item");
    if (sidebarItem) {
      sidebarItem.classList.add("bg-purple-200", "active");
      sidebarItem.classList.remove("hover:bg-purple-100");
    }
  }

  // Close employee submenu when switching to other tabs
  closeEmployeeMenu();
}

// Employee submenu functions
function toggleEmployeeMenu() {
  const submenu = document.getElementById("employee-submenu");
  const chevron = document.getElementById("employee-chevron");

  if (submenu.classList.contains("hidden")) {
    submenu.classList.remove("hidden");
    chevron.style.transform = "rotate(90deg)";

    // Reset all sidebar items when opening employee menu
    resetAllSidebarItems();

    // Set employee menu button as active
    const employeeButton = chevron.closest(".sidebar-item");
    if (employeeButton) {
      employeeButton.classList.add("bg-purple-200", "active");
      employeeButton.classList.remove("hover:bg-purple-100");
    }

    // Show employee list by default
    showEmployeeTab("list");
  } else {
    submenu.classList.add("hidden");
    chevron.style.transform = "rotate(0deg)";
  }
}

function closeEmployeeMenu() {
  const submenu = document.getElementById("employee-submenu");
  const chevron = document.getElementById("employee-chevron");

  if (submenu && !submenu.classList.contains("hidden")) {
    submenu.classList.add("hidden");
    chevron.style.transform = "rotate(0deg)";

    // Reset employee menu button state
    const employeeButton = chevron.closest(".sidebar-item");
    if (employeeButton) {
      employeeButton.classList.remove("bg-purple-200", "active");
      employeeButton.classList.add("hover:bg-purple-100");
    }
  }
}

function showEmployeeTab(subtab) {
  // Hide all main tabs first
  const tabs = document.querySelectorAll(".tab-content");
  tabs.forEach((tab) => tab.classList.add("hidden"));

  // Show employee management tab
  const employeeTab = document.getElementById("employees-tab");
  if (employeeTab) {
    employeeTab.classList.remove("hidden");
  }

  // Hide all employee subtabs
  const employeeSubtabs = document.querySelectorAll(".employee-subtab-content");
  employeeSubtabs.forEach((tab) => tab.classList.add("hidden"));

  // Show selected employee subtab based on the subtab parameter
  let targetSubtabId;
  switch (subtab) {
    case "list":
      targetSubtabId = "employee-list-tab";
      break;
    case "add":
      targetSubtabId = "employee-add-tab";
      break;
    case "edit":
      targetSubtabId = "employee-edit-tab";
      break;
    case "delete":
      targetSubtabId = "employee-delete-tab";
      break;
    case "search":
      targetSubtabId = "employee-search-tab";
      break;
    default:
      targetSubtabId = "employee-list-tab";
  }

  const targetSubtab = document.getElementById(targetSubtabId);
  if (targetSubtab) {
    targetSubtab.classList.remove("hidden");
  }

  // Reset ALL sidebar items (main + submenu)
  resetAllSidebarItems();

  // Set employee main menu as active
  const employeeMainButton = document.querySelector(
    'button[onclick="toggleEmployeeMenu()"]'
  );
  if (employeeMainButton) {
    employeeMainButton.classList.add("bg-purple-200", "active");
    employeeMainButton.classList.remove("hover:bg-purple-100");
  }

  // Set active submenu item
  if (event && event.target) {
    const submenuItem = event.target.closest(".employee-submenu-item");
    if (submenuItem) {
      submenuItem.classList.add("bg-purple-100", "active");
      submenuItem.classList.remove("hover:bg-purple-100");
    }
  }
}

// Helper function to reset all sidebar items
function resetAllSidebarItems() {
  // Reset main sidebar items
  const sidebarItems = document.querySelectorAll(".sidebar-item");
  sidebarItems.forEach((item) => {
    item.classList.remove("bg-purple-200", "active");
    item.classList.add("hover:bg-purple-100");
  });

  // Reset employee submenu items
  const submenuItems = document.querySelectorAll(".employee-submenu-item");
  submenuItems.forEach((item) => {
    item.classList.remove("bg-purple-100", "active");
    item.classList.add("hover:bg-purple-100");
  });
}

// Dashboard data management
const dashboardData = {
  employees: 12,
  menuItems: 25,
  todayRevenue: "2.5M",
  orders: 45,
};

// Update dashboard cards with real data
function updateDashboard() {
  // This will be implemented when connecting to backend
  console.log("Dashboard updated with:", dashboardData);
}

// Mobile menu toggle
function toggleMobileMenu() {
  const sidebar = document.querySelector(".sidebar");
  if (sidebar) {
    sidebar.classList.toggle("open");
  }
}

// Profile edit functionality
function editProfile() {
  alert("Tính năng chỉnh sửa hồ sơ đang được phát triển...");
}

// Initialize dashboard when page loads
document.addEventListener("DOMContentLoaded", function () {
  updateDashboard();

  // Add event listeners for profile buttons
  const editButton = document.querySelector(".profile-edit-btn");
  if (editButton) {
    editButton.addEventListener("click", editProfile);
  }
});
