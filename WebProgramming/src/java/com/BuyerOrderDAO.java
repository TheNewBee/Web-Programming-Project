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

/**
 *
 * @author Raymond Chan
 */
public class BuyerOrderDAO {

    private Connection conn;
    private Statement stmt;

    public BuyerOrderDAO() {
        conn = new DBConnection().getConnection();
        try {
            stmt = conn.createStatement();
        } catch (SQLException E) {
        }
    }

    public String createBuyerOrder(BuyerOrder buyerOrder) {
        try {
            int memberID = buyerOrder.getMemberID();
            int shopID = buyerOrder.getShopID();
            BuyerOrderProduct[] buyerOrderProducts = buyerOrder.getBuyerOrderProducts();
            if (buyerOrderProducts.length <= 0) {
                return "no_product_in_order";
            }

            if (!"".equals(buyerOrder.getBuyerOrderPromotionName())) {
                updateBuyerOrderPromotion(buyerOrder);
            }

            String query = "INSERT INTO buyer_order(member_id, shop_id, "
                    + "buyer_order_promotion_id, total_price, shipping_name, "
                    + "shipping_contact_number, shipping_address, status, datetime) "
                    + "VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, memberID);
            preparedStmt.setInt(2, shopID);
            preparedStmt.setInt(3, buyerOrder.getBuyerOrderPromotionID());
            preparedStmt.setDouble(4, buyerOrder.getTotalPrice());
            preparedStmt.setString(5, buyerOrder.getShippingName());
            preparedStmt.setInt(6, buyerOrder.getShippingContactNumber());
            preparedStmt.setString(7, buyerOrder.getShippingAddress());
            preparedStmt.setInt(8, buyerOrder.getStatus());
            preparedStmt.setTimestamp(9, buyerOrder.getDatetime());
            preparedStmt.executeUpdate();

            query = "SELECT buyer_order_id FROM buyer_order WHERE member_id='" + memberID + "' AND shop_id='" + shopID + "' ORDER BY datetime DESC";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                int buyerOrderID = rs.getInt("buyer_order_id");
                buyerOrder.setBuyerOrderID(buyerOrderID);
                for (BuyerOrderProduct buyerOrderProduct : buyerOrderProducts) {
                    query = "INSERT INTO buyer_order_product(buyer_order_id, product_id, product_type_id, quantity, product_price) "
                            + "VALUES (?,?,?,?,?)";
                    preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, buyerOrderID);
                    System.out.println(buyerOrderProduct.getProduct());
                    preparedStmt.setInt(2, buyerOrderProduct.getProduct().getProductID());
                    preparedStmt.setInt(3, buyerOrderProduct.getProductTypeID());
                    preparedStmt.setInt(4, buyerOrderProduct.getQuantity());
                    preparedStmt.setDouble(5, buyerOrderProduct.getProductPrice());
                    preparedStmt.executeUpdate();
                }
            }
            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String deleteBuyerOrder(BuyerOrder buyerOrder) {
        try {
            int buyerOrderID = buyerOrder.getBuyerOrderID();
            String query = "DELETE FROM buyer_order_product WHERE buyer_order_id=?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, buyerOrderID);
            preparedStmt.executeUpdate();

            query = "DELETE FROM buyer_order WHERE buyer_order_id=?";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, buyerOrderID);
            preparedStmt.executeUpdate();

            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String updateBuyerOrderPromotion(BuyerOrder buyerOrder) {
        try {
            int buyerOrderID = buyerOrder.getBuyerOrderID();
            String buyerOrderPromotionName = buyerOrder.getBuyerOrderPromotionName();
            int buyerOrderPromotionType = buyerOrder.getBuyerOrderPromotionType();
            double buyerOrderPromotionAmount = buyerOrder.getBuyerOrderPromotionAmount();

            String query = "SELECT buyer_order_promotion_id "
                    + "FROM buyer_order "
                    + "NATURAL JOIN buyer_order_promotion "
                    + "WHERE buyer_order_promotion_name='" + buyerOrderPromotionName + "' "
                    + "AND buyer_order_promotion_type='" + buyerOrderPromotionType + "' "
                    + "AND buyer_order_promotion_amount='" + buyerOrderPromotionAmount + "' "
                    + "AND buyer_order_id='" + buyerOrderID + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                buyerOrder.setBuyerOrderPromotionID(rs.getInt("buyer_order_promotion_id"));
            } else {
                query = "SELECT buyer_order_promotion_id "
                        + "FROM buyer_order_promotion "
                        + "WHERE buyer_order_promotion_name='" + buyerOrderPromotionName + "' "
                        + "AND buyer_order_promotion_type='" + buyerOrderPromotionType + "' "
                        + "AND buyer_order_promotion_amount='" + buyerOrderPromotionAmount + "'";
                rs = stmt.executeQuery(query);
                if (!rs.next()) {
                    query = "INSERT INTO buyer_order_promotion(buyer_order_promotion_name,buyer_order_promotion_type,buyer_order_promotion_amount) "
                            + "VALUES (?,?,?)";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setString(1, buyerOrderPromotionName);
                    preparedStmt.setInt(2, buyerOrderPromotionType);
                    preparedStmt.setDouble(3, buyerOrderPromotionAmount);
                    preparedStmt.executeUpdate();
                }
                query = "SELECT buyer_order_promotion_id "
                        + "FROM buyer_order_promotion "
                        + "WHERE buyer_order_promotion_name='" + buyerOrderPromotionName + "' "
                        + "AND buyer_order_promotion_type='" + buyerOrderPromotionType + "' "
                        + "AND buyer_order_promotion_amount='" + buyerOrderPromotionAmount + "'";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    query = "UPDATE buyer_order SET buyer_order_promotion_id=? WHERE buyer_order_id=?";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, rs.getInt("buyer_order_promotion_id"));
                    preparedStmt.setInt(2, buyerOrderID);
                    preparedStmt.executeUpdate();
                    buyerOrder.setBuyerOrderPromotionID(rs.getInt("buyer_order_promotion_id"));
                } else {
                    return "error_968";
                }
            }
            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String getBuyerOrder(BuyerOrder buyerOrder) {
        try {
            int buyerOrderID = buyerOrder.getBuyerOrderID();
            if (buyerOrderID != 0) {
                String query = "SELECT * FROM buyer_order NATURAL JOIN buyer_order_promotion WHERE buyer_order_id='" + buyerOrderID + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    buyerOrder.setMemberID(rs.getInt("member_id"));
                    buyerOrder.setShopID(rs.getInt("shop_id"));
                    buyerOrder.setBuyerOrderPromotionID(rs.getInt("buyer_order_promotion_id"));
                    buyerOrder.setBuyerOrderPromotionName(rs.getString("buyer_order_promotion_name"));
                    buyerOrder.setBuyerOrderPromotionType(rs.getInt("buyer_order_promotion_type"));
                    buyerOrder.setBuyerOrderPromotionAmount(rs.getInt("buyer_order_promotion_amount"));
                    buyerOrder.setTotalPrice(rs.getDouble("total_price"));
                    buyerOrder.setShippingName(rs.getString("shipping_name"));
                    buyerOrder.setShippingContactNumber(rs.getInt("shipping_contact_number"));
                    buyerOrder.setShippingAddress(rs.getString("shipping_address"));
                    buyerOrder.setStatus(rs.getInt("status"));
                    buyerOrder.setDatetime(rs.getTimestamp("datetime"));

                    query = "SELECT COUNT(*) as row_count, product_id, product_type_id, product_type_name, quantity, product_price "
                            + "FROM buyer_order_product "
                            + "NATURAL JOIN product_type "
                            + "WHERE buyer_order_id='" + buyerOrderID + "' "
                            + "GROUP BY product_id";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        int index = 0;
                        BuyerOrderProduct[] buyerOrderProducts = new BuyerOrderProduct[rs.getInt("row_count")];
                        do {
                            buyerOrderProducts[index].setProductID(rs.getInt("product_id"));
                            buyerOrderProducts[index].setProductTypeID(rs.getInt("product_type_id"));
                            buyerOrderProducts[index].setProductTypeName(rs.getString("product_type_name"));
                            buyerOrderProducts[index].setQuantity(rs.getInt("quantity"));
                            buyerOrderProducts[index].setProductPrice(rs.getDouble("product_price"));
                        } while (rs.next());
                        buyerOrder.setBuyerOrderProducts(buyerOrderProducts);
                        return "success";
                    }
                } else {
                    query = "SELECT * FROM buyer_order WHERE buyer_order_id='" + buyerOrderID + "'";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        buyerOrder.setMemberID(rs.getInt("member_id"));
                        buyerOrder.setShopID(rs.getInt("shop_id"));
                        buyerOrder.setBuyerOrderPromotionID(rs.getInt("buyer_order_promotion_id"));
                        buyerOrder.setTotalPrice(rs.getDouble("total_price"));
                        buyerOrder.setShippingName(rs.getString("shipping_name"));
                        buyerOrder.setShippingContactNumber(rs.getInt("shipping_contact_number"));
                        buyerOrder.setShippingAddress(rs.getString("shipping_address"));
                        buyerOrder.setStatus(rs.getInt("status"));
                        buyerOrder.setDatetime(rs.getTimestamp("datetime"));

                        query = "SELECT COUNT(*) as row_count, product_id, product_type_id, product_type_name, quantity, product_price "
                                + "FROM buyer_order_product "
                                + "NATURAL JOIN product_type "
                                + "WHERE buyer_order_id='" + buyerOrderID + "' "
                                + "GROUP BY product_id";
                        rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            int index = 0;
                            BuyerOrderProduct[] buyerOrderProducts = new BuyerOrderProduct[rs.getInt("row_count")];
                            do {
                                buyerOrderProducts[index] = new BuyerOrderProduct();
                                buyerOrderProducts[index].setProductID(rs.getInt("product_id"));
                                buyerOrderProducts[index].setProductTypeID(rs.getInt("product_type_id"));
                                buyerOrderProducts[index].setProductTypeName(rs.getString("product_type_name"));
                                buyerOrderProducts[index].setQuantity(rs.getInt("quantity"));
                                buyerOrderProducts[index++].setProductPrice(rs.getDouble("product_price"));
                            } while (rs.next());
                            buyerOrder.setBuyerOrderProducts(buyerOrderProducts);
                            return "success";
                        }
                    }
                }
            }
            return "order_id_not_found";
        } catch (SQLException E) {
            return "error_507" + E.toString();
        }
    }

    public BuyerOrder[] getBuyerOrdersByColumnName(String columnName) {
        try {
            String query = "SELECT COUNT(*) as row_count FROM buyer_order";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                BuyerOrder[] buyerOrders = new BuyerOrder[rs.getInt("row_count")];
                query = "SELECT buyer_order_id FROM buyer_order ORDER BY " + columnName + " ASC";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    for (int i = 0; i < buyerOrders.length; i++, rs.next()) {
                        buyerOrders[i] = new BuyerOrder();
                        buyerOrders[i].setBuyerOrderID(rs.getInt("buyer_order_id"));
                        getBuyerOrder(buyerOrders[i]);
                    }
                }
                return buyerOrders;
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public BuyerOrder[] getBuyerOrdersByMemberID(Member member) {
        try {
            int memberID = member.getMemberID();
            String query = "SELECT COUNT(*) as row_count FROM buyer_order WHERE member_id='" + memberID + "' ORDER BY datetime DESC";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                BuyerOrder[] buyerOrders = new BuyerOrder[rs.getInt("row_count")];
                query = "SELECT buyer_order_id FROM buyer_order WHERE member_id='" + memberID + "' ORDER BY datetime DESC";
                rs = stmt.executeQuery(query);
                System.out.println(buyerOrders.length);
                for (int i = 0; i < buyerOrders.length; i++) {
                    System.out.println(1);
                    if (rs.next()) {
                        System.out.println(2);
                        buyerOrders[i] = new BuyerOrder();
                        System.out.println(3);
                        buyerOrders[i].setMemberID(memberID);
                        System.out.println(4);
                        buyerOrders[i].setBuyerOrderID(rs.getInt("buyer_order_id"));
                        System.out.println(5);
                        getBuyerOrder(buyerOrders[i]);
                        System.out.println(6);
                    }
                }
                return buyerOrders;
            }
        } catch (SQLException E) {
        }
        return null;
    }
}
