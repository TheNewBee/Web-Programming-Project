/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 *
 * @author Raymond Chan
 */
public class CartDAO {

    private Connection conn;
    private Statement stmt;

    public CartDAO() {
        conn = new DBConnection().getConnection();
        try {
            stmt = conn.createStatement();
        } catch (SQLException E) {
        }
    }

    // Same with get data from database to object
    // Suitable for re-get data from database.
    public String getCart(Cart cart) {
        try {
            int memberID = cart.getMemberID();
            if (memberID == 0) {
                return "member_not_found";
            } else {
                String query = "SELECT COUNT(*) as row_count, product_id, product_type_id, product_type_name, quantity, added_on, last_modified "
                        + "FROM cart "
                        + "NATURAL JOIN product_type "
                        + "WHERE member_id='" + memberID + "' "
                        + "GROUP BY product_id "
                        + "ORDER BY last_modified DESC";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    System.out.println(123);
                    int index = 0;
                    CartProduct[] cartProducts = new CartProduct[rs.getInt("row_count")];
                    cartProducts[index] = new CartProduct();
                    cartProducts[index].setProductID(rs.getInt("product_id"));
                    cartProducts[index].setProductTypeID(rs.getInt("product_type_id"));
                    cartProducts[index].setProductTypeName(rs.getString("product_type_name"));
                    cartProducts[index].setQuantity(rs.getInt("quantity"));
                    cartProducts[index].setAddedOn(rs.getTimestamp("added_on"));
                    cartProducts[index++].setLastModified(rs.getTimestamp("last_modified"));
                    for (int i = 0; i < cartProducts.length - 1; i++) {
                        cartProducts[index] = new CartProduct();
                        cartProducts[index].setProductID(rs.getInt("product_id"));
                        cartProducts[index].setProductTypeID(rs.getInt("product_type_id"));
                        cartProducts[index].setProductTypeName(rs.getString("product_type_name"));
                        cartProducts[index].setQuantity(rs.getInt("quantity"));
                        cartProducts[index].setAddedOn(rs.getTimestamp("added_on"));
                        cartProducts[index++].setLastModified(rs.getTimestamp("last_modified"));
                        rs.next();
                    }
                    cart.setCartProducts(cartProducts);
                }
            }
            return "success";
        } catch (SQLException E) {
            return "error_507" + E.toString();
        }
    }

    // Same with update data from object to database
    // Suitable for any change in product (add/delete/update product type)
    public String updateCart(Cart cart) {
        try {
            int memberID = cart.getMemberID();
            if (memberID == 0) {
                return "member_not_found";
            } else {
                String query = "DELETE FROM cart WHERE member_id=?";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1, memberID);
                preparedStmt.executeUpdate();

                CartProduct[] cartProducts = cart.getCartProducts();
                for (int i = 0; i < cartProducts.length; i++) {
                    query = "INSERT INTO cart(member_id, product_id, product_type_id, quantity, added_on) "
                            + "VALUES (?,?,?,?,?)";
                    preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, memberID);
                    preparedStmt.setInt(2, cartProducts[i].getProductID());
                    preparedStmt.setInt(3, cartProducts[i].getProductTypeID());
                    preparedStmt.setInt(4, cartProducts[i].getQuantity());
                    preparedStmt.setTimestamp(5, cartProducts[i].getAddedOn());
                    preparedStmt.executeUpdate();
                }
            }
            return "success";
        } catch (SQLException E) {
            return "error_507" + E.toString();
        }
    }

    public String checkOut(Cart cart, CartProduct[] checkOutProducts, String shippingName, int shippingContactNumber, String shippingAddress) {
        try {
            if (checkOutProducts.length > 0) {
                double totalPrice = 0.0;
                int iBuyerOrderProducts = 0;
                int shopID = checkOutProducts[0].getProduct().getShopID();
                BuyerOrder buyerOrder = new BuyerOrder();
                BuyerOrderProduct[] buyerOrderProducts = new BuyerOrderProduct[checkOutProducts.length];
                buyerOrder.setMemberID(cart.getMemberID());
                buyerOrder.setShopID(shopID);
                buyerOrder.setShippingName(shippingName);
                buyerOrder.setShippingContactNumber(shippingContactNumber);
                buyerOrder.setShippingAddress(shippingAddress);

                for (int i = 0; i < checkOutProducts.length; i++) {
                    double productPrice = 0.0;
                    Product product = checkOutProducts[i].getProduct();
                    if (shopID != product.getShopID()) {
                        return "products_not_in_same_shop";
                    }

                    switch (product.getProductPromotionType()) {
                        case 0:
                            productPrice = product.getProductPrice();
                            break;
                        case 1:
                            productPrice = product.getProductPrice() * product.getProductPromotionAmount();
                            break;
                        case 2:
                            productPrice = product.getProductPrice() - product.getProductPromotionAmount();
                            break;
                    }
                    totalPrice += productPrice;
                    Product product2 = checkOutProducts[i].getProduct();
                    new ProductDAO().getProduct(product2);
                    buyerOrderProducts[iBuyerOrderProducts] = new BuyerOrderProduct();
                    buyerOrderProducts[iBuyerOrderProducts].setProduct(product2);
                    buyerOrderProducts[iBuyerOrderProducts].setProductPrice(productPrice);
                    buyerOrderProducts[iBuyerOrderProducts].setProductTypeID(checkOutProducts[i].getProductTypeID());
                    buyerOrderProducts[iBuyerOrderProducts++].setQuantity(checkOutProducts[i].getQuantity());
                }
                buyerOrder.setTotalPrice(totalPrice);
                buyerOrder.setBuyerOrderProducts(buyerOrderProducts);
                new BuyerOrderDAO().createBuyerOrder(buyerOrder);

                String query = "DELETE FROM cart WHERE member_id=?";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1, cart.getMemberID());
                preparedStmt.executeUpdate();
                return "success";
            } else {
                return "no_product";
            }
        } catch (SQLException E) {
            return "error_507" + E.toString();
        }
    }
}
