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
public class ProductPhoto {

    private int status;
    private String productPhotoPath;
    private Timestamp addedOn;
    private Timestamp lastModified;

    public ProductPhoto() {
        status = 0;
        productPhotoPath = "";
        addedOn = lastModified
                = new Timestamp(new java.util.Date().getTime());
    }

    public String getProductPhotoPath() {
        return productPhotoPath;
    }

    public void setProductPhotoPath(String productPhotoPath) {
        this.productPhotoPath = productPhotoPath;
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
}
