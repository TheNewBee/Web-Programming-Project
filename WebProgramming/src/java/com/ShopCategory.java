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
public class ShopCategory {

    private int shopCategoryID;
    private String shopCategoryName;

    public ShopCategory() {
        shopCategoryID = 0;
        shopCategoryName = "";
    }

    public int getShopCategoryID() {
        return shopCategoryID;
    }

    public void setShopCategoryID(int shopCategoryID) {
        this.shopCategoryID = shopCategoryID;
    }

    public String getShopCategoryName() {
        return shopCategoryName;
    }

    public void setShopCategoryName(String shopCategoryName) {
        this.shopCategoryName = shopCategoryName;
    }
}
