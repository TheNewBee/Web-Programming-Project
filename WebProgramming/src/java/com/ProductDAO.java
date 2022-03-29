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
public class ProductDAO {

    private Connection conn;
    private Statement stmt;

    public ProductDAO() {
        conn = new DBConnection().getConnection();
        try {
            stmt = conn.createStatement();
        } catch (SQLException E) {
        }
    }

    public String createProduct(Product product) {
        try {
            if (product.getShopID() > 0) {
                String query = "SELECT product_id FROM product WHERE product_name='" + product.getProductName() + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    return "product_name_has_been_register";
                } else {
                    ProductPhoto[] productPhotos = product.getProductPhotos();
                    for (ProductPhoto productPhoto : productPhotos) {
                        query = "SELECT product_id FROM product_photo WHERE product_photo_path='" + productPhoto.getProductPhotoPath() + "'";
                        rs = stmt.executeQuery(query);
                        if (rs.next()) {
                            return "photo_path_has_been_register";
                        }
                    }

                    if (!"".equals(product.getProductPromotionName())) {
                        updateProductPromotion(product);
                    }
                    if (!"".equals(product.getProductCategory().getProductCategoryName())) {
                        updateProductCategory(product);
                    }

                    String productName = product.getProductName();
                    query = "INSERT INTO product(shop_id, product_name, product_introduction, "
                            + "product_detail, product_category_id, product_price, product_promotion_id, "
                            + "amount_stock, amount_sold, status, added_on, last_modified) "
                            + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, product.getShopID());
                    preparedStmt.setString(2, productName);
                    preparedStmt.setString(3, product.getProductIntroduction());
                    preparedStmt.setString(4, product.getProductDetail());
                    preparedStmt.setInt(5, product.getProductCategory().getProductCategoryID());
                    preparedStmt.setDouble(6, product.getProductPrice());
                    preparedStmt.setInt(7, product.getProductPromotionID());
                    preparedStmt.setInt(8, product.getAmountStock());
                    preparedStmt.setInt(9, product.getAmountSold());
                    preparedStmt.setInt(10, product.getStatus());
                    preparedStmt.setTimestamp(11, product.getAddedOn());
                    preparedStmt.setTimestamp(12, product.getLastModified());
                    preparedStmt.executeUpdate();

                    query = "SELECT product_id FROM product WHERE product_name='" + productName + "'";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        int productID = rs.getInt("product_id");
                        product.setProductID(productID);
                        for (ProductPhoto productPhoto : productPhotos) {
                            query = "INSERT INTO product_photo(product_id, product_photo_path, status, added_on, last_modified) "
                                    + "VALUES (?,?,?,?,?)";
                            preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setInt(1, productID);
                            preparedStmt.setString(2, productPhoto.getProductPhotoPath());
                            preparedStmt.setInt(3, productPhoto.getStatus());
                            preparedStmt.setTimestamp(4, productPhoto.getAddedOn());
                            preparedStmt.setTimestamp(5, productPhoto.getLastModified());
                            preparedStmt.executeUpdate();
                        }

                        String[] productTabNames = product.getProductTabNames();
                        int[] productTabIDs = new int[productTabNames.length];
                        int iProductTabIDs = 0;
                        for (String productTabName : productTabNames) {
                            query = "SELECT product_tab_id FROM product_tab WHERE product_tab_name='" + productTabName + "'";
                            rs = stmt.executeQuery(query);
                            if (!rs.next()) {

                                query = "INSERT INTO product_tab(product_tab_name) "
                                        + "VALUES (?)";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setString(1, productTabName);
                                preparedStmt.executeUpdate();
                            }
                            query = "SELECT product_tab_id FROM product_tab WHERE product_tab_name='" + productTabName + "'";
                            rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                productTabIDs[iProductTabIDs] = rs.getInt("product_tab_id");
                                query = "INSERT INTO product_product_tab(product_id, product_tab_id) "
                                        + "VALUES (?,?)";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setInt(1, productID);
                                preparedStmt.setInt(2, productTabIDs[iProductTabIDs++]);
                                preparedStmt.executeUpdate();
                            } else {
                                return "error_968";
                            }
                        }
                        product.setProductTabIDs(productTabIDs);

                        String[] productTypeNames = product.getProductTypeNames();
                        int[] productTypeIDs = new int[productTypeNames.length];
                        int iProductTypeIDs = 0;
                        for (String productTypeName : productTypeNames) {
                            query = "SELECT product_type_id FROM product_type WHERE product_type_name='" + productTypeName + "'";
                            rs = stmt.executeQuery(query);
                            if (!rs.next()) {
                                query = "INSERT INTO product_type(product_type_name) "
                                        + "VALUES (?)";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setString(1, productTypeName);
                                preparedStmt.executeUpdate();
                            }
                            query = "SELECT product_type_id FROM product_type WHERE product_type_name='" + productTypeName + "'";
                            rs = stmt.executeQuery(query);
                            if (rs.next()) {
                                productTypeIDs[iProductTypeIDs] = rs.getInt("product_type_id");
                                query = "INSERT INTO product_product_type(product_id, product_type_id) "
                                        + "VALUES (?,?)";
                                preparedStmt = conn.prepareStatement(query);
                                preparedStmt.setInt(1, productID);
                                preparedStmt.setInt(2, productTypeIDs[iProductTypeIDs++]);
                                preparedStmt.executeUpdate();
                            } else {
                                return "error_968";
                            }
                        }
                        product.setProductTypeIDs(productTypeIDs);
                        return "success";
                    } else {
                        return "error_304";
                    }
                }
            } else {
                return "no_shop_found";
            }
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String deleteProduct(Product product) {
        try {
            int productID = product.getProductID();
            String query = "DELETE FROM product_product_tab WHERE product_id=?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, productID);
            preparedStmt.executeUpdate();

            query = "DELETE FROM product_product_type WHERE product_id=?";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, productID);
            preparedStmt.executeUpdate();

            query = "DELETE FROM product_photo WHERE product_id=?";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, productID);
            preparedStmt.executeUpdate();

            query = "DELETE FROM product_comment WHERE product_id=?";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, productID);
            preparedStmt.executeUpdate();

            query = "DELETE FROM cart_product WHERE product_id=?";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, productID);
            preparedStmt.executeUpdate();

            query = "DELETE FROM product WHERE product_id=?";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, productID);
            preparedStmt.executeUpdate();

            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String getProduct(Product product) {
        try {
            int productID = product.getProductID();
            String productName = product.getProductName();
            if (productID == 0 && "".equals(productName)) {
                return "no_key_found";
            } else {
                if (productID == 0) {
                    String query = "SELECT product_id FROM product WHERE product_name='" + productName + "'";
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        productID = rs.getInt("product_id");
                    } else {
                        return "product_name_not_found_in_database";
                    }
                }
                String query = "SELECT * FROM product "
                        + "NATURAL JOIN product_category "
                        + "NATURAL JOIN product_promotion "
                        + "WHERE product_id='" + productID + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    productName = rs.getString("product_name");
                    product.setProductID(productID);
                    product.setShopID(rs.getInt("shop_id"));
                    product.setProductName(productName);
                    product.setProductIntroduction(rs.getString("product_introduction"));
                    product.setProductDetail(rs.getString("product_detail"));
                    product.setProductPrice(rs.getDouble("product_price"));
                    product.setProductPromotionID(rs.getInt("product_promotion_id"));
                    product.setProductPromotionName(rs.getString("product_promotion_name"));
                    product.setProductPromotionType(rs.getInt("product_promotion_type"));
                    product.setProductPromotionAmount(rs.getDouble("product_promotion_amount"));
                    product.setAmountStock(rs.getInt("amount_stock"));
                    product.setAmountSold(rs.getInt("amount_sold"));
                    product.setStatus(rs.getInt("status"));
                    product.setAddedOn(rs.getTimestamp("added_on"));
                    product.setLastModified(rs.getTimestamp("last_modified"));

                    ProductCategory productCategory = new ProductCategory();
                    productCategory.setProductCategoryID(rs.getInt("product_category_id"));
                    productCategory.setProductCategoryName(rs.getString("product_category_name"));
                    product.setProductCategory(productCategory);

                    query = "SELECT COUNT(*) as row_count "
                            + "FROM product_product_tab "
                            + "NATURAL JOIN product_tab "
                            + "WHERE product_id='" + productID + "'";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        int index = 0;
                        int[] productTabIDs = new int[rs.getInt("row_count")];
                        String[] productTabNames = new String[rs.getInt("row_count")];
                        query = "SELECT product_tab_id, product_tab_name "
                                + "FROM product_product_tab "
                                + "NATURAL JOIN product_tab "
                                + "WHERE product_id='" + productID + "'";
                        rs = stmt.executeQuery(query);
                        for (int i = 0; i < productTabIDs.length; i++) {
                            if (rs.next()) {
                                productTabIDs[index] = rs.getInt("product_tab_id");
                                productTabNames[index++] = rs.getString("product_tab_name");
                            }
                        }
                        product.setProductTabIDs(productTabIDs);
                        product.setProductTabNames(productTabNames);
                    }

                    query = "SELECT COUNT(*) as row_count "
                            + "FROM product_product_type "
                            + "NATURAL JOIN product_type "
                            + "WHERE product_id='" + productID + "'";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        int index = 0;
                        int[] productTypeIDs = new int[rs.getInt("row_count")];
                        String[] productTypeNames = new String[rs.getInt("row_count")];
                        query = "SELECT COUNT(*) as row_count, product_type_id, product_type_name "
                                + "FROM product_product_type "
                                + "NATURAL JOIN product_type "
                                + "WHERE product_id='" + productID + "' "
                                + "GROUP BY product_type_id";
                        rs = stmt.executeQuery(query);
                        for (int i = 0; i < productTypeIDs.length - 1; i++) {
                            if (rs.next()) {
                                productTypeIDs[index] = rs.getInt("product_type_id");
                                productTypeNames[index++] = rs.getString("product_type_name");
                            }
                        }
                        product.setProductTypeIDs(productTypeIDs);
                        product.setProductTypeNames(productTypeNames);
                    }

                    query = "SELECT COUNT(*) as row_count, member_id, comment_details, product_rating, status, added_on, last_modified "
                            + "FROM product_comment "
                            + "WHERE product_id='" + productID + "' "
                            + "GROUP BY member_id";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        int index = 0;
                        ProductComment[] productComments = new ProductComment[rs.getInt("row_count")];
                        productComments[index] = new ProductComment();
                        productComments[index].setMemberID(rs.getInt("member_id"));
                        productComments[index].setCommentDetails(rs.getString("comment_details"));
                        productComments[index].setProductRating(rs.getInt("product_rating"));
                        productComments[index].setStatus(rs.getInt("status"));
                        productComments[index].setAddedOn(rs.getTimestamp("added_on"));
                        productComments[index++].setLastModified(rs.getTimestamp("last_modified"));
                        for (int i = 0; i < productComments.length - 1; i++) {
                            if (rs.next()) {
                                productComments[index] = new ProductComment();
                                productComments[index].setMemberID(rs.getInt("member_id"));
                                productComments[index].setCommentDetails(rs.getString("comment_details"));
                                productComments[index].setProductRating(rs.getInt("product_rating"));
                                productComments[index].setStatus(rs.getInt("status"));
                                productComments[index].setAddedOn(rs.getTimestamp("added_on"));
                                productComments[index++].setLastModified(rs.getTimestamp("last_modified"));
                            }
                        }
                        product.setProductComments(productComments);
                    }

                    query = "SELECT COUNT(*) as row_count, product_photo_path, status, added_on, last_modified "
                            + "FROM product_photo "
                            + "WHERE product_id='" + productID + "' "
                            + "GROUP BY product_photo_path";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        int index = 0;
                        ProductPhoto[] productPhotos = new ProductPhoto[rs.getInt("row_count")];
                        productPhotos[index] = new ProductPhoto();
                        productPhotos[index].setProductPhotoPath(rs.getString("product_photo_path"));
                        productPhotos[index].setStatus(rs.getInt("status"));
                        productPhotos[index].setAddedOn(rs.getTimestamp("added_on"));
                        productPhotos[index++].setLastModified(rs.getTimestamp("last_modified"));
                        for (int i = 0; i < productPhotos.length - 1; i++) {
                            if (rs.next()) {
                                productPhotos[index] = new ProductPhoto();
                                productPhotos[index].setProductPhotoPath(rs.getString("product_photo_path"));
                                productPhotos[index].setStatus(rs.getInt("status"));
                                productPhotos[index].setAddedOn(rs.getTimestamp("added_on"));
                                productPhotos[index++].setLastModified(rs.getTimestamp("last_modified"));
                            }
                        }
                        product.setProductPhotos(productPhotos);
                    }
                }
            }
            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public Product[] getProducts() {
        try {
            String query = "SELECT COUNT(*) as row_count FROM product";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                Product[] products = new Product[rs.getInt("row_count")];
                query = "SELECT product_id FROM product";
                rs = stmt.executeQuery(query);
                for (int i = 0; i < products.length; i++) {
                    if (rs.next()) {
                        products[i] = new Product();
                        products[i].setProductID(rs.getInt("product_id"));
                        getProduct(products[i]);
                    }
                }
                return products;
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public Product[] getProductsByShopID(int shopID) {
        try {
            if (shopID != 0) {
                String query = "SELECT COUNT(*) as row_count FROM product WHERE shop_id='" + shopID + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    Product[] products = new Product[rs.getInt("row_count")];
                    query = "SELECT product_id FROM product WHERE shop_id='" + shopID + "'";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        for (int i = 0; i < products.length; i++, rs.next()) {
                            products[i] = new Product();
                            products[i].setProductID(rs.getInt("product_id"));
                            getProduct(products[i]);
                        }
                    }
                    return products;
                }
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public Product[] getProductsByProductCategory(ProductCategory productCategory) {
        try {
            int productCategoryID = productCategory.getProductCategoryID();
            if (productCategoryID != 0) {
                String query = "SELECT COUNT(*) as row_count FROM product WHERE product_category_id='" + productCategoryID + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    Product[] products = new Product[rs.getInt("row_count")];
                    query = "SELECT product_id FROM product WHERE product_category_id='" + productCategoryID + "'";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        for (int i = 0; i < products.length; i++, rs.next()) {
                            products[i] = new Product();
                            products[i].setProductID(rs.getInt("product_id"));
                            getProduct(products[i]);
                        }
                    }
                    return products;
                }
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public Product[] getProductsByProductNameKey(String productNameKey) {
        try {
            if (!"".equals(productNameKey)) {
                String query = "SELECT COUNT(*) as row_count FROM product WHERE product_name LIKE '%" + productNameKey + "%'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    Product[] products = new Product[rs.getInt("row_count")];
                    query = "SELECT product_id FROM product WHERE product_name LIKE '%" + productNameKey + "%'";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        for (int i = 0; i < products.length; i++, rs.next()) {
                            products[i] = new Product();
                            products[i].setProductID(rs.getInt("product_id"));
                            getProduct(products[i]);
                        }
                    }
                    return products;
                }
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public Product[] getProductsByProductTab(int productTabID, String productTabName) {
        try {
            if (productTabID != 0 || !"".equals(productTabName)) {
                if (productTabID == 0) {
                    String query = "SELECT product_tab_id FROM product_tab WHERE product_tab_name='" + productTabName + "'";
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        productTabID = rs.getInt("product_id");
                    }
                }

                String query = "SELECT COUNT(*) as row_count FROM product_product_tab WHERE product_tab_id='" + productTabID + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    Product[] products = new Product[rs.getInt("row_count")];
                    query = "SELECT product_id FROM product_product_tab WHERE product_tab_id='" + productTabID + "'";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        for (int i = 0; i < products.length; i++, rs.next()) {
                            products[i] = new Product();
                            products[i].setProductID(rs.getInt("product_id"));
                            getProduct(products[i]);
                        }
                    }
                    return products;
                }
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public ProductCategory[] getProductCategories() {
        try {
            String query = "SELECT COUNT(*) as row_count FROM product_category";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                ProductCategory[] productCategory = new ProductCategory[rs.getInt("row_count")];
                query = "SELECT product_category_id, product_category_name FROM product_category";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    for (int i = 0; i < productCategory.length; i++, rs.next()) {
                        productCategory[i] = new ProductCategory();
                        productCategory[i].setProductCategoryID(rs.getInt("product_category_id"));
                        productCategory[i].setProductCategoryName(rs.getString("product_category_name"));
                    }
                }
                return productCategory;
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public String updateProduct(Product product) {
        try {
            String query = "UPDATE product "
                    + "SET shop_id=?,product_name=?,product_introduction=?,product_detail=?,product_price=?,amount_stock=?,status=? "
                    + "WHERE product_id=?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, product.getShopID());
            preparedStmt.setString(2, product.getProductName());
            preparedStmt.setString(3, product.getProductIntroduction());
            preparedStmt.setString(4, product.getProductDetail());
            preparedStmt.setDouble(5, product.getProductPrice());
            preparedStmt.setInt(6, product.getAmountStock());
            preparedStmt.setInt(7, product.getStatus());
            preparedStmt.setInt(8, product.getProductID());
            preparedStmt.executeUpdate();
            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String updateProductTabs(Product product) {
        try {
            int productID = product.getProductID();
            int[] productTabIDs = product.getProductTabIDs();
            String[] productTabNames = product.getProductTabNames();

            for (int i = 0; i < productTabIDs.length; i++) {
                String query = "SELECT product_tab_id "
                        + "FROM product_product_tab "
                        + "NATURAL JOIN product_tab "
                        + "WHERE product_tab_name='" + productTabNames[i] + "' "
                        + "AND product_id='" + productID + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    productTabIDs[i] = rs.getInt("product_tab_id");
                } else {
                    query = "SELECT product_tab_id FROM product_tab WHERE product_tab_name='" + productTabNames[i] + "'";
                    rs = stmt.executeQuery(query);
                    if (!rs.next()) {
                        query = "INSERT INTO product_tab(product_tab_name) "
                                + "VALUES (?)";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setString(1, productTabNames[i]);
                        preparedStmt.executeUpdate();
                    }
                    query = "SELECT product_tab_id FROM product_tab WHERE product_tab_name='" + productTabNames[i] + "'";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        productTabIDs[i] = rs.getInt("product_tab_id");
                        query = "INSERT INTO product_product_tab(product_id, product_tab_id) "
                                + "VALUES (?,?)";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1, productID);
                        preparedStmt.setInt(2, productTabIDs[i]);
                        preparedStmt.executeUpdate();
                    } else {
                        return "error_968";
                    }
                }
            }

            product.setProductTabIDs(productTabIDs);
            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String updateProductTypes(Product product) {
        try {
            int productID = product.getProductID();
            int[] productTypeIDs = product.getProductTypeIDs();
            String[] productTypeNames = product.getProductTypeNames();

            for (int i = 0; i < productTypeIDs.length; i++) {
                String query = "SELECT product_type_id "
                        + "FROM product_product_type "
                        + "NATURAL JOIN product_type "
                        + "WHERE product_type_name='" + productTypeNames[i] + "' "
                        + "AND product_id='" + productID + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    productTypeIDs[i] = rs.getInt("product_type_id");
                } else {
                    query = "SELECT product_type_id FROM product_type WHERE product_type_name='" + productTypeNames[i] + "'";
                    rs = stmt.executeQuery(query);
                    if (!rs.next()) {
                        query = "INSERT INTO product_type(product_type_name) "
                                + "VALUES (?)";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setString(1, productTypeNames[i]);
                        preparedStmt.executeUpdate();
                    }
                    query = "SELECT product_type_id FROM product_type WHERE product_type_name='" + productTypeNames[i] + "'";
                    rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        productTypeIDs[i] = rs.getInt("product_type_id");
                        query = "INSERT INTO product_product_type(product_id, product_type_id) "
                                + "VALUES (?,?)";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1, productID);
                        preparedStmt.setInt(2, productTypeIDs[i]);
                        preparedStmt.executeUpdate();
                    } else {
                        return "error_968";
                    }
                }
            }

            product.setProductTypeIDs(productTypeIDs);
            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String updateProductCategory(Product product) {
        try {
            int productID = product.getProductID();
            String productCategoryName = product.getProductCategory().getProductCategoryName();

            // Check whether is this change
            String query = "SELECT product_category_id "
                    + "FROM product "
                    + "NATURAL JOIN product_category "
                    + "WHERE product_category_name='" + productCategoryName + "' "
                    + "AND product_id='" + productID + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                product.getProductCategory().setProductCategoryID(rs.getInt("product_category_id"));
            } else {
                query = "SELECT product_category_id FROM product_category WHERE product_category_name='" + productCategoryName + "'";
                rs = stmt.executeQuery(query);
                if (!rs.next()) {
                    query = "INSERT INTO product_category(product_category_name) "
                            + "VALUES (?)";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setString(1, productCategoryName);
                    preparedStmt.executeUpdate();
                }
                query = "SELECT product_category_id FROM product_category WHERE product_category_name='" + productCategoryName + "'";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    query = "UPDATE product SET product_category_id=? WHERE product_id=?";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, rs.getInt("product_category_id"));
                    preparedStmt.setInt(2, productID);
                    preparedStmt.executeUpdate();
                    product.getProductCategory().setProductCategoryID(rs.getInt("product_category_id"));
                } else {
                    return "error_968";
                }
            }
            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String updateProductPromotion(Product product) {
        try {
            int productID = product.getProductID();
            String productPromotionName = product.getProductPromotionName();
            int productPromotionType = product.getProductPromotionType();
            double productPromotionAmount = product.getProductPromotionAmount();

            String query = "SELECT product_promotion_id "
                    + "FROM product "
                    + "NATURAL JOIN product_promotion "
                    + "WHERE product_promotion_name='" + productPromotionName + "' "
                    + "AND product_promotion_type='" + productPromotionType + "' "
                    + "AND product_promotion_amount='" + productPromotionAmount + "' "
                    + "AND product_id='" + productID + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                product.setProductPromotionID(rs.getInt("product_promotion_id"));
            } else {
                query = "SELECT product_promotion_id "
                        + "FROM product_promotion "
                        + "WHERE product_promotion_name='" + productPromotionName + "' "
                        + "AND product_promotion_type='" + productPromotionType + "' "
                        + "AND product_promotion_amount='" + productPromotionAmount + "'";
                rs = stmt.executeQuery(query);
                if (!rs.next()) {
                    query = "INSERT INTO product_promotion(product_promotion_name,product_promotion_type,product_promotion_amount) "
                            + "VALUES (?,?,?)";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setString(1, productPromotionName);
                    preparedStmt.setInt(2, productPromotionType);
                    preparedStmt.setDouble(3, productPromotionAmount);
                    preparedStmt.executeUpdate();
                }
                query = "SELECT product_promotion_id "
                        + "FROM product_promotion "
                        + "WHERE product_promotion_name='" + productPromotionName + "' "
                        + "AND product_promotion_type='" + productPromotionType + "' "
                        + "AND product_promotion_amount='" + productPromotionAmount + "'";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    query = "UPDATE product SET product_promotion_id=? WHERE product_id=?";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1, rs.getInt("product_promotion_id"));
                    preparedStmt.setInt(2, productID);
                    preparedStmt.executeUpdate();
                    product.setProductPromotionID(rs.getInt("product_promotion_id"));
                } else {
                    return "error_968";
                }
            }
            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String addProductComment(Product product, ProductComment productComment) {
        try {
            if (product.getProductID() <= 0) {
                return "product_not_set";
            } else if (productComment.getMemberID() <= 0) {
                return "member_not_set";
            }

            String query = "SELECT product_id FROM product_comment WHERE product_id='" + product.getProductID() + "' AND member_id='" + productComment.getMemberID() + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return "member_has_commented";
            } else {
                query = "INSERT INTO product_comment(product_id, member_id, comment_details, product_rating, status, added_on, last_modified) "
                        + "VALUES (?,?,?,?,?,?,?)";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1, product.getProductID());
                preparedStmt.setInt(2, productComment.getMemberID());
                preparedStmt.setString(3, productComment.getCommentDetails());
                preparedStmt.setInt(4, productComment.getProductRating());
                preparedStmt.setInt(5, productComment.getStatus());
                preparedStmt.setTimestamp(6, productComment.getAddedOn());
                preparedStmt.setTimestamp(7, productComment.getLastModified());
                preparedStmt.executeUpdate();

                ProductComment[] productComments = product.getProductComments();
                ProductComment[] copyProductComments = new ProductComment[productComments.length + 1];
                for (int i = 0; i < productComments.length; i++) {
                    copyProductComments[i] = productComments[i];
                }
                copyProductComments[productComments.length] = productComment;
                product.setProductComments(copyProductComments);
            }
            return "success";
        } catch (SQLException E) {
            return "error_507" + E.toString();
        }
    }

    public String deleteProductComment(Product product, ProductComment productComment) {
        try {
            if (product.getProductID() <= 0) {
                return "product_not_set";
            } else if (productComment.getMemberID() <= 0) {
                return "member_not_set";
            }

            String query = "SELECT product_id FROM product_comment WHERE product_id='" + product.getProductID() + "' member_id='" + productComment.getMemberID() + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                query = "DELETE FROM product_comment WHERE product_id=? AND member_id=?";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1, product.getProductID());
                preparedStmt.setInt(2, productComment.getMemberID());
                preparedStmt.executeUpdate();

                int index = 0;
                ProductComment[] productComments = product.getProductComments();
                ProductComment[] copyProductComments = new ProductComment[productComments.length - 1];
                for (int i = 0; i < productComments.length; i++) {
                    if (productComments[i].getMemberID() != productComment.getMemberID()) {
                        copyProductComments[index++] = productComments[i];
                    }
                }
                product.setProductComments(copyProductComments);
            } else {
                return "member_has_not_commented";
            }
            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }
}
