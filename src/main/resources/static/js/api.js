const apiBaseURL = '/api';

//GET request
const apiGet = async (url) => {
  try {
    const response = await fetch(apiBaseURL + url);
    if (!response.ok) {
      throw new Error(`Error: ${response.statusText}`);
    }
    return await response.json();
  } catch (error) {
    console.error(error);
  }
};

//POST request
const apiPost = async (url, data) => {
  try {
    const response = await fetch(apiBaseURL + url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      throw new Error(`Error: ${response.statusText}`);
    }
    return await response.json();
  } catch (error) {
    console.error(error);
  }
};

//PUT request
const apiPut = async (url, data) => {
  try {
    const response = await fetch(apiBaseURL + url, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
    if (!response.ok) {
      throw new Error(`Error: ${response.statusText}`);
    }
    return await response.json();
  } catch (error) {
    console.error(error);
  }
};

//DELETE request
const apiDelete = async (url) => {
  try {
    const response = await fetch(apiBaseURL + url, {
      method: 'DELETE',
    });
    if (!response.ok) {
      throw new Error(`Error: ${response.statusText}`);
    }
    return await response.json();
  } catch (error) {
    console.error(error);
  }
};

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

// API for transactions
const SummaryAPI = {
  getAllAccountsSummary: (month) => apiGet(`/summary/account-summary?month=${month}`),
  getAccountCategoryData: (accountId, month) =>
  apiGet(`/summary/account-category-expenses?accountId=${accountId}&month=${month}`),
  getCategoryExpensesSummary: (month) => apiGet(`/summary/category-expenses-summary?month=${month}`),
};




export { AccountsAPI, CategoriesAPI, TransactionsAPI, AuthAPI, SummaryAPI };
