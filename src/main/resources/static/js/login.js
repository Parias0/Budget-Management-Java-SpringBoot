document.addEventListener("DOMContentLoaded", function () {
  const loginForm = document.getElementById('loginForm');
  const registerForm = document.getElementById('registerForm');
  const toggleFormBtn = document.getElementById('toggleFormBtn');
  const formTitle = document.getElementById('formTitle');
  const errorMessage = document.getElementById('errorMessage');

  toggleFormBtn.addEventListener('click', () => {
    if (loginForm.style.display === 'none') {
      // Przełącz na formularz logowania
      loginForm.style.display = 'block';
      registerForm.style.display = 'none';
      formTitle.textContent = 'Login';
      toggleFormBtn.textContent = "Don't have an account? Register here";
    } else {
      // Przełącz na formularz rejestracji
      loginForm.style.display = 'none';
      registerForm.style.display = 'block';
      formTitle.textContent = 'Register';
      toggleFormBtn.textContent = 'Already have an account? Login here';
    }
    errorMessage.style.display = 'none';
    errorMessage.textContent = '';
  });

// Obsługa wysyłania formularza logowania
loginForm.addEventListener("submit", function(event) {
    event.preventDefault();
    const formData = {
        username: document.getElementById("loginUsername").value,
        password: document.getElementById("loginPassword").value
    };

    fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(formData),
        credentials: "include"
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(data => {
                const customErrorMessage = "Incorrect login details. Please try again.";
                throw new Error(customErrorMessage);
            });
        }
        return response.json();
    })
    .then(data => {
        window.location.href = "/";
    })
    .catch(error => {
        const errorMessageElement = document.getElementById('errorMessage');
        errorMessageElement.style.display = "block";
        errorMessageElement.classList.add("alert", "alert-danger", "text-center");
        errorMessageElement.textContent = error.message;
    });
});




  // Obsługa wysyłania formularza rejestracji
  registerForm.addEventListener("submit", function(event) {
    event.preventDefault();
    const formData = {
      username: document.getElementById("registerUsername").value,
      password: document.getElementById("registerPassword").value
    };
    fetch("http://localhost:8080/api/auth/register", {
      method: "POST",
      headers: {"Content-Type": "application/json"},
      body: JSON.stringify(formData),
      credentials: 'same-origin'
    })
    .then(response => {
      if (response.ok) {
        // Po udanej rejestracji przełączamy formularz na logowanie
        loginForm.style.display = 'block';
        registerForm.style.display = 'none';
        formTitle.textContent = 'Login';
        toggleFormBtn.textContent = "Don't have an account? Register here";
        errorMessage.style.display = "block";
        errorMessage.style.color = "green";
        errorMessage.textContent = "User registered successfully. Please log in.";
        // Opcjonalnie możesz wyczyścić formularz rejestracji
        registerForm.reset();
      } else {
        errorMessage.style.display = "block";
        errorMessage.style.color = "red";
        errorMessage.textContent = "Registration failed!";
      }
    })
    .catch(error => console.error('Error:', error));
  });
});
