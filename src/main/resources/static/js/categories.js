import { CategoriesAPI, TransactionsAPI } from './api.js';

document.addEventListener('DOMContentLoaded', function() {
    loadCategories();
    setupMonthForm();
    loadCurrentMonthData();
});

// Ładowanie listy wszystkich kategorii (prawa kolumna)
function loadCategories() {
    CategoriesAPI.getAllCategories()
        .then(categories => {
            const list = document.getElementById("categoriesList");
            list.innerHTML = categories.map(category => `
                <li class="list-group-item d-flex justify-content-between align-items-center">
                    ${category.name}
                    <span class="badge bg-primary rounded-pill">ID: ${category.id}</span>
                </li>
            `).join('');
        })
        .catch(error => console.error("Error loading categories:", error));
}

// Obsługa formularza wyboru miesiąca
function setupMonthForm() {
    document.getElementById('monthForm').addEventListener('submit', function(e) {
        e.preventDefault();
        const month = document.getElementById('monthInput').value;
        loadMonthlyData(month);
    });
}

// Automatyczne ładowanie danych dla bieżącego miesiąca
function loadCurrentMonthData() {
    const now = new Date();
    const month = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;
    document.getElementById('monthInput').value = month;
    loadMonthlyData(month);
}

// Główna funkcja ładowania danych miesięcznych
function loadMonthlyData(month) {
    TransactionsAPI.getClientCategoryData(month)
        .then(data => {
            renderMonthlyData(data);
        })
        .catch(error => {
            console.error("Error loading monthly data:", error);
            document.getElementById('monthlyData').innerHTML = `
                <div class="alert alert-danger">Error loading data for selected month</div>
            `;
        });
}

// Renderowanie danych miesięcznych
function renderMonthlyData(data) {
    const container = document.getElementById('monthlyData');
    container.innerHTML = `
        <div class="row">
            <div class="col-12">
                <h4 class="mb-3 lead">Total Expenses: ${data.totalExpenses.toFixed(2)} PLN</h4>
            </div>

            ${data.categories.map(category => `
                <div class="col-md-6 mb-4">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="card-title mb-0 lead">${category.categoryName}</h5>
                            <span class="text-muted">Total: ${category.totalAmount.toFixed(2)} PLN</span>
                        </div>
                        <div class="card-body">
                            <h6>Transactions:</h6>
                            <ul class="list-group list-group-flush">
                                ${category.transactions.map(transaction => `
                                    <li class="list-group-item d-flex justify-content-between align-items-center">
                                        <div>
                                            <span class="badge bg-secondary me-2">${transaction.date}</span>
                                            ${transaction.description}
                                        </div>
                                        <span class="text-nowrap">${transaction.amount.toFixed(2)} PLN</span>
                                    </li>
                                `).join('')}
                            </ul>
                        </div>
                    </div>
                </div>
            `).join('')}
        </div>
    `;
}
