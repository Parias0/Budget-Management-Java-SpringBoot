document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');
    const toggleFormBtn = document.getElementById('toggleFormBtn');
    const formTitle = document.getElementById('formTitle');
    const errorMessage = document.getElementById('errorMessage');

    // Sprawdzenie, czy formularze istnieją, zanim wykonamy na nich operacje
    if (toggleFormBtn && loginForm && registerForm && formTitle) {
        toggleFormBtn.addEventListener('click', () => {
            const isLoginVisible = loginForm.style.display !== 'none';

            loginForm.style.display = isLoginVisible ? 'none' : 'block';
            registerForm.style.display = isLoginVisible ? 'block' : 'none';
            formTitle.textContent = isLoginVisible ? 'Register' : 'Login';
            toggleFormBtn.textContent = isLoginVisible ? 'Already have an account? Login here' : "Don't have an account? Register here";

            if (errorMessage) {
                errorMessage.style.display = 'none';
                errorMessage.textContent = '';
            }
        });
    }

    // Obsługa wysyłania formularza logowania
    if (loginForm) {
        loginForm.addEventListener("submit", function(event) {
            event.preventDefault();
            const formData = {
                username: document.getElementById("loginUsername").value,
                password: document.getElementById("loginPassword").value
            };

            fetch("http://localhost:8080/api/auth/login", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(formData),
                credentials: "include"
            })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(data => {
                        throw new Error("Incorrect login details. Please try again.");
                    });
                }
                return response.json();
            })
            .then(() => {
                window.location.href = "/";
            })
            .catch(error => {
                if (errorMessage) {
                    errorMessage.style.display = "block";
                    errorMessage.classList.add("alert", "alert-danger", "text-center");
                    errorMessage.textContent = error.message;
                }
            });
        });
    }

    // Obsługa wysyłania formularza rejestracji
    if (registerForm) {
        registerForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const usernameInput = document.getElementById("registerUsername");
            const passwordInput = document.getElementById("registerPassword");

            if (!usernameInput || !passwordInput) return;

            usernameInput.classList.remove("is-invalid");
            passwordInput.classList.remove("is-invalid");
            if (errorMessage) errorMessage.style.display = "none";

            const formData = {
                username: usernameInput.value.trim(),
                password: passwordInput.value.trim()
            };

            fetch("http://localhost:8080/api/auth/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(formData),
                credentials: 'same-origin'
            })
            .then(response => response.json().then(data => ({ status: response.status, body: data })))
            .then(({ status, body }) => {
                if (status === 200) {
                    loginForm.style.display = 'block';
                    registerForm.style.display = 'none';
                    formTitle.textContent = 'Login';
                    toggleFormBtn.textContent = "Don't have an account? Register here";

                    if (errorMessage) {
                        errorMessage.style.display = "block";
                        errorMessage.classList.remove("alert-danger");
                        errorMessage.classList.add("alert-success");
                        errorMessage.textContent = body.message;
                    }
                } else {
                    if (errorMessage) {
                        errorMessage.style.display = "block";
                        errorMessage.classList.remove("alert-success");
                        errorMessage.classList.add("alert-danger");
                        errorMessage.textContent = body.message;
                    }

                    if (body.message.includes("Username")) {
                        usernameInput.classList.add("is-invalid");
                    }
                    if (body.message.includes("Password")) {
                        passwordInput.classList.add("is-invalid");
                    }
                }
            })
            .catch(error => {
                console.error("Error:", error);
                if (errorMessage) {
                    errorMessage.style.display = "block";
                    errorMessage.style.color = "red";
                    errorMessage.textContent = "An unexpected error occurred!";
                }
            });
        });
    }
});

// Obsługa wylogowania
function logoutUser() {
    fetch('/api/auth/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
    .then(response => response.text())
    .then(data => {
        if (data === "User logged out successfully") {
            window.location.href = '/login';
        } else {
            alert("Logout failed");
        }
    })
    .catch(error => {
        console.error('Error during logout:', error);
        alert("Logout failed");
    });
}
