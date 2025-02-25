package com.finances.budgetmanagement.service.impl;

import com.finances.budgetmanagement.dto.CategoryDTO;
import com.finances.budgetmanagement.entity.Category;
import com.finances.budgetmanagement.repository.CategoryRepository;
import com.finances.budgetmanagement.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if (categoryRepository.findByName(categoryDTO.getName()) != null) {
            throw new RuntimeException("Kategoria już istnieje!");
        }

        Category category = mapToEntity(categoryDTO);
        category = categoryRepository.save(category);
        return mapToDTO(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }


    private CategoryDTO mapToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    private Category mapToEntity(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }
}
