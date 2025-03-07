// Tablica nazw miesięcy po angielsku
const monthNames = [
  'January', 'February', 'March', 'April', 'May', 'June',
  'July', 'August', 'September', 'October', 'November', 'December'
];

let expenseChart = null;
let incomeChart = null;

// Ładowanie podsumowania miesięcznego dla wybranego roku, miesiąca i kategorii
function loadMonthlySummary() {
  const selectedYear = document.getElementById('yearSelect').value;
  const selectedMonth = document.getElementById('monthSelect').value; // "" = Select month, "All" lub konkretny miesiąc
  // Pobieramy wybrane kategorie (wartości będą tablicą)
  const categorySelect = document.getElementById('categorySelect');
  const selectedCategories = Array.from(categorySelect.selectedOptions).map(opt => opt.value);

  // Wymagamy, aby rok był wybrany i żeby został dokonany wybór miesiąca (nie pozostawał domyślny)
  if (!selectedYear || !selectedMonth) {
    console.log("Year and month must be selected.");
    return;
  }

  console.log(`Loading data for year: ${selectedYear}, month: ${selectedMonth}, categories: ${selectedCategories.join(', ') || 'All'}`);

  // Ukrywamy wykresy
  document.getElementById('expensesChartContainer').style.display = 'none';
  document.getElementById('incomeChartContainer').style.display = 'none';

  fetch(`http://localhost:8080/api/transactions/summary?year=${selectedYear}`)
    .then(response => response.json())
    .then(summary => {
      console.log('Response from server:', summary);
      const container = document.getElementById('monthlySummary');

      if (!summary || summary.length === 0) {
        container.innerHTML = "<p>No data available for the selected year.</p>";
        return;
      }

      container.innerHTML = `<h3>Summary for the year ${selectedYear}</h3>`;

      // Uzupełniamy select kategorii (raz) – jeśli nie zawiera opcji
      if (document.getElementById('categorySelect').options.length <= 1) {
        let uniqueCategories = new Set(summary.map(item => item.categoryName));
        uniqueCategories.forEach(category => {
          const opt = document.createElement('option');
          opt.value = category;
          opt.textContent = category;
          document.getElementById('categorySelect').appendChild(opt);
        });
      }

      // Grupowanie danych po miesiącach
      const months = {};
      summary.forEach(item => {
        const mName = monthNames[item.month - 1];
        if (!months[mName]) {
          months[mName] = [];
        }
        months[mName].push(item);
      });

      // Filtrowanie po miesiącu
      let monthsToDisplay;
      if (selectedMonth && selectedMonth !== 'All') {
        monthsToDisplay = months[selectedMonth] ? { [selectedMonth]: months[selectedMonth] } : {};
      } else {
        monthsToDisplay = months;
      }

      if (Object.keys(monthsToDisplay).length === 0) {
        container.innerHTML += "<p>No data available for the selected month.</p>";
      } else {
        container.innerHTML += generateMonthlyTable(monthsToDisplay);
      }

      // Rysujemy wykresy z dodatkowym filtrem kategorii
      drawExpenseChart(summary, selectedMonth, selectedCategories);
      drawIncomeChart(summary, selectedMonth, selectedCategories);

      // Pokazujemy wykresy
      document.getElementById('expensesChartTitle').style.display = 'block';
      document.getElementById('incomeChartTitle').style.display = 'block';
      document.getElementById('expensesChartContainer').style.display = 'block';
      document.getElementById('incomeChartContainer').style.display = 'block';
    })
    .catch(error => {
      console.error("Error loading data:", error);
    });
}

function generateMonthlyTable(monthsToDisplay) {
  let html = "";
  for (const month in monthsToDisplay) {
    html += `<h4>${month}</h4>`;
    html += `<table class="table table-bordered">
                <thead class="thead-dark">
                  <tr><th>Category</th><th>Total Amount</th><th>Type</th></tr>
                </thead>
                <tbody>`;
    monthsToDisplay[month].forEach(item => {
      const rowClass = item.transactionType === 'INCOME' ? 'income-row' : 'expense-row';
      html += `
        <tr class="${rowClass}">
          <td>${item.categoryName}</td>
          <td>${item.totalAmount}</td>
          <td>${item.transactionType}</td>
        </tr>`;
    });
    html += `</tbody></table>`;
  }
  return html;
}





// Funkcja rysująca wykres wydatków z filtrem kategorii
function drawExpenseChart(summary, selectedMonth, selectedCategories) {
  const expenseChartContainer = document.getElementById('expensesChartContainer');
  const expenseCanvas = document.getElementById('expensesChart');

  // Filtrowanie po miesiącu i kategorii
  const filteredSummary = summary.filter(item =>
    item.transactionType === 'EXPENSE' &&
    (selectedMonth === '' || selectedMonth === 'All' || monthNames[item.month - 1] === selectedMonth) &&
    (selectedCategories.length === 0 || selectedCategories.includes(item.categoryName))
  );

  if (filteredSummary.length === 0) {
    expenseCanvas.style.display = 'none';
    if (!document.getElementById('expenseNoDataMsg')) {
      const msg = document.createElement('p');
      msg.id = 'expenseNoDataMsg';
      msg.textContent = "No expense data available for the selected filters.";
      expenseChartContainer.appendChild(msg);
    }
    return;
  } else {
    expenseCanvas.style.display = 'block';
    const existingMsg = document.getElementById('expenseNoDataMsg');
    if (existingMsg) existingMsg.remove();
  }

  const labels = [];
  const data = [];
  filteredSummary.forEach(item => {
    const index = labels.indexOf(item.categoryName);
    if (index === -1) {
      labels.push(item.categoryName);
      data.push(item.totalAmount);
    } else {
      data[index] += item.totalAmount;
    }
  });

  if (expenseChart !== null) {
    expenseChart.destroy();
  }

  expenseChart = new Chart(expenseCanvas.getContext('2d'), {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: 'Expenses',
        data: data,
        backgroundColor: 'rgba(255, 99, 132, 0.6)',
        borderColor: 'rgba(255, 99, 132, 1)',
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      scales: {
        y: { beginAtZero: true }
      }
    }
  });
}

// Funkcja rysująca wykres przychodów z filtrem kategorii
function drawIncomeChart(summary, selectedMonth, selectedCategories) {
  const incomeChartContainer = document.getElementById('incomeChartContainer');
  const incomeCanvas = document.getElementById('incomeChart');

  const filteredSummary = summary.filter(item =>
    item.transactionType === 'INCOME' &&
    (selectedMonth === '' || selectedMonth === 'All' || monthNames[item.month - 1] === selectedMonth) &&
    (selectedCategories.length === 0 || selectedCategories.includes(item.categoryName))
  );

  if (filteredSummary.length === 0) {
    incomeCanvas.style.display = 'none';
    if (!document.getElementById('incomeNoDataMsg')) {
      const msg = document.createElement('p');
      msg.id = 'incomeNoDataMsg';
      msg.textContent = "No income data available for the selected filters.";
      incomeChartContainer.appendChild(msg);
    }
    return;
  } else {
    incomeCanvas.style.display = 'block';
    const existingMsg = document.getElementById('incomeNoDataMsg');
    if (existingMsg) existingMsg.remove();
  }

  const labels = [];
  const data = [];
  filteredSummary.forEach(item => {
    const index = labels.indexOf(item.categoryName);
    if (index === -1) {
      labels.push(item.categoryName);
      data.push(item.totalAmount);
    } else {
      data[index] += item.totalAmount;
    }
  });

  if (incomeChart !== null) {
    incomeChart.destroy();
  }

  incomeChart = new Chart(incomeCanvas.getContext('2d'), {
    type: 'bar',
    data: {
      labels: labels,
      datasets: [{
        label: 'Income',
        data: data,
        backgroundColor: 'rgba(75, 192, 192, 0.6)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1
      }]
    },
    options: {
      responsive: true,
      scales: {
        y: { beginAtZero: true }
      }
    }
  });
}
