package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.category.CategoryDTO;

import java.util.List;

public interface CategoryService {


    CategoryDTO createCategory (CategoryDTO categoryDTO);

    List<CategoryDTO> getAllCategories();

    void deleteCategory (Long id);

    CategoryDTO updateCategory (Long id, CategoryDTO categoryDTO);


 }
