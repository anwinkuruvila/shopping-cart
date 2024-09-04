package com.shoppingcart.service.product;

import com.shoppingcart.exceptions.ResourceNotFoundException;
import com.shoppingcart.model.Category;
import com.shoppingcart.model.Product;
import com.shoppingcart.repository.CategoryRepository;
import com.shoppingcart.repository.ProductRepository;
import com.shoppingcart.request.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {


    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Product addProduct(ProductRequest productRequest) {

        Category category = Optional.ofNullable(categoryRepository.findByName(productRequest.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(productRequest.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        return productRepository.save(createProductObjectFromRequest(productRequest, category));
    }

    private Product createProductObjectFromRequest(ProductRequest request, Category category){
        return new Product(request.getName(), request.getBrand(), request.getPrice(),
                request.getInventory(), request.getDescription(), category);
    }

    @Override
    public Product updateProduct(ProductRequest request, long productId) {

        Product product = productRepository.findById(productId)
                .map(existingProduct -> updateExistingProduct(existingProduct, request))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productRepository.save(product);

    }

    private Product updateExistingProduct(Product existingProduct, ProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return  existingProduct;

    }

    @Override
    public Product getProductById(long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found for Id"));
    }

    @Override
    public void deleteProduct(long productId) {

        productRepository.findById(productId).ifPresentOrElse(
                product -> productRepository.delete(product),
                () -> {
                    throw new ResourceNotFoundException("product not found for Id");
                });

    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByNameAndBrand(String name, String brand) {
        return productRepository.findByNameAndBrand(name, brand);
    }

    @Override
    public long countProductsByNameAndBrand(String name, String brand) {
        return productRepository.countByNameAndBrand(name, brand);
    }
}

