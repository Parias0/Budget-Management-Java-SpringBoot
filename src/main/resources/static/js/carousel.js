import { TransactionsAPI } from './api.js';

document.addEventListener("DOMContentLoaded", async () => {
    const currentMonth = new Date().toISOString().slice(0, 7); // Pobieramy aktualny rok-miesiąc w formacie YYYY-MM

    try {
        const expensesData = await TransactionsAPI.getCategoryExpensesSummary(currentMonth);
        updateCarousel(expensesData);
    } catch (error) {
        console.error("Error fetching category expenses:", error);
    }
});

function updateCarousel(expenses) {
    const carouselInner = document.getElementById("carouselInner");
    carouselInner.innerHTML = "";

    expenses.forEach((expense, index) => {
        const isActive = index === 0 ? "active" : "";

        const cardHtml = `
            <div class="carousel-item ${isActive}">
                <div class="card text-center">
                    <div class="card-body">
                        <h5 class="card-title">${expense.categoryName}</h5>
                        <p class="card-text">Total spent: ${expense.totalAmount} PLN</p>
                    </div>
                </div>
            </div>
        `;
        carouselInner.innerHTML += cardHtml;
    });
}

  document.addEventListener('DOMContentLoaded', () => {
  const header = document.getElementById('expensesCategoryLabel');
  if (header) {
  const currentMonth = new Date().toLocaleString('en-EN', { month: 'long' });
  header.textContent = `Expense Categories - ${currentMonth}`;
  }
  });
