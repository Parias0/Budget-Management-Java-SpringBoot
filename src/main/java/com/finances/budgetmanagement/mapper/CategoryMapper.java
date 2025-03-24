package com.finances.budgetmanagement.mapper;

import com.finances.budgetmanagement.dto.CategoryDTO;
import com.finances.budgetmanagement.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO categoryToCategoryDTO(Category category);
    Category categoryDTOToCategory(CategoryDTO categoryDTO);
}
