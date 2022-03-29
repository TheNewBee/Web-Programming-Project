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
public class Cart {

    private Member member;
    private CartProduct[] cartProducts;

    public Cart() {
        member = new Member();
        cartProducts = null;
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

    public CartProduct[] getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(CartProduct[] cartProducts) {
        this.cartProducts = cartProducts;
    }
}
