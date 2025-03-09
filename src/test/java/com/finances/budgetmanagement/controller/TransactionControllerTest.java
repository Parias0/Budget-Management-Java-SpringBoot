package com.finances.budgetmanagement.controller;

import com.finances.budgetmanagement.dto.TransactionDTO;
import com.finances.budgetmanagement.exception.GlobalExceptionHandler;
import com.finances.budgetmanagement.exception.TransactionNotFoundException;
import com.finances.budgetmanagement.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(transactionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // Test: wyjątek rzucany przez serwis podczas tworzenia transakcji -> HTTP 404
    @Test
    public void whenCreateTransaction_ThrowsTransactionNotFoundException_thenReturn404() throws Exception {
        // Przygotowanie danych wejściowych
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(BigDecimal.valueOf(100.0));
        transactionDTO.setDate(LocalDate.parse("2025-03-07"));
        transactionDTO.setDescription("Test transaction");
        transactionDTO.setCategoryName("Non-existing category");
        transactionDTO.setTransactionType("EXPENSE");

        // Mockowanie wyjątku rzucanego przez serwis
        when(transactionService.createTransaction(any(TransactionDTO.class), 1L))
                .thenThrow(new TransactionNotFoundException("Transaction not found!"));

        // Wykonanie żądania POST i sprawdzenie odpowiedzi
        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100.0,\"date\":\"2025-03-07\",\"description\":\"Test transaction\",\"categoryName\":\"Non-existing category\",\"transactionType\":\"EXPENSE\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Transaction not found!"));
    }

    // Test: poprawne tworzenie transakcji -> HTTP 201 i komunikat sukcesu
    @Test
    public void whenCreateTransactionSuccessful_thenReturn201AndMessage() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(BigDecimal.valueOf(100.0));
        transactionDTO.setDate(LocalDate.parse("2025-03-07"));
        transactionDTO.setDescription("Test transaction");
        transactionDTO.setCategoryName("Existing category");
        transactionDTO.setTransactionType("EXPENSE");

        // Symulacja poprawnego działania serwisu
        when(transactionService.createTransaction(any(TransactionDTO.class), 1L))
                .thenReturn(transactionDTO);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":100.0,\"date\":\"2025-03-07\",\"description\":\"Test transaction\",\"categoryName\":\"Existing category\",\"transactionType\":\"EXPENSE\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Transaction created successfully"));
    }

    // Test: aktualizacja transakcji -> HTTP 200 oraz zwrócony obiekt transakcji w formacie JSON
    @Test
    public void whenUpdateTransactionSuccessful_thenReturnUpdatedTransaction() throws Exception {
        TransactionDTO updatedDTO = new TransactionDTO();
        updatedDTO.setId(1L);
        updatedDTO.setAmount(BigDecimal.valueOf(150.0));
        updatedDTO.setDate(LocalDate.parse("2025-03-08"));
        updatedDTO.setDescription("Updated transaction");
        updatedDTO.setCategoryName("Updated category");
        updatedDTO.setTransactionType("INCOME");

        // Symulacja poprawnej aktualizacji transakcji przez serwis
        when(transactionService.updateTransaction(eq(1L), any(TransactionDTO.class), 1L))
                .thenReturn(updatedDTO);

        mockMvc.perform(put("/api/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":150.0,\"date\":\"2025-03-08\",\"description\":\"Updated transaction\",\"categoryName\":\"Updated category\",\"transactionType\":\"INCOME\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(150.0))
                .andExpect(jsonPath("$.description").value("Updated transaction"))
                .andExpect(jsonPath("$.categoryName").value("Updated category"))
                .andExpect(jsonPath("$.transactionType").value("INCOME"));
    }

    // Test: usunięcie transakcji -> HTTP 200 i komunikat o usunięciu
    @Test
    public void whenDeleteTransactionSuccessful_thenReturnOkMessage() throws Exception {
        // Zakładamy, że metoda deleteTransaction w serwisie nie rzuca wyjątku
        doNothing().when(transactionService).deleteTransaction(1L, 1L);

        mockMvc.perform(delete("/api/transactions/remove/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction deleted"));
    }

    // Test: pobranie listy transakcji -> HTTP 200 oraz lista w formacie JSON
    @Test
    public void whenGetAllTransactions_thenReturnTransactions() throws Exception {
        TransactionDTO dto1 = new TransactionDTO(1L, "Category1", "Desc1", BigDecimal.valueOf(100.0), LocalDate.parse("2025-03-07"));
        dto1.setTransactionType("EXPENSE");
        TransactionDTO dto2 = new TransactionDTO(2L, "Category2", "Desc2", BigDecimal.valueOf(200.0), LocalDate.parse("2025-03-08"));
        dto2.setTransactionType("INCOME");

        when(transactionService.getAllTransactions()).thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(get("/api/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].categoryName").value("Category1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].categoryName").value("Category2"));
    }
}
