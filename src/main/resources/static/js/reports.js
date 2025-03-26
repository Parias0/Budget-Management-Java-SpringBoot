import { ReportAPI, AccountsAPI } from './api.js';

let ratioChart = null;
let categoryChart = null;

const formatCurrency = (value) => {
  return Number(value).toLocaleString('pl-PL', {
    style: 'currency',
    currency: 'PLN',
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });
};

const renderCharts = (data) => {
  if (ratioChart) ratioChart.destroy();
  if (categoryChart) categoryChart.destroy();

  // Wykres stosunku przychodów do wydatków
  const ratioCtx = document.getElementById('ratioChart').getContext('2d');
  ratioChart = new Chart(ratioCtx, {
    type: 'doughnut',
    data: {
      labels: ['Income', 'Expenses'],
      datasets: [{
        data: [data.totalIncome, data.totalExpense],
        backgroundColor: ['#28a745', '#dc3545']
      }]
    }
  });

  // Wykres podziału na kategorie
  const categoryCtx = document.getElementById('categoryChart').getContext('2d');
  const categories = Object.keys(data.categoryBreakdown);
  categoryChart = new Chart(categoryCtx, {
    type: 'bar',
    data: {
      labels: categories,
      datasets: [{
        label: 'Amount',
        data: categories.map(c => data.categoryBreakdown[c]),
        backgroundColor: '#007bff'
      }]
    },
    options: {
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            callback: (value) => formatCurrency(value)
          }
        }
      }
    }
  });
};

const renderTransactions = (transactions) => {
  const container = document.getElementById('transactionList');
  container.innerHTML = '';

  transactions.forEach(tx => {
    const element = document.createElement('a');
    element.className = `list-group-item list-group-item-action
      ${tx.transactionType === 'INCOME' ? 'list-group-item-success' : 'list-group-item-danger'}`;
    element.innerHTML = `
      <div class="d-flex justify-content-between">
        <div>
          <h6 class="mb-1">${tx.description}</h6>
          <small>${new Date(tx.date).toLocaleDateString('pl-PL')}</small>
        </div>
        <div class="text-end">
          <div>${formatCurrency(tx.amount)}</div>
          <small class="text-muted">${tx.categoryName}</small>
        </div>
      </div>
    `;
    container.appendChild(element);
  });
};

window.loadReport = async () => {
  const accountId = document.getElementById('accountSelect').value;
  const month = document.getElementById('monthSelect').value;

  if (!accountId || !month) {
    alert('Proszę wybrać konto i miesiąc');
    return;
  }

  try {
    document.getElementById('loading').classList.remove('d-none');
    document.getElementById('reportContent').classList.add('d-none');

    const reportData = await ReportAPI.getMonthlyReport(accountId, month);

    document.getElementById('totalIncome').textContent = formatCurrency(reportData.totalIncome);
    document.getElementById('totalExpense').textContent = formatCurrency(reportData.totalExpense);

    renderCharts(reportData);
    renderTransactions(reportData.transactions);

    document.getElementById('loading').classList.add('d-none');
    document.getElementById('reportContent').classList.remove('d-none');
  } catch (error) {
    console.error('Błąd:', error);
    alert('Błąd podczas generowania raportu');
    document.getElementById('loading').classList.add('d-none');
  }
};

document.addEventListener('DOMContentLoaded', async () => {
  try {
    // Ustaw domyślny miesiąc
    const now = new Date();
    const currentMonth = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;
    document.getElementById('monthSelect').value = currentMonth;

    // Załaduj konta
    const accounts = await AccountsAPI.getAllAccounts();
    const select = document.getElementById('accountSelect');

    accounts.forEach(account => {
      const option = document.createElement('option');
      option.value = account.id;
      option.textContent = account.name;
      select.appendChild(option);
    });

    // Automatycznie generuj pierwszy raport
    await loadReport();

  } catch (error) {
    console.error('Błąd inicjalizacji:', error);
    alert('Błąd ładowania danych');
  }
});