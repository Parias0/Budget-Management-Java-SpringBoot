// Importowanie API dla kont
import { AccountsAPI } from './api.js';

// Funkcja ładująca wszystkie konta i renderująca je na stronie
async function loadAccounts() {
  try {
    const accounts = await AccountsAPI.getAllAccounts();
    renderAccountsList(accounts);
  } catch (error) {
    console.error('Error loading accounts:', error);
    showAlert('Failed to load accounts', 'danger');
  }
}

// Funkcja renderująca listę kont
function renderAccountsList(accounts) {
  const accountsContainer = document.getElementById('accounts-list');
  accountsContainer.innerHTML = '';

  // Sprawdź, czy jesteśmy na stronie home.html
  const isHomePage = document.getElementById('home-accounts-header') !== null;

  accounts.forEach(account => {
    const accountItem = document.createElement(isHomePage ? 'div' : 'a');

    if (!isHomePage) {
      accountItem.classList.add('list-group-item-action');
      accountItem.setAttribute('href', '#');
      accountItem.addEventListener('click', (e) => {
        e.preventDefault();
        openAccountModal(account.id);
      });
    }

    accountItem.classList.add('list-group-item');
    accountItem.setAttribute('data-account-id', account.id);
    accountItem.innerHTML = `${account.name} - ${account.balance} PLN`;
    accountsContainer.appendChild(accountItem);
  });
}

// Funkcja tworząca nowe konto
async function createAccount(event) {
  event.preventDefault();  // Zapobieganie domyślnemu działaniu formularza

  const accountData = {
    name: document.getElementById('account-name').value,
    balance: parseFloat(document.getElementById('account-balance').value),
  };

  try {
    const createdAccount = await AccountsAPI.createAccount(accountData);
    showAlert('Account created successfully', 'success');  // Pokaż alert sukcesu
    loadAccounts();  // Przeładuj listę kont po dodaniu nowego
  } catch (error) {
    console.error('Error creating account:', error);
    showAlert('Failed to create account', 'danger');  // Pokaż alert błędu
  }
}

// Funkcja do wyświetlania alertów Bootstrap
function showAlert(message, type) {
  const alertContainer = document.getElementById('alert-container');
  const alert = document.createElement('div');
  alert.classList.add('alert', `alert-${type}`, 'alert-dismissible', 'fade', 'show');
  alert.role = 'alert';
  alert.innerHTML = `
    <strong>${type === 'success' ? 'Success' : 'Error'}:</strong> ${message}
    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
  `;

  alertContainer.appendChild(alert);

  // Opcjonalnie: usuwanie alertu po kilku sekundach
  setTimeout(() => {
    alert.remove();
  }, 5000);  // Usuwa alert po 5 sekundach
}


// Zmodyfikowana funkcja openAccountModal
async function openAccountModal(accountId) {
  try {
    const account = await AccountsAPI.getAccountById(accountId); // Używamy API

    document.getElementById('account-name-modal').value = account.name;
    document.getElementById('account-balance-modal').value = account.balance;

    const modalButtons = document.getElementById('modal-buttons');
    modalButtons.innerHTML = `
      <button type="button" class="btn btn-primary" id="save-changes-btn">Save Changes</button>
      <button type="button" class="btn btn-danger" id="delete-account-btn">Delete Account</button>
    `;

    document.getElementById('save-changes-btn').addEventListener('click', () => editAccount(accountId));
    document.getElementById('delete-account-btn').addEventListener('click', () => deleteAccount(accountId));

    new bootstrap.Modal(document.getElementById('accountModal')).show();

  } catch (error) {
    console.error('Error opening modal:', error);
    showAlert('Cannot load account details: ' + error.message, 'danger');
  }
}

async function editAccount(accountId) {
  try {
    const newName = document.getElementById('account-name-modal').value;
    const newBalance = parseFloat(document.getElementById('account-balance-modal').value);

    const updatedData = {
      name: newName,
      balance: newBalance,
    };

    const updatedAccount = await AccountsAPI.updateAccount(accountId, updatedData);
    showAlert('Account updated successfully', 'success');
    loadAccounts();

    // Zamknięcie modala
    bootstrap.Modal.getInstance(document.getElementById('accountModal')).hide();

  } catch (error) {
    console.error('Error updating account:', error);
    showAlert('Failed to update account: ' + error.message, 'danger');
  }


  // Zamknięcie modala
  const myModal = bootstrap.Modal.getInstance(document.getElementById('accountModal'));
  myModal.hide();
}

// Funkcja usuwająca konto
async function deleteAccount(accountId) {
  const confirmation = confirm('Are you sure you want to delete this account?');

  if (confirmation) {
    try {
      const result = await AccountsAPI.deleteAccount(accountId);
      alert('Account deleted successfully');
      loadAccounts();  // Przeładuj listę kont po usunięciu
    } catch (error) {
      console.error('Error deleting account:', error);
      alert('Failed to delete account');
    }
  }

  // Zamknięcie modala
  const myModal = bootstrap.Modal.getInstance(document.getElementById('accountModal'));
  myModal.hide();
}


// Obsługa submit formularza "Create Account" TYLKO jeśli formularz istnieje
const createAccountForm = document.getElementById('create-account-form');
if (createAccountForm) {
  createAccountForm.addEventListener('submit', createAccount);
}

// Wywołanie ładowania kont TYLKO jeśli kontener istnieje
const accountsListContainer = document.getElementById('accounts-list');
if (accountsListContainer) {
  document.addEventListener('DOMContentLoaded', loadAccounts);
}

// Wywołanie ładowania kont przy załadowaniu strony
document.addEventListener('DOMContentLoaded', loadAccounts);
