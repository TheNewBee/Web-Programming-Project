/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author Raymond Chan
 */
public class Product {

    private int productID;
    private int shopID;
    private int amountStock;
    private int amountSold;
    private int status;
    private double productPrice;
    private String productName;
    private String productIntroduction;
    private String productDetail;
    private int[] productTabIDs;
    private String[] productTabNames;
    private int[] productTypeIDs;
    private String[] productTypeNames;
    private Timestamp addedOn;
    private Timestamp lastModified;

    private ProductCategory productCategory;

    private int productPromotionID;
    private String productPromotionName;
    private int productPromotionType;
    private double productPromotionAmount;

    private ProductComment[] productComments;
    private ProductPhoto[] productPhotos;

    public Product() {
        productID = shopID = amountStock = amountSold = status
                = productPromotionID = productPromotionType = 0;
        productPrice = productPromotionAmount = 0.0;
        productName = productIntroduction = productDetail
                = productPromotionName = "";
        addedOn = lastModified
                = new Timestamp(new java.util.Date().getTime());
        productTabIDs = productTypeIDs = new int[1];
        productTabNames = productTypeNames = new String[1];
        productCategory = new ProductCategory();
        productComments = new ProductComment[1];
        productPhotos = new ProductPhoto[1];
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductIntroduction() {
        return productIntroduction;
    }

    public void setProductIntroduction(String productIntroduction) {
        this.productIntroduction = productIntroduction;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getAmountStock() {
        return amountStock;
    }

    public void setAmountStock(int amountStock) {
        this.amountStock = amountStock;
    }

    public int getAmountSold() {
        return amountSold;
    }

    public void setAmountSold(int amountSold) {
        this.amountSold = amountSold;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Timestamp addedOn) {
        this.addedOn = addedOn;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }

    public int[] getProductTabIDs() {
        return productTabIDs;
    }

    public void setProductTabIDs(int[] productTabIDs) {
        this.productTabIDs = productTabIDs;
    }

    public String[] getProductTabNames() {
        return productTabNames;
    }

    public void setProductTabNames(String[] productTabNames) {
        this.productTabNames = productTabNames;
    }

    public int[] getProductTypeIDs() {
        return productTypeIDs;
    }

    public void setProductTypeIDs(int[] productTypeIDs) {
        this.productTypeIDs = productTypeIDs;
    }

    public String[] getProductTypeNames() {
        return productTypeNames;
    }

    public void setProductTypeNames(String[] productTypeNames) {
        this.productTypeNames = productTypeNames;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public int getProductPromotionID() {
        return productPromotionID;
    }

    public void setProductPromotionID(int productPromotionID) {
        this.productPromotionID = productPromotionID;
    }

    public String getProductPromotionName() {
        return productPromotionName;
    }

    public void setProductPromotionName(String productPromotionName) {
        this.productPromotionName = productPromotionName;
    }

    public int getProductPromotionType() {
        return productPromotionType;
    }

    public void setProductPromotionType(int productPromotionType) {
        this.productPromotionType = productPromotionType;
    }

    public double getProductPromotionAmount() {
        return productPromotionAmount;
    }

    public void setProductPromotionAmount(double productPromotionAmount) {
        this.productPromotionAmount = productPromotionAmount;
    }

    public ProductComment[] getProductComments() {
        return productComments;
    }

    public void setProductComments(ProductComment[] productComments) {
        this.productComments = productComments;
    }

    public ProductPhoto[] getProductPhotos() {
        return productPhotos;
    }

    public void setProductPhotos(ProductPhoto[] productPhotos) {
        this.productPhotos = productPhotos;
    }
}
