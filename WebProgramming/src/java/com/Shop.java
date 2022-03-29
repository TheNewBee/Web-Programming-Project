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
public class Shop {

    private int shopID;
    private String shopName;
    private String shopIntroduction;
    private String shopDetail;
    private ShopCategory shopCategory;
    private int shopLevel;
    private Member member;
    private int contactNumber;
    private String contactEmail;
    private String website;
    private int amountSold;
    private boolean disabled;
    private Timestamp createdOn;
    private Timestamp lastModified;
    private int[] shopTabIDs;
    private String[] shopTabNames;

    public Shop() {
        shopID = contactNumber = amountSold = 0;
        shopLevel = 1;
        shopName = shopIntroduction = shopDetail = contactEmail = website = "";
        disabled = false;
        createdOn = lastModified
                = new Timestamp(new java.util.Date().getTime());
        member = new Member();
        shopCategory = new ShopCategory();
        shopTabIDs = new int[1];
        shopTabNames = new String[1];
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopIntroduction() {
        return shopIntroduction;
    }

    public void setShopIntroduction(String shopIntroduction) {
        this.shopIntroduction = shopIntroduction;
    }

    public String getShopDetail() {
        return shopDetail;
    }

    public void setShopDetail(String shopDetail) {
        this.shopDetail = shopDetail;
    }

    public ShopCategory getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(ShopCategory shopCategory) {
        this.shopCategory = shopCategory;
    }

    public int getShopLevel() {
        return shopLevel;
    }

    public void setShopLevel(int shopLevel) {
        this.shopLevel = shopLevel;
    }

    public int getMemberID() {
        return member.getMemberID();
    }

    public void setMemberID(int memberID) {
        member.setMemberID(memberID);
        new MemberDAO().getMember(member);
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getContractNumber() {
        return contactNumber;
    }

    public void setContractNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContractEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getAmountSold() {
        return amountSold;
    }

    public void setAmountSold(int amountSold) {
        this.amountSold = amountSold;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDiabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public Timestamp getLastModified() {
        return lastModified;
    }

    public void setLastModified(Timestamp lastModified) {
        this.lastModified = lastModified;
    }

    public int[] getShopTabIDs() {
        return shopTabIDs;
    }

    public void setShopTabIDs(int[] shopTabIDs) {
        this.shopTabIDs = shopTabIDs;
    }

    public String[] getShopTabNames() {
        return shopTabNames;
    }

    public void setShopTabNames(String[] shopTabNames) {
        this.shopTabNames = shopTabNames;
    }
}
