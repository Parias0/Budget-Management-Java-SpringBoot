import { TransactionsAPI, CategoriesAPI, AccountsAPI } from './api.js'; // Upewnij się, że importujesz AccountsAPI

let transactions = [];
let accountsList = [];
let currentPage = 1;
const pageSize = 10;

// Inicjalizacja – ładowanie kategorii, kont, transakcji i ostatnich transakcji
document.addEventListener('DOMContentLoaded', async () => {
  await loadAccounts();  // Czekamy, aż konta zostaną załadowane
  await loadTransactions();

  if (document.getElementById('transactionForm')) {
    setupFormSubmit();
    loadCategories();
    setupPagination();
    setupModalForm();
  }
});

// Ładowanie kont
async function loadAccounts() {
  try {
    const accounts = await AccountsAPI.getAllAccounts();
    accountsList = accounts;
    populateSelect('account', accounts, 'id', 'name');
    populateSelect('editAccount', accounts, 'id', 'name');
  } catch (error) {
    console.error('Error loading accounts:', error);
  }
}


// Funkcja uzupełniająca select (dla kategorii i kont)
// Dla kont przekazujemy dodatkowo klucze (valueKey oraz textKey)
function populateSelect(selectId, items, valueKey = 'name', textKey = 'name') {
  const select = document.getElementById(selectId);
  if (!select) return;
  select.innerHTML = '';
  // Dodaj pustą opcję, jeśli chcesz umożliwić wybór domyślnego konta
  const defaultOption = document.createElement('option');
  defaultOption.value = '';
  defaultOption.textContent = '-- Select --';
  select.appendChild(defaultOption);
  items.forEach(item => {
    const option = document.createElement('option');
    option.value = item[valueKey];
    option.textContent = item[textKey];
    select.appendChild(option);
  });
}

// Ładowanie kategorii do obu formularzy
async function loadCategories() {
  try {
    const categories = await CategoriesAPI.getAllCategories();
    populateSelect('category', categories);
    populateSelect('editCategory', categories);
  } catch (error) {
    console.error('Error loading categories:', error);
  }
}

// Obsługa wysyłania formularza tworzenia transakcji
function setupFormSubmit() {
  const form = document.getElementById('transactionForm');
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const transactionData = {
      date: form.date.value,
      amount: form.amount.value,
      description: form.description.value,
      transactionType: document.querySelector('input[name="transactionType"]:checked').value,
      categoryName: form.category.value,
      accountId: form.account.value ? form.account.value : null, // Dodajemy accountId jeśli został wybrany
    };

    try {
      const createdTransaction = await TransactionsAPI.createTransaction(transactionData);
      form.reset();
      // Po dodaniu transakcji odświeżamy listę
      loadTransactions();
    } catch (error) {
      console.error('Error creating transaction:', error);
    }
  });
}

// Pobieranie transakcji i renderowanie ich
async function loadTransactions() {
  try {
    transactions = await TransactionsAPI.getAllTransactions();
    renderRecentTransactions();

    // Sprawdź, czy jesteśmy na stronie transactions.html, zanim uruchomisz renderowanie pełnej listy
    if (document.getElementById('transactionList')) {
      renderTransactions();
    }
  } catch (error) {
    console.error('Error loading transactions:', error);
  }
}

// Renderowanie transakcji jako elementów list-group
function renderTransactions() {
  const transactionList = document.getElementById('transactionList');
  transactionList.innerHTML = '';

  const startIndex = (currentPage - 1) * pageSize;
  const paginatedItems = transactions.slice(startIndex, startIndex + pageSize);

  paginatedItems.forEach(tx => {
    // Wyszukiwanie konta dla transakcji
    const account = accountsList.find(acc => acc.id == tx.accountId);
    const accountName = account ? account.name : 'Default account';

    // Utworzenie elementu listy dla transakcji
    const li = document.createElement('li');
    li.className = 'list-group-item';

    // Możesz użyć struktury kart wewnątrz elementu listy
    li.innerHTML = `
      <div class="d-flex justify-content-between align-items-center">
        <div>
          <h6 class="mb-1">${tx.transactionType} - ${tx.amount}</h6>
          <p class="mb-1">${tx.description}</p>
          <small class="text-muted">${tx.date} | ${tx.categoryName}</small>
        </div>
        <div class="text-end">
          <span class="badge bg-secondary mb-2">${accountName}</span>
          <div>
            <button class="btn btn-sm btn-info" onclick="openEditModal(${tx.id})">Edit</button>
            <button class="btn btn-sm btn-danger" onclick="deleteTransaction(${tx.id})">Delete</button>
          </div>
        </div>
      </div>
    `;

    transactionList.appendChild(li);
  });
}


// Renderowanie ostatnich 5 transakcji w karcie po prawej
function renderRecentTransactions() {
  const recentContainer = document.getElementById('recentTransactions');
  if (!recentContainer) return;
  recentContainer.innerHTML = '';
  const recent = transactions.slice(-5).reverse(); // najnowsze na górze
  recent.forEach(tx => {
    // Wyszukiwanie konta dla transakcji
    const account = accountsList.find(acc => acc.id == tx.accountId);
    const accountName = account ? account.name : 'Default account';

    const card = document.createElement('div');
    card.className = 'card mb-2';
    card.innerHTML = `
      <div class="card-body">
        <div class="d-flex justify-content-between align-items-center">
          <h6 class="card-title mb-0">${tx.transactionType} - ${tx.amount} 'PLN'</h6>
          <span class="badge bg-secondary">${accountName}</span>
        </div>
        <p class="card-text mt-2">${tx.description}</p>
        <small class="text-muted">${tx.date}</small>
      </div>
    `;
    recentContainer.appendChild(card);
  });
}



// Obsługa paginacji
function setupPagination() {
  document.getElementById('prevPage').addEventListener('click', () => {
    if (currentPage > 1) {
      currentPage--;
      renderTransactions();
    }
  });
  document.getElementById('nextPage').addEventListener('click', () => {
    if (currentPage * pageSize < transactions.length) {
      currentPage++;
      renderTransactions();
    }
  });
}

// Otwarcie modalu do edycji transakcji
window.openEditModal = function (id) {
  const tx = transactions.find(item => item.id === id);
  if (!tx) return;

  // Wypełnienie formularza edycji
  document.getElementById('editTransactionId').value = tx.id;
  document.getElementById('editDate').value = tx.date;
  document.getElementById('editAmount').value = tx.amount;
  document.getElementById('editDescription').value = tx.description;
  // Ustawienie radiobuttonów
  if (tx.transactionType === 'INCOME') {
    document.getElementById('editIncomeRadio').checked = true;
  } else {
    document.getElementById('editExpenseRadio').checked = true;
  }
  document.getElementById('editCategory').value = tx.categoryName;
  // Ustawienie wybranego konta w formularzu edycji
  if(document.getElementById('editAccount')){
    document.getElementById('editAccount').value = tx.accountId || '';
  }

  // Otwarcie modalu (Bootstrap 5)
  const editModal = new bootstrap.Modal(document.getElementById('editTransactionModal'));
  editModal.show();
};

// Obsługa formularza edycji transakcji
function setupModalForm() {
  const editForm = document.getElementById('editTransactionForm');
  editForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const id = document.getElementById('editTransactionId').value;
    const transactionData = {
      date: document.getElementById('editDate').value,
      amount: document.getElementById('editAmount').value,
      description: document.getElementById('editDescription').value,
      transactionType: document.querySelector('input[name="editTransactionType"]:checked').value,
      categoryName: document.getElementById('editCategory').value,
      accountId: document.getElementById('editAccount')?.value || null,
    };

    try {
      await TransactionsAPI.updateTransaction(id, transactionData);
      // Po edycji odświeżamy dane
      loadTransactions();
      // Ukrycie modalu
      const editModalEl = document.getElementById('editTransactionModal');
      const modalInstance = bootstrap.Modal.getInstance(editModalEl);
      modalInstance.hide();
    } catch (error) {
      console.error('Error updating transaction:', error);
    }
  });
}

// Usuwanie transakcji
window.deleteTransaction = async function (id) {
  if (confirm('Czy na pewno chcesz usunąć transakcję?')) {
    try {
      await TransactionsAPI.deleteTransaction(id);
      loadTransactions();
    } catch (error) {
      console.error('Error deleting transaction:', error);
    }
  }
};
