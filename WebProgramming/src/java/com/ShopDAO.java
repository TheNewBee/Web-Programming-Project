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
public class ShopDAO {

    private Connection conn;
    private Statement stmt;

    public ShopDAO() {
        conn = new DBConnection().getConnection();
        try {
            stmt = conn.createStatement();
        } catch (SQLException E) {
        }
    }

    public String createShop(Shop shop) {
        try {
            String shopName = shop.getShopName();
            String query = "SELECT shop_id FROM shop WHERE shop_name=\"" + shopName + "\"";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return "shop_name_has_been_register";
            } else {
                if (!"".equals(shop.getShopCategory().getShopCategoryName())) {
                    updateShopCategory(shop);
                }
                query = "INSERT INTO shop(shop_name, shop_introduction, shop_detail, shop_category_id, shop_level, member_id, contact_number, contact_email, website, amount_sold, disabled, created_on, last_modified) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString(1, shopName);
                preparedStmt.setString(2, shop.getShopIntroduction());
                preparedStmt.setString(3, shop.getShopDetail());
                preparedStmt.setInt(4, shop.getShopCategory().getShopCategoryID());
                preparedStmt.setDouble(5, shop.getShopLevel());
                preparedStmt.setInt(6, shop.getMemberID());
                preparedStmt.setInt(7, shop.getContractNumber());
                preparedStmt.setString(8, shop.getContactEmail());
                preparedStmt.setString(9, shop.getWebsite());
                preparedStmt.setInt(10, shop.getAmountSold());
                preparedStmt.setInt(11, (shop.getDisabled() ? 1 : 0));
                preparedStmt.setTimestamp(12, shop.getCreatedOn());
                preparedStmt.setTimestamp(13, shop.getLastModified());
                preparedStmt.executeUpdate();

                query = "SELECT shop_id FROM shop WHERE shop_name='" + shopName + "'";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    int shopID = rs.getInt("shop_id");
                    shop.setShopID(shopID);
                    String[] shopTabNames = shop.getShopTabNames();
                    int[] shopTabIDs = new int[shopTabNames.length];
                    int iShopTabIDs = 0;
                    for (String shopTabName : shopTabNames) {
                        query = "SELECT shop_tab_id FROM shop_tab WHERE shop_tab_name='" + shopTabName + "'";
                        rs = stmt.executeQuery(query);
                        if (!rs.next()) {
                            query = "INSERT INTO shop_tab(shop_tab_name) "
                                    + "VALUES (?)";
                            preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setString(1, shopTabName);
                            preparedStmt.executeUpdate();
                        }
                        query = "SELECT shop_tab_id FROM shop_tab WHERE shop_tab_name='" + shopTabName + "'";
                        rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            shopTabIDs[iShopTabIDs] = rs.getInt("shop_tab_id");
                            query = "INSERT INTO shop_shop_tab(shop_id, shop_tab_id) "
                                    + "VALUES (?,?)";
                            preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setInt(1, shopID);
                            preparedStmt.setInt(2, shopTabIDs[iShopTabIDs++]);
                            preparedStmt.executeUpdate();
                        } else {
                            return "error_968";
                        }
                    }
                    shop.setShopTabIDs(shopTabIDs);

                    query = "UPDATE member SET has_shop=1 WHERE member_id=?";
                    preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, shop.getMemberID());
                    preparedStmt.executeUpdate();
                }
            }
            return "success";
        } catch (SQLException E) {
            return "error_507" + E.toString();
        }
    }

    public String updateShopCategory(Shop shop) {
        try {
            int shopID = shop.getShopID();
            String shopCategoryName = shop.getShopCategory().getShopCategoryName();

            // Check whether is this change
            String query = "SELECT shop_category_id "
                    + "FROM shop "
                    + "NATURAL JOIN shop_category "
                    + "WHERE shop_category_name='" + shopCategoryName + "' "
                    + "AND shop_id='" + shopID + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                shop.getShopCategory().setShopCategoryID(rs.getInt("shop_category_id"));
            } else {
                query = "SELECT shop_category_id FROM shop_category WHERE shop_category_name='" + shopCategoryName + "'";
                rs = stmt.executeQuery(query);
                if (!rs.next()) {
                    query = "INSERT INTO shop_category(shop_category_name) "
                            + "VALUES (?)";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setString(1, shopCategoryName);
                    preparedStmt.executeUpdate();
                }
                query = "SELECT shop_category_id FROM shop_category WHERE shop_category_name='" + shopCategoryName + "'";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    query = "UPDATE shop SET shop_category_id=? WHERE shop_id=?";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, rs.getInt("shop_category_id"));
                    preparedStmt.setInt(2, shopID);
                    preparedStmt.executeUpdate();
                    shop.getShopCategory().setShopCategoryID(rs.getInt("shop_category_id"));
                } else {
                    return "error_968";
                }
            }
            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String deleteShop(Shop shop) {
        try {
            int shopID = shop.getShopID();
            String query = "DELETE FROM shop_shop_tab WHERE shop_id=?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, shopID);
            preparedStmt.executeUpdate();

            query = "DELETE FROM shop WHERE shop_id=?";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, shopID);
            preparedStmt.executeUpdate();

            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String getShop(Shop shop) {
        try {
            int shopID = shop.getShopID();
            String shopName = shop.getShopName();
            if (shopID == 0 && "".equals(shopName)) {
                return "no_key_found";
            } else {
                if (shopID == 0) {
                    String query = "SELECT shop_id FROM shop WHERE shop_name='" + shopName + "'";
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        shopID = rs.getInt("shop_id");
                    } else {
                        return "shop_name_not_found_in_database";
                    }
                }
                String query = "SELECT * FROM shop NATURAL JOIN shop_category WHERE shop_id=" + shopID;
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    shopName = rs.getString("shop_name");
                    shop.setShopID(shopID);
                    shop.setShopName(shopName);
                    shop.setShopIntroduction(rs.getString("shop_introduction"));
                    shop.setShopDetail(rs.getString("shop_detail"));
                    shop.setShopLevel(rs.getInt("shop_level"));
                    shop.setMemberID(rs.getInt("member_id"));
                    shop.setContractNumber(rs.getInt("contact_number"));
                    shop.setContractEmail(rs.getString("contact_email"));
                    shop.setWebsite(rs.getString("website"));
                    shop.setAmountSold(rs.getInt("amount_sold"));
                    shop.setDiabled(rs.getInt("disabled") == 1);
                    shop.setCreatedOn(rs.getTimestamp("created_on"));
                    shop.setLastModified(rs.getTimestamp("last_modified"));

                    ShopCategory shopCategory = new ShopCategory();
                    shopCategory.setShopCategoryID(rs.getInt("shop_category_id"));
                    shopCategory.setShopCategoryName(rs.getString("shop_category_name"));
                    shop.setShopCategory(shopCategory);

                    query = "SELECT COUNT(*) as row_count, shop_tab_id, shop_tab_name "
                            + "FROM shop_shop_tab "
                            + "NATURAL JOIN shop_tab "
                            + "WHERE shop_id='" + shopID + "' "
                            + "GROUP BY shop_tab_id";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        int index = 0;
                        int[] shopTabIDs = new int[rs.getInt("row_count")];
                        String[] shopTabNames = new String[rs.getInt("row_count")];
                        for (int i = 0; i < rs.getInt("row_count") - 1; i++) {
                            if (rs.next()) {
                                shopTabIDs[index] = rs.getInt("shop_tab_id");
                                shopTabNames[index++] = rs.getString("shop_tab_name");
                            }
                        }
                        shop.setShopTabIDs(shopTabIDs);
                        shop.setShopTabNames(shopTabNames);
                    }
                    return "success";
                } else {
                    return "no_shop_found";
                }
            }
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public Shop getShopByMember(Member member) {
        try {
            int memberID = member.getMemberID();
            if (memberID > 0) {
                String query = "SELECT shop_id FROM shop WHERE member_id='" + memberID + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    Shop shop = new Shop();
                    shop.setShopID(rs.getInt("shop_id"));
                    getShop(shop);
                    return shop;
                }
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public Shop[] getShops() {
        try {
            String query = "SELECT COUNT(*) as row_count FROM shop";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                Shop[] shops = new Shop[rs.getInt("row_count")];
                query = "SELECT shop_id FROM shop";
                rs = stmt.executeQuery(query);
                for (int i = 0; i < shops.length; i++, rs.next()) {
                    shops[i] = new Shop();
                    shops[i].setShopID(rs.getInt("product_id"));
                    getShop(shops[i]);
                }
                return shops;
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public Shop[] getShopsByCategory(ShopCategory shopCategory) {
        try {
            int shopCategoryID = shopCategory.getShopCategoryID();
            if (shopCategoryID != 0) {
                String query = "SELECT COUNT(*) as row_count FROM shop WHERE shop_category_id='" + shopCategoryID + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    Shop[] shops = new Shop[rs.getInt("row_count")];
                    query = "SELECT shop_id FROM shop WHERE shop_category_id='" + shopCategoryID + "'";
                    rs = stmt.executeQuery(query);
                    for (int i = 0; i < shops.length; i++, rs.next()) {
                        shops[i] = new Shop();
                        shops[i].setShopID(rs.getInt("shop_id"));
                        getShop(shops[i]);
                    }
                    return shops;
                }
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public ShopCategory[] getShopCategories() {
        try {
            String query = "SELECT COUNT(*) as row_count FROM shop_category";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                ShopCategory[] shopCategory = new ShopCategory[rs.getInt("row_count")];
                query = "SELECT shop_category_id, shop_category_name FROM shop_category";
                rs = stmt.executeQuery(query);
                for (int i = 0; i < shopCategory.length; i++) {
                    if (rs.next()) {
                        shopCategory[i] = new ShopCategory();
                        shopCategory[i].setShopCategoryID(rs.getInt("shop_category_id"));
                        shopCategory[i].setShopCategoryName(rs.getString("shop_category_name"));
                    }
                }
                return shopCategory;
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public BuyerOrder[] getShopBuyerOrders(Shop shop) {
        try {
            BuyerOrderDAO buyerOrderDAO = new BuyerOrderDAO();
            int shopID = shop.getShopID();
            if (shopID != 0) {
                String query = "SELECT COUNT(*) as row_count FROM buyer_order WHERE shop_id='" + shopID + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    BuyerOrder[] buyerOrders = new BuyerOrder[rs.getInt("row_count")];
                    query = "SELECT buyer_order_id FROM buyer_order WHERE shop_id='" + shopID + "'";
                    rs = stmt.executeQuery(query);
                    for (int i = 0; i < buyerOrders.length; i++, rs.next()) {
                        buyerOrders[i] = new BuyerOrder();
                        buyerOrders[i].setBuyerOrderID(rs.getInt("buyer_order_id"));
                        buyerOrderDAO.getBuyerOrder(buyerOrders[i]);
                    }
                    return buyerOrders;
                }
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public Member[] getBoughtMembers(Shop shop) {
        try {
            MemberDAO memberDAO = new MemberDAO();
            int shopID = shop.getShopID();
            if (shopID != 0) {
                String query = "SELECT COUNT(*) as row_count "
                        + "FROM buyer_order "
                        + "WHERE shop_id='" + shopID + "' "
                        + "GROUP BY member_id";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    Member[] boughtMembers = new Member[rs.getInt("row_count")];
                    query = "SELECT member_id "
                            + "FROM buyer_order "
                            + "WHERE shop_id='" + shopID + "' "
                            + "GROUP BY member_id";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        for (int i = 0; i < boughtMembers.length; i++, rs.next()) {
                            boughtMembers[i] = new Member();
                            boughtMembers[i].setMemberID(rs.getInt("member_id"));
                            memberDAO.getMember(boughtMembers[i]);
                        }
                    }
                    return boughtMembers;
                }
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public int getTotalProductsSold(Shop shop) {
        try {
            int shopID = shop.getShopID();
            if (shopID != 0) {
                String query = "SELECT SUM(quantity) AS total_products_sold "
                        + "FROM buyer_order_product "
                        + "WHERE buyer_order_id= "
                        + " (SELECT buyer_order_id "
                        + " FROM buyer_order "
                        + " WHERE shop_id='" + shopID + "')";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    return rs.getInt("total_products_sold");
                }
            }
        } catch (SQLException E) {
        }
        return -1;
    }
}
