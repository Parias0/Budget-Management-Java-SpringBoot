document.addEventListener('DOMContentLoaded', function() {
  // Inicjalizacja: ładowanie danych przy starcie strony
  loadAccountBalance(); // Tylko ta funkcja będzie działać na stronie Home

  // ✅ Dodajemy sprawdzenie, czy elementy istnieją, zanim spróbujemy ich używać
  const transactionTable = document.getElementById('transactionTable');
  const transactionForm = document.getElementById('transactionForm');
  
  if (transactionTable) {
    loadTransactions();
  }
  
  if (transactionForm) {
    loadTransactionCategories();
  }
});

let currentPage = 1;
const rowsPerPage = 10;
let allTransactions = [];

// Funkcja ładująca saldo konta
function loadAccountBalance() {
  fetch('http://localhost:8080/api/accounts/balance', {
    method: 'GET',
    credentials: 'include' // Upewnia się, że ciasteczka sesji są wysyłane
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Nie udało się pobrać salda');
    }
    return response.json();
  })
  .then(account => {
    // Zaktualizowanie elementu h3 z saldem konta
    document.getElementById('accountBalance').innerHTML = `<h4>${account.balance} PLN</h4>`;
  })
  .catch(error => console.error('Błąd ładowania salda:', error));
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

// Ładowanie listy transakcji
function loadTransactions() {
  fetch('http://localhost:8080/api/transactions')
    .then(response => response.json())
    .then(transactions => {
      console.log("Transactions loaded:", transactions);
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

//// Obsługa formularza dodawania nowej transakcji
document.getElementById('transactionForm')?.addEventListener('submit', function(event) {
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
  .then(response => response.text())
  .then(data => {
    alert('Transaction was added!');
    loadTransactions();
    loadAccountBalance();
  })
  .catch(error => console.error('Error adding transaction:', error));
});
