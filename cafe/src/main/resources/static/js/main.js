// Initialize Lucide icons when page loads
document.addEventListener("DOMContentLoaded", function () {
  // Initialize Lucide icons
  if (typeof lucide !== "undefined") {
    lucide.createIcons();
  }

  // Add any other initialization code here
  console.log("Café Management System loaded successfully!");
});

// Optional: Add smooth page transitions
document.addEventListener("DOMContentLoaded", function () {
  // Add loading animation for navigation links
  const navLinks = document.querySelectorAll(".sidebar-item");

  navLinks.forEach((link) => {
    link.addEventListener("click", function (e) {
      // Add loading state
      const icon = this.querySelector("i");
      const originalIcon = icon.getAttribute("data-lucide");

      // Show loading spinner briefly
      icon.setAttribute("data-lucide", "loader-2");
      icon.classList.add("animate-spin");
      lucide.createIcons();

      // Allow navigation to proceed normally
      setTimeout(() => {
        // This will be reset when the new page loads
      }, 100);
    });
  });
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
  }
}

function showEmployeeTab(subtab) {
  // Hide all employee tab contents
  const employeeContents = document.querySelectorAll(".employee-tab-content");
  employeeContents.forEach((content) => content.classList.add("hidden"));

  // Show selected employee tab content
  const selectedContent = document.getElementById(`employee-${subtab}-content`);
  if (selectedContent) {
    selectedContent.classList.remove("hidden");
  }

  // Update tab button states
  const tabButtons = document.querySelectorAll(".employee-tab-btn");
  tabButtons.forEach((btn) => {
    btn.classList.remove(
      "active",
      "text-purple-600",
      "border-b-2",
      "border-purple-600"
    );
    btn.classList.add("text-gray-500", "hover:text-gray-700");
  });

  // Add active class to current tab
  const activeButton = document.querySelector(
    `[onclick="showEmployeeTab('${subtab}')"]`
  );
  if (activeButton) {
    activeButton.classList.add(
      "active",
      "text-purple-600",
      "border-b-2",
      "border-purple-600"
    );
    activeButton.classList.remove("text-gray-500", "hover:text-gray-700");
  }
}

// Initialize employee tabs when employees tab is loaded
document.addEventListener("DOMContentLoaded", function () {
  // Initialize employee tabs if on employees page
  const employeeContent = document.querySelector(
    "[th\\:if=\"${activeTab == 'employees'}\"]"
  );
  if (employeeContent && !employeeContent.classList.contains("hidden")) {
    showEmployeeTab("list");
  }
});

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
