// categories.js

document.addEventListener('DOMContentLoaded', function() {
  loadCategories();
});

// Ładowanie listy kategorii
function loadCategories() {
  fetch('http://localhost:8080/api/categories')
    .then(response => response.json())
    .then(categories => {
      const table = document.getElementById("categoryTable");
      table.innerHTML = "";
      categories.forEach(category => {
        const row = document.createElement("tr");
        row.innerHTML = `
          <td>${category.id}</td>
          <td>${category.name}</td>
          <td>
            <button class="btn btn-danger btn-sm" onclick="deleteCategory(${category.id})">Delete</button>
          </td>
        `;
        table.appendChild(row);
      });
    })
    .catch(error => console.error("Category loading error:", error));
}

// Obsługa formularza dodawania nowej kategorii
document.getElementById('categoryForm').addEventListener('submit', function(event) {
  event.preventDefault();
  const categoryName = document.getElementById('categoryNameInput').value;
  fetch('http://localhost:8080/api/categories', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name: categoryName })
  })
  .then(response => response.json())
  .then(data => {
    alert('New category added!');
    loadCategories();
  })
  .catch(error => console.error('Error adding category:', error));
});

// Usuwanie kategorii
function deleteCategory(id) {
  fetch(`http://localhost:8080/api/categories/${id}`, {
    method: "DELETE"
  })
  .then(() => {
    alert("Category was deleted!");
    loadCategories();
  })
  .catch(error => console.error("Category deletion error:", error));
}
