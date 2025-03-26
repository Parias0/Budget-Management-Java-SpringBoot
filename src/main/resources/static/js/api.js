const apiBaseURL = '/api';

const apiRequest = async (method, url, data = null) => {
  try {
    const options = {
      method,
      headers: { 'Content-Type': 'application/json' },
    };
    if (data) options.body = JSON.stringify(data);

    const response = await fetch(apiBaseURL + url, options);

    if (response.status === 403) {
      window.location.href = '/error/403';
      return;
    }

    if (!response.ok) throw new Error(`Error: ${response.statusText}`);

    return await response.json();
  } catch (error) {
    console.error(error);
  }
};

// Skrócone funkcje dla poszczególnych metod:
const apiGet = (url) => apiRequest('GET', url);
const apiPost = (url, data) => apiRequest('POST', url, data);
const apiPut = (url, data) => apiRequest('PUT', url, data);
const apiDelete = (url) => apiRequest('DELETE', url);

// API for accounts
const AccountsAPI = {
  getAllAccounts: () => apiGet('/accounts'),
  getAccountById: (id) => apiGet(`/accounts/${id}`),
  createAccount: (accountData) => apiPost('/accounts', accountData),
  updateAccount: (accountId, accountData) => apiPut(`/accounts/${accountId}`, accountData),
  deleteAccount: (accountId) => apiDelete(`/accounts/${accountId}`),
};

// API for categories
const CategoriesAPI = {
  getAllCategories: () => apiGet('/categories'),
  createCategory: (categoryData) => apiPost('/categories', categoryData),
  updateCategory: (categoryId, categoryData) => apiPut(`/categories/${categoryId}`, categoryData),
  deleteCategory: (categoryId) => apiDelete(`/categories/${categoryId}`),
};

// API for transactions
const TransactionsAPI = {
  getAllTransactions: () => apiGet('/transactions'),
  createTransaction: (transactionData) => apiPost('/transactions', transactionData),
  updateTransaction: (transactionId, transactionData) => apiPut(`/transactions/${transactionId}`, transactionData),
  deleteTransaction: (transactionId) => apiDelete(`/transactions/${transactionId}`),
  getFilteredTransactions: (params) =>
  apiGet('/transactions/filter?' + new URLSearchParams(params).toString()),
};

// Auth API
const AuthAPI = {
  login: (loginData) => apiPost('/auth/login', loginData),
  register: (registerData) => apiPost('/auth/register', registerData),
  logout: () => apiPost('/auth/logout'),
};

// Summary API
const SummaryAPI = {
  getAllAccountsSummary: (month) => apiGet(`/summary/account-summary?month=${month}`),
  getAccountCategoryData: (accountId, month) =>
  apiGet(`/summary/account-category-expenses?accountId=${accountId}&month=${month}`),
  getCategoryExpensesSummary: (month) => apiGet(`/summary/category-expenses-summary?month=${month}`),
};

const ReportAPI = {
  getMonthlyReport: (accountId, month) =>
  apiGet(`/reports/monthly?accountId=${accountId}&month=${month}`),
};




export { AccountsAPI, CategoriesAPI, TransactionsAPI, AuthAPI, SummaryAPI, ReportAPI };
