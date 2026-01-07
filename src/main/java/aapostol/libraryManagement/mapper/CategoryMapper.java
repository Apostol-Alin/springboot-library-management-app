package aapostol.libraryManagement.mapper;

import org.springframework.stereotype.Component;

import aapostol.libraryManagement.dto.CategoryRequest;
import aapostol.libraryManagement.json.Category;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        return category;
    }
}