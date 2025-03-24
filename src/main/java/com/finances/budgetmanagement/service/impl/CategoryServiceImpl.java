package com.finances.budgetmanagement.service.impl;

import com.finances.budgetmanagement.dto.CategoryDTO;
import com.finances.budgetmanagement.entity.Category;
import com.finances.budgetmanagement.exception.CategoryNotFoundException;
import com.finances.budgetmanagement.mapper.CategoryMapper;
import com.finances.budgetmanagement.repository.CategoryRepository;
import com.finances.budgetmanagement.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.findByName(categoryDTO.getName()) != null) {
            throw new RuntimeException("Kategoria już istnieje!");
        }

        Category category = categoryMapper.categoryDTOToCategory(categoryDTO);
        category = categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDTO(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::categoryToCategoryDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (!category.getTransactions().isEmpty()) {
            throw new RuntimeException("Cannot delete category with associated transactions");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        category.setName(categoryDTO.getName());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.categoryToCategoryDTO(updatedCategory);
    }
}
