package com.shoppingcart.service.category;

import com.shoppingcart.exceptions.AlreadyExistsException;
import com.shoppingcart.exceptions.ResourceNotFoundException;
import com.shoppingcart.model.Category;
import com.shoppingcart.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {

        return Optional.of(category)
                .filter(c -> categoryRepository.existsByName(category.getName()))
                .map(c -> categoryRepository.save(c))
                .orElseThrow(() -> new AlreadyExistsException("The Category already exists!"));
    }

    @Override
    public Category updateCategory(Category category, long id) {
        Category oldCategory =  Optional.ofNullable(getCategoryById(id))
                .orElseThrow(() -> new ResourceNotFoundException("category not found for Id"));

        oldCategory.setName(category.getName());
        return categoryRepository.save(oldCategory);
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.findById(id).ifPresentOrElse(
                category -> categoryRepository.delete(category),
                () -> {
                    throw new ResourceNotFoundException("category not found for Id");
                });
    }
}
