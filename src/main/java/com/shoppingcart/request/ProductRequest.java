package com.shoppingcart.request;

import com.shoppingcart.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {

    private long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
