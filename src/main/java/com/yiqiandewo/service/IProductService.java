package com.yiqiandewo.service;

import com.yiqiandewo.domain.Product;

import java.util.List;

public interface IProductService {

    List<Product> findAll(int page, int size);

    List<Product> findByStr(String str, int page, int size);

    Product findById(Integer id);

    void saveProduct(Product product);

    void delProduct(Integer productId);

    void updateProduct(Product product);
}
