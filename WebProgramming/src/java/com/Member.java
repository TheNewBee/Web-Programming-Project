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
public class Member {

    private int memberID;
    private int memberLevel;
    private int amountBought;
    private boolean hasShop;
    private boolean disabled;
    private String email;
    private String nickname;
    private Timestamp regDate;
    private Timestamp lastLogin;

    public Member() {
        memberID = memberLevel = amountBought = 0;
        email = nickname = "";
        regDate = lastLogin
                = new Timestamp(new java.util.Date().getTime());
        hasShop = false;
        disabled = true;
    }

    public int getMemberID() {
        return memberID;
    }

    public void setMemberID(int memberID) {
        this.memberID = memberID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getMemberLevel() {
        return memberLevel;
    }

    public void setMemberLevel(int memberLevel) {
        this.memberLevel = memberLevel;
    }

    public int getAmountBought() {
        return amountBought;
    }

    public void setAmountBought(int amountBought) {
        this.amountBought = amountBought;
    }

    public boolean getHasShop() {
        return hasShop;
    }

    public void setHasShop(boolean hasShop) {
        this.hasShop = hasShop;
    }

    public boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Timestamp getRegDate() {
        return regDate;
    }

    public void setRegDate(Timestamp regDate) {
        this.regDate = regDate;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }
}
