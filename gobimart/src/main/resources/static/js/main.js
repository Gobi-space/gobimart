/**
 * GOBIMART - Client-side UI enhancements
 * Note: Core business logic remains strictly on the Java / Spring Boot backend.
 */

document.addEventListener('DOMContentLoaded', function () {
    // Mobile navigation toggle
    const mobileToggle = document.getElementById('mobileNavToggle');
    const navLinks = document.getElementById('navLinks');

    if (mobileToggle && navLinks) {
        mobileToggle.addEventListener('click', function () {
            navLinks.classList.toggle('active');
        });
    }

    // Auto-dismiss alert notifications after 5 seconds if desired
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function () {
                if (alert.parentNode) {
                    alert.parentNode.removeChild(alert);
                }
            }, 500);
        }, 5000);
    });
});

/**
 * Confirm action dialog helper
 */
function confirmAction(message) {
    return confirm(message || 'Are you sure you want to proceed?');
}
