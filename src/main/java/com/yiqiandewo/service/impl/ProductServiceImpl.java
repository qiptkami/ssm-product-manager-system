package com.yiqiandewo.service.impl;

import com.github.pagehelper.PageHelper;
import com.yiqiandewo.dao.IProductDao;
import com.yiqiandewo.domain.Product;
import com.yiqiandewo.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private IProductDao productDao;

    @Override
    public List<Product> findAll(int page, int size) {
        PageHelper.startPage(page, size);
        return productDao.findAll();
    }

    @Override
    public List<Product> findByStr(String str) {
        List<Product> list = productDao.findByName(str);
        List<Product> list1 = productDao.findByDesc(str);
        list.addAll(list1);
        return list;
    }

    @Override
    public void saveProduct(Product product) {
        productDao.saveProduct(product);
    }

    @Override
    public void delProduct(Integer productId) {
        productDao.delProduct(productId);
    }

    @Override
    public void updateProduct(Product product) {
        productDao.updateProduct(product);
    }

    @Override
    public Product findById(Integer id) {
        return productDao.findById(id);
    }
}
