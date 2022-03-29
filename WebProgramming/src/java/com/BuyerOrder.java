/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.sql.Timestamp;

/**
 *
 * @author Raymond Chan
 */
public class BuyerOrder {

    private int buyerOrderID;
    private int memberID;
    private int shopID;
    private int buyerOrderPromotionID;
    private String buyerOrderPromotionName;
    private int buyerOrderPromotionType;
    private double buyerOrderPromotionAmount;
    private double totalPrice;
    private String shippingName;
    private int shippingContactNumber;
    private String shippingAddress;
    private int status;
    private Timestamp datetime;
    private BuyerOrderProduct[] buyerOrderProducts;

    public BuyerOrder() {
        buyerOrderID = memberID = shopID = buyerOrderPromotionID
                = buyerOrderPromotionType = shippingContactNumber = 0;
        status = 1;
        buyerOrderPromotionAmount = totalPrice = 0.0;
        buyerOrderPromotionName = shippingName = shippingAddress = "";
        datetime = new Timestamp(new java.util.Date().getTime());
        buyerOrderProducts = new BuyerOrderProduct[1];
    }

    public int getBuyerOrderID() {
        return buyerOrderID;
    }

    public void setBuyerOrderID(int buyerOrderID) {
        this.buyerOrderID = buyerOrderID;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public int getBuyerOrderPromotionID() {
        return buyerOrderPromotionID;
    }

    public void setBuyerOrderPromotionID(int buyerOrderPromotionID) {
        this.buyerOrderPromotionID = buyerOrderPromotionID;
    }

    public String getBuyerOrderPromotionName() {
        return buyerOrderPromotionName;
    }

    public void setBuyerOrderPromotionName(String buyerOrderPromotionName) {
        this.buyerOrderPromotionName = buyerOrderPromotionName;
    }

    public int getBuyerOrderPromotionType() {
        return buyerOrderPromotionType;
    }

    public void setBuyerOrderPromotionType(int buyerOrderPromotionType) {
        this.buyerOrderPromotionType = buyerOrderPromotionType;
    }

    public double getBuyerOrderPromotionAmount() {
        return buyerOrderPromotionAmount;
    }

    public void setBuyerOrderPromotionAmount(double buyerOrderPromotionAmount) {
        this.buyerOrderPromotionAmount = buyerOrderPromotionAmount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getShippingName() {
        return shippingName;
    }

    public void setShippingName(String shippingName) {
        this.shippingName = shippingName;
    }

    public int getShippingContactNumber() {
        return shippingContactNumber;
    }

    public void setShippingContactNumber(int shippingContactNumber) {
        this.shippingContactNumber = shippingContactNumber;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getDatetime() {
        return datetime;
    }

    public void setDatetime(Timestamp datetime) {
        this.datetime = datetime;
    }

    public BuyerOrderProduct[] getBuyerOrderProducts() {
        return buyerOrderProducts;
    }

    public void setBuyerOrderProducts(BuyerOrderProduct[] buyerOrderProducts) {
        this.buyerOrderProducts = buyerOrderProducts;
    }
}
