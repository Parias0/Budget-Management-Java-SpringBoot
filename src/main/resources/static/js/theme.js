document.addEventListener("DOMContentLoaded", function () {
    const themeToggle = document.getElementById("themeToggle");
    const currentTheme = localStorage.getItem("theme") || "light";
    const body = document.documentElement;

    // Ustaw ikonę przycisku na podstawie aktualnego motywu
    function updateButtonIcon(theme) {
        themeToggle.innerHTML = theme === "dark" ?
            '<i class="bi bi-sun fs-3"></i>' : '<i class="bi bi-moon-stars fs-3"></i>';
    }

    // Ustaw zapisany motyw
    body.setAttribute("data-bs-theme", currentTheme);
    updateButtonIcon(currentTheme);

    themeToggle.addEventListener("click", function () {
        const newTheme = body.getAttribute("data-bs-theme") === "light" ? "dark" : "light";
        body.setAttribute("data-bs-theme", newTheme);
        localStorage.setItem("theme", newTheme);
        updateButtonIcon(newTheme);
    });
});
