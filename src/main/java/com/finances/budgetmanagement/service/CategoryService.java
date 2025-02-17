package com.finances.budgetmanagement.service;

import com.finances.budgetmanagement.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {


    CategoryDTO createCategory (CategoryDTO categoryDTO);

    List<CategoryDTO> getAllCategories();
}
