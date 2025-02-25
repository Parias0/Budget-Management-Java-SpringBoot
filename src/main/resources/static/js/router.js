document.addEventListener("DOMContentLoaded", function () {
  handleRouting();
});

function handleRouting() {
  const path = window.location.pathname;
  if (path === "/categories") {
    loadCategories();
  } else if (path === "/transactions") {
    loadTransactions();
  }
}

function navigateTo(route) {
  window.location.href = route;
}
