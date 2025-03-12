document.addEventListener("DOMContentLoaded", function () {
    'use strict';

    // Pobranie wszystkich formularzy z klasą needs-validation
    const forms = document.querySelectorAll('.needs-validation');

    // Iteracja po formularzach i dodanie walidacji
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
});
