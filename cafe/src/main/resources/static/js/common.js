document.addEventListener('DOMContentLoaded', function() {
    // Password toggle functionality
    const passwordToggle = document.querySelector('.password-toggle');
    const passwordDisplay = document.querySelector('.password-display');
    
    if (passwordToggle && passwordDisplay) {
        passwordToggle.addEventListener('click', function() {
            const icon = this.querySelector('i');
            if (passwordDisplay.textContent === '••••••••') {
                passwordDisplay.textContent = 'vanthe123'; // Replace with actual password logic
                icon.className = 'fas fa-eye-slash';
            } else {
                passwordDisplay.textContent = '••••••••';
                icon.className = 'fas fa-eye';
            }
        });
    }

    // Sidebar hover effects
    const sidebarLinks = document.querySelectorAll('aside nav a');
    sidebarLinks.forEach(link => {
        link.addEventListener('mouseenter', function() {
            if (!this.classList.contains('bg-blue-500')) {
                this.style.transform = 'translateX(4px)';
            }
        });
        link.addEventListener('mouseleave', function() {
            if (!this.classList.contains('bg-blue-500')) {
                this.style.transform = 'translateX(0)';
            }
        });
    });

    // Animate cards on load
    const cards = document.querySelectorAll('.bg-white');
    cards.forEach((card, index) => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        setTimeout(() => {
            card.style.transition = 'all 0.6s ease';
            card.style.opacity = '1';
            card.style.transform = 'translateY(0)';
        }, index * 100);
    });
});