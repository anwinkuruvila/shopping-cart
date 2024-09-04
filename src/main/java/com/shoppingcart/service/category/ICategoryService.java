package com.shoppingcart.service.category;

import com.shoppingcart.model.Category;

import java.util.List;

public interface ICategoryService {

    public Category getCategoryById(long id);
    public Category getCategoryByName(String name);
    public List<Category> getAllCategories();

    public Category addCategory(Category category);
    public Category updateCategory(Category category, long id);

    public void deleteCategory(long id);

}
