/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

/**
 *
 * @author Raymond Chan
 */
public class BuyerOrderProduct {

    private Product product;
    private int productTypeID;
    private String productTypeName;
    private int quantity;
    private double productPrice;

    public BuyerOrderProduct() {
        productTypeID = quantity = 0;
        productPrice = 0.0;
        product = new Product();
    }

    public int getProductID() {
        return product.getProductID();
    }

    public void setProductID(int productID) {
        this.product.setProductID(productID);
        new ProductDAO().getProduct(product);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getProductTypeID() {
        return productTypeID;
    }

    public void setProductTypeID(int productTypeID) {
        this.productTypeID = productTypeID;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
}
