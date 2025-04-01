import { AuthAPI } from '/js/api.js';

document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const toggleFormBtn = document.getElementById('toggleFormBtn');
    const formTitle = document.getElementById('formTitle');
    const errorMessage = document.getElementById('errorMessage');

    // Toggle between forms
    if (toggleFormBtn && loginForm && registerForm && formTitle) {
        toggleFormBtn.addEventListener('click', () => {
            const isLoginVisible = loginForm.style.display !== 'none';

            loginForm.style.display = isLoginVisible ? 'none' : 'block';
            registerForm.style.display = isLoginVisible ? 'block' : 'none';
            formTitle.textContent = isLoginVisible ? 'Register' : 'Login';
            toggleFormBtn.textContent = isLoginVisible
                ? 'Already have an account? Login here'
                : "Don't have an account? Register here";

            if (errorMessage) {
                errorMessage.style.display = 'none';
                errorMessage.textContent = '';
            }
        });
    }

    // Login handler
    if (loginForm) {
        loginForm.addEventListener("submit", function(event) {
            event.preventDefault();
            const formData = {
                username: document.getElementById("loginUsername").value,
                password: document.getElementById("loginPassword").value
            };

            AuthAPI.login(formData)
                .then(() => {
                window.location.href = "/";
            })
                .catch(error => {
                handleErrorMessage(error.message || "Incorrect login details");
            });
        });
    }

    // Registration handler
    if (registerForm) {
        registerForm.addEventListener("submit", function (event) {
            event.preventDefault();
            const usernameInput = document.getElementById("registerUsername");
            const passwordInput = document.getElementById("registerPassword");

            resetFormValidation(usernameInput, passwordInput);

            const formData = {
                username: usernameInput.value.trim(),
                password: passwordInput.value.trim()
            };

            AuthAPI.register(formData)
                .then(data => {
                toggleToLoginForm();
                handleSuccessMessage(data.message || "Registration successful!");
            })
                .catch(error => {
                handleRegistrationError(error, usernameInput, passwordInput);
            });
        });
    }
});

// Logout handler
function logoutUser() {
    AuthAPI.logout()
        .then(() => {
        window.location.href = '/login';
    })
        .catch(() => {
        alert("Logout failed");
    });
}

// Helper functions
function handleErrorMessage(message) {
    const errorMessage = document.getElementById('errorMessage');
    if (errorMessage) {
        errorMessage.style.display = "block";
        errorMessage.classList.add("alert-danger");
        errorMessage.textContent = message;
    }
}

function handleSuccessMessage(message) {
    const errorMessage = document.getElementById('errorMessage');
    if (errorMessage) {
        errorMessage.style.display = "block";
        errorMessage.classList.remove("alert-danger");
        errorMessage.classList.add("alert-success");
        errorMessage.textContent = message;
    }
}

function resetFormValidation(...inputs) {
    inputs.forEach(input => input.classList.remove("is-invalid"));
    const errorMessage = document.getElementById('errorMessage');
    if (errorMessage) errorMessage.style.display = "none";
}

function toggleToLoginForm() {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const formTitle = document.getElementById('formTitle');
    const toggleFormBtn = document.getElementById('toggleFormBtn');

    if (loginForm && registerForm && formTitle && toggleFormBtn) {
        loginForm.style.display = 'block';
        registerForm.style.display = 'none';
        formTitle.textContent = 'Login';
        toggleFormBtn.textContent = "Don't have an account? Register here";
    }
}

function handleRegistrationError(error, usernameInput, passwordInput) {
    console.error("Error:", error);

    if (error.message.includes("Username")) {
        usernameInput.classList.add("is-invalid");
    }
    if (error.message.includes("Password")) {
        passwordInput.classList.add("is-invalid");
    }

    handleErrorMessage(error.message || "Registration failed");
}