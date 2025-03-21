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
  if (!accountsContainer) return;

  accountsContainer.innerHTML = '';

  const isHomePage = document.getElementById('home-accounts-header') !== null;

  accounts.forEach(account => {
    const accountItem = document.createElement(isHomePage ? 'div' : 'a');

    // Formatowanie kwoty
    const balance = typeof account.balance === 'number'
      ? account.balance.toFixed(2)
      : parseFloat(account.balance || 0).toFixed(2);

    if (!isHomePage) {
      accountItem.classList.add('list-group-item-action');
      accountItem.setAttribute('href', '#');
      accountItem.addEventListener('click', (e) => {
        e.preventDefault();
        openAccountModal(account.id);
      });
    }

    accountItem.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center');
    accountItem.innerHTML = `
      <span>${account.name}</span>
      <span class="badge bg-primary rounded-pill">${balance} PLN</span>
    `;

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
    const account = await AccountsAPI.getAccountById(accountId);

    if (!account) {
      throw new Error('Account not found');
    }

    console.log('Received account data:', account); // Debugowanie

    document.getElementById('account-name-modal').value = account.name || '';
    document.getElementById('account-balance-modal').value = account.balance || 0;

    const modalButtons = document.getElementById('modal-buttons');
    modalButtons.innerHTML = `
      <button type="button" class="btn btn-primary" id="save-changes-btn">Save Changes</button>
      <button type="button" class="btn btn-danger" id="delete-account-btn">Delete Account</button>
    `;

    // Usuń istniejące event listeners przed dodaniem nowych
    const cleanUpEvents = () => {
      document.getElementById('save-changes-btn')?.removeEventListener('click', saveHandler);
      document.getElementById('delete-account-btn')?.removeEventListener('click', deleteHandler);
    };

    const saveHandler = () => {
      cleanUpEvents();
      editAccount(accountId);
    };

    const deleteHandler = () => {
      cleanUpEvents();
      deleteAccount(accountId);
    };

    document.getElementById('save-changes-btn').addEventListener('click', saveHandler);
    document.getElementById('delete-account-btn').addEventListener('click', deleteHandler);

    new bootstrap.Modal(document.getElementById('accountModal')).show();

  } catch (error) {
    console.error('Error opening modal:', error);
    showAlert(`Cannot load account details: ${error.message}`, 'danger');

    // Zamknij modal w przypadku błędu
    const modal = bootstrap.Modal.getInstance(document.getElementById('accountModal'));
    modal?.hide();
  }
}

async function editAccount(accountId) {
  try {
    const newName = document.getElementById('account-name-modal').value.trim();
    const newBalance = parseFloat(document.getElementById('account-balance-modal').value);

    if (!newName) {
      showAlert('Account name cannot be empty', 'danger');
      return;
    }

    const updatedData = {
      name: newName,
      balance: newBalance
    };

    const updatedAccount = await AccountsAPI.updateAccount(accountId, updatedData);
    console.log('Updated account:', updatedAccount);

    showAlert('Account updated successfully', 'success');
    loadAccounts();

    const modal = bootstrap.Modal.getInstance(document.getElementById('accountModal'));
    modal.hide();

  } catch (error) {
    console.error('Error updating account:', error);
    showAlert(`Failed to update account: ${error.message}`, 'danger');
  }
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
