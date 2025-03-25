import { AccountsAPI, CategoriesAPI, SummaryAPI } from './api.js';

document.addEventListener('DOMContentLoaded', function() {
    loadAccounts();
    loadCategoriesList();
    setupFilterForm();
});

// Ładowanie listy kont do selecta
async function loadAccounts() {
    try {
        const accounts = await AccountsAPI.getAllAccounts();
        const accountSelect = document.getElementById('accountSelect');

        accountSelect.innerHTML = accounts.map(account => `
            <option value="${account.id}">${account.name} (${account.balance?.toFixed(2)} PLN)</option>
        `).join('');

        // Ustaw domyślny miesiąc na bieżący
        const now = new Date();
        document.getElementById('monthInput').value =
            `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;

    } catch (error) {
        console.error("Error loading accounts:", error);
        showError("Failed to load accounts");
    }
}

// Ładowanie listy kategorii (prawa panel)
async function loadCategoriesList() {
    try {
        const categories = await CategoriesAPI.getAllCategories();
        const categoriesList = document.getElementById('categoriesList');

        categoriesList.innerHTML = categories.map(category => `
            <li class="list-group-item d-flex justify-content-between align-items-center">
                ${category.name}
                <span class="badge bg-primary rounded-pill">${category.id}</span>
            </li>
        `).join('');

    } catch (error) {
        console.error("Error loading categories:", error);
        showError("Failed to load categories");
    }
}

// Konfiguracja formularza filtrów
function setupFilterForm() {
    const filterForm = document.getElementById('filterForm');

    filterForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const accountId = document.getElementById('accountSelect').value;
        const month = document.getElementById('monthInput').value;

        if (!accountId || !month) {
            showError("Please select both account and month");
            return;
        }

        showLoading();

        try {
            const data = await SummaryAPI.getAccountCategoryData(accountId, month);
            renderData(data);
        } catch (error) {
            console.error("Error loading data:", error);
            showError(error.message || "Failed to load data");
        }
    });
}

// Renderowanie danych
function renderData(data) {
    const container = document.getElementById('dataContainer');

    container.innerHTML = `
        <div class="card mb-4">
            <div class="card-header bg-primary text-white">
                <h3 class="card-title mb-0 lead">
                    Total Expenses for ${data.month}: ${data.totalExpenses?.toFixed(2)} PLN
                </h3>
            </div>
            <div class="card-body">
                ${data.categories?.map(category => `
                    <div class="category-card mb-4 border rounded p-3">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h4 class="mb-0 text-primary">${category.categoryName}</h4>
                            <span class="badge bg-danger fs-6">${category.totalAmount?.toFixed(2)} PLN</span>
                        </div>

                        <div class="transaction-list">
                            ${category.transactions?.map(transaction => `
                                <div class="transaction-item d-flex justify-content-between align-items-center mb-2 p-2 border-bottom">
                                    <div class="me-3">
                                        <div class="fw-bold">${transaction.description || 'No description'}</div>
                                        <small class="text-muted">${transaction.date}</small>
                                    </div>
                                    <div class="text-end">
                                        <div class="fw-bold text-danger">${transaction.amount?.toFixed(2)} PLN</div>
                                    </div>
                                </div>
                            `).join('') || '<div class="text-muted">No transactions in this category</div>'}
                        </div>
                    </div>
                `).join('') || '<div class="alert alert-info">No categories with expenses found</div>'}
            </div>
        </div>
    `;
}

// Pomocnicze funkcje UI
function showLoading() {
    const container = document.getElementById('dataContainer');
    container.innerHTML = `
        <div class="text-center my-5">
            <div class="spinner-border text-primary" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
            <p class="mt-2">Loading data...</p>
        </div>
    `;
}

function showError(message) {
    const container = document.getElementById('dataContainer');
    container.innerHTML = `
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;
}