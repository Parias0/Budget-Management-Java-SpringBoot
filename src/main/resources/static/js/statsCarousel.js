import { AccountsAPI } from './api.js';

console.log('Plik statsCarousel.js został załadowany');

const showMessage = (message) => {
    const carouselInner = document.getElementById("statsCarouselInner");
    if (carouselInner) {
        carouselInner.innerHTML = `
            <div class="alert alert-info m-3">
                ${message}
            </div>
        `;
    }
};

const showError = (message) => {
    const carouselInner = document.getElementById("statsCarouselInner");
    if (carouselInner) {
        carouselInner.innerHTML = `
            <div class="alert alert-danger m-3">
                ${message}
            </div>
        `;
    }
};

const initCarousel = (accounts) => {
    console.log('Rozpoczęcie inicjalizacji karuzeli z danymi:', accounts);

    const carouselInner = document.getElementById("statsCarouselInner");
    if (!carouselInner) {
        console.error('Brak elementu karuzeli!');
        return;
    }

    carouselInner.innerHTML = '';

    const formatAmount = (value) => {
        const amount = Number(value) || 0;
        return amount.toLocaleString('pl-PL', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        }) + ' PLN';
    };

    accounts.forEach((account, index) => {
        const item = document.createElement('div');
        item.className = `carousel-item ${index === 0 ? 'active' : ''}`;
        item.innerHTML = `
            <div class="card border-0 shadow-sm m-3">
                <div class="card-body">
                    <h4 class="text-center mb-4 lead">${account.accountName}</h4>
                    <div class="row justify-content-center">  <!-- Dodane wyśrodkowanie wiersza -->
                        <div class="col-md-5 mb-3">  <!-- Zmiana na col-md-5 i dodanie text-center -->
                            <div class="text-success text-center">
                                <div class="h5 d-flex align-items-center justify-content-center">
                                    <i class="bi bi-arrow-up-circle me-2"></i> Incomes
                                </div>
                                <div class="h2 mt-2">${formatAmount(account.income)}</div>
                            </div>
                        </div>
                        <div class="col-md-5">  <!-- Zmiana na col-md-5 i dodanie text-center -->
                            <div class="text-danger text-center">
                                <div class="h5 d-flex align-items-center justify-content-center">
                                    <i class="bi bi-arrow-down-circle me-2"></i> Expenses
                                </div>
                                <div class="h2 mt-2">${formatAmount(account.expense)}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        `;
        carouselInner.appendChild(item);
    });

    // Inicjalizacja karuzeli
    const carouselElement = document.getElementById('statsCarousel');
    if (carouselElement) {
        new bootstrap.Carousel(carouselElement, {
            interval: 5000,
            wrap: true
        });
        console.log('Karuzela została zainicjalizowana');
    }
};

document.addEventListener("DOMContentLoaded", async () => {

    try {
        const now = new Date();
        const currentMonth = `${now.getFullYear()}-${(now.getMonth() + 1).toString().padStart(2, '0')}`;
        const response = await AccountsAPI.getAllAccountsSummary(currentMonth);
        console.log('Odpowiedź z API:', response);

        // Poprawiony warunek - sprawdzamy bezpośrednio response
        if (!Array.isArray(response) || response.length === 0) {
            showMessage("Brak kont do wyświetlenia");
            return;
        }

        initCarousel(response); // Przekazujemy bezpośrednio response
    } catch (error) {
        console.error("Błąd:", error);
        showError("Nie udało się załadować danych kont");
    }
});