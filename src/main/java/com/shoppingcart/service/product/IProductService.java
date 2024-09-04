package com.shoppingcart.service.product;

import com.shoppingcart.model.Product;
import com.shoppingcart.request.ProductRequest;

import java.util.List;

public interface IProductService {

    public Product getProductById(long productId);
    public void deleteProduct(long productId);

    Product addProduct(ProductRequest productRequest);

    public Product updateProduct(ProductRequest product, long productId);

    public List<Product> getAllProducts();
    public List<Product> getProductsByCategory(String category);
    public List<Product> getProductsByBrand(String brand);
    public List<Product> getProductsByCategoryAndBrand(String category, String brand);
    public List<Product> getProductsByName(String name);
    public List<Product> getProductsByNameAndBrand(String name, String brand);

    public long countProductsByNameAndBrand(String name, String brand);






}
