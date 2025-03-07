// transactions.js

document.addEventListener('DOMContentLoaded', function() {
  // Inicjalizacja: ładowanie danych przy starcie strony
  loadTransactionCategories();
  loadTransactions();
  loadAvailableYears();
  loadAccountBalance();
});

let currentPage = 1;
const rowsPerPage = 10;
let allTransactions = [];

// Funkcja ładująca saldo konta
function loadAccountBalance(containerId) {
  fetch('http://localhost:8080/api/accounts/balance')
    .then(response => response.json())
    .then(account => {
      document.getElementById(containerId).innerHTML = `<h4>${account.balance} PLN</h4>`;
    })
    .catch(error => console.error('Error loading account balance:', error));
}


// Ładowanie dostępnych kategorii dla formularza dodawania transakcji
function loadTransactionCategories() {
  fetch('http://localhost:8080/api/categories')
    .then(response => response.json())
    .then(categories => {
      const selectElement = document.getElementById('category');
      if (selectElement) {
        selectElement.innerHTML = "";
        categories.forEach(category => {
          const option = document.createElement('option');
          option.value = category.name;
          option.textContent = category.name;
          selectElement.appendChild(option);
        });
      }
    })
    .catch(error => console.error('Error loading categories:', error));
}

// Obsługa formularza dodawania nowej transakcji
document.getElementById('transactionForm').addEventListener('submit', function(event) {
  event.preventDefault();
  const transactionData = {
    date: document.getElementById('date').value,
    amount: document.getElementById('amount').value,
    description: document.getElementById('description').value,
    categoryName: document.getElementById('category').value,
    transactionType: document.querySelector('input[name="transactionType"]:checked').value
  };

  fetch('http://localhost:8080/api/transactions', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(transactionData)
  })
  .then(response => response.json())
  .then(data => {
    alert('Transaction was added!');
    loadTransactions();
    loadAvailableYears();
    loadAccountBalance('accountBalance2');
  })
  .catch(error => console.error('Error adding transaction:', error));
});

// Ładowanie listy transakcji
function loadTransactions() {
  fetch('http://localhost:8080/api/transactions')
    .then(response => response.json())
    .then(transactions => {
      allTransactions = transactions;
      displayTransactions();
    })
    .catch(error => console.error('Error loading transactions:', error));
}

function displayTransactions() {
  const tbody = document.getElementById('transactionTable');
  tbody.innerHTML = "";
  const start = (currentPage - 1) * rowsPerPage;
  const end = start + rowsPerPage;
  const paginatedItems = allTransactions.slice(start, end);

  paginatedItems.forEach(transaction => {
    const rowClass = transaction.transactionType === 'INCOME' ? 'income-row' : 'expense-row';
    tbody.innerHTML += `
      <tr class="${rowClass}">
        <td>${transaction.id}</td>
        <td>${transaction.date}</td>
        <td>${transaction.amount}</td>
        <td>${transaction.description}</td>
        <td>${transaction.categoryName}</td>
        <td>${transaction.transactionType}</td>
        <td><button class="btn btn-sm btn-warning" onclick='openEditModal(${JSON.stringify(transaction)})'>Edit</button></td>
        <td><button class="btn btn-sm btn-danger" onclick="deleteTransaction(${transaction.id})">Delete</button></td>
      </tr>
    `;
  });
  updatePaginationButtons();
}

function updatePaginationButtons() {
  document.getElementById('prevPage').disabled = currentPage === 1;
  document.getElementById('nextPage').disabled = currentPage * rowsPerPage >= allTransactions.length;
}

function prevPage() {
  if (currentPage > 1) {
    currentPage--;
    displayTransactions();
  }
}

function nextPage() {
  if (currentPage * rowsPerPage < allTransactions.length) {
    currentPage++;
    displayTransactions();
  }
}



// Ładowanie dostępnych lat dla podsumowania miesięcznego
function loadAvailableYears() {
  fetch('http://localhost:8080/api/transactions/available-years')
    .then(response => response.json())
    .then(years => {
      const selectElement = document.getElementById('yearSelect');
      if (selectElement) {
        selectElement.innerHTML = "<option value=''>Select year</option>";
        years.forEach(year => {
          const option = document.createElement('option');
          option.value = year;
          option.textContent = year;
          selectElement.appendChild(option);
        });
      }
    })
    .catch(error => console.error("Error loading years:", error));
}

// Funkcja otwierająca modal edycji transakcji
function openEditModal(transaction) {
  // Wypełnij formularz danymi transakcji
  document.getElementById('editTransactionId').value = transaction.id;
  document.getElementById('editDate').value = transaction.date;
  document.getElementById('editAmount').value = transaction.amount;
  document.getElementById('editDescription').value = transaction.description;

  // Ustawienie odpowiedniego radio buttona
  if (transaction.transactionType === 'INCOME') {
    document.getElementById('editIncomeRadio').checked = true;
  } else {
    document.getElementById('editExpenseRadio').checked = true;
  }

  // Załaduj dostępne kategorie do selecta w modal'u
  fetch('http://localhost:8080/api/categories')
    .then(response => response.json())
    .then(categories => {
      const selectElement = document.getElementById('editCategory');
      selectElement.innerHTML = "";
      categories.forEach(category => {
        const option = document.createElement('option');
        option.value = category.name;
        option.textContent = category.name;
        if (category.name === transaction.categoryName) {
          option.selected = true;
        }
        selectElement.appendChild(option);
      });
    })
    .catch(error => console.error('Error loading categories:', error));

  // Otwórz modal korzystając z API Bootstrapa 5
    var modalElement = document.getElementById('editTransactionModal');
    var modal = bootstrap.Modal.getInstance(modalElement);
    if (!modal) {
      modal = new bootstrap.Modal(modalElement);
    }
    modal.show();
  }

// Obsługa formularza edycji transakcji
document.getElementById('editTransactionForm').addEventListener('submit', function(event) {
  event.preventDefault();
  const transactionId = document.getElementById('editTransactionId').value;
  const updatedData = {
    date: document.getElementById('editDate').value,
    amount: document.getElementById('editAmount').value,
    description: document.getElementById('editDescription').value,
    categoryName: document.getElementById('editCategory').value,
    transactionType: document.querySelector('input[name="editTransactionType"]:checked').value
  };

  fetch('http://localhost:8080/api/transactions/' + transactionId, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(updatedData)
  })
  .then(response => response.json())
  .then(data => {
    alert('Transaction updated successfully!');
    // Ukryj modal korzystając z API Bootstrapa 5
    var modalElement = document.getElementById('editTransactionModal');
    var modal = bootstrap.Modal.getInstance(modalElement);
    if (modal) {
      modal.hide();
    }
    loadTransactions();
    loadAccountBalance();
  })
  .catch(error => console.error('Error updating transaction:', error));
});


// Usuwanie transakcji
function deleteTransaction(id) {
  fetch(`http://localhost:8080/api/transactions/remove/${id}`, {
    method: "DELETE"
  })
  .then(() => {
    alert("Transaction was deleted!");
    loadTransactions();
    loadAccountBalance();
  })
  .catch(error => console.error("Transaction deletion error:", error));
}

// Pagination Buttons Initialization
document.getElementById('prevPage').onclick = prevPage;
document.getElementById('nextPage').onclick = nextPage;