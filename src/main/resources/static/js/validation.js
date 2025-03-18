document.addEventListener("DOMContentLoaded", function () {
    'use strict';

    // Pobranie wszystkich formularzy z klasą needs-validation
    const forms = document.querySelectorAll('.needs-validation');

    // Iteracja po formularzach i dodanie walidacji
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            // Sprawdzenie ogólnej walidacji formularza
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }

            // Sprawdzenie, czy formularz to rejestracja i czy hasła się zgadzają
            const password = form.querySelector("#registerPassword");
            const confirmPassword = form.querySelector("#confirmPassword");

            if (password && confirmPassword) { // Tylko w formularzu rejestracji
                if (password.value !== confirmPassword.value) {
                    event.preventDefault();
                    event.stopPropagation();
                    confirmPassword.classList.add("is-invalid");
                    confirmPassword.nextElementSibling.textContent = "Passwords do not match.";
                } else {
                    confirmPassword.classList.remove("is-invalid");
                }
            }

            form.classList.add('was-validated');
        }, false);
    });
});
