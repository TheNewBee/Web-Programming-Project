/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Raymond Chan
 */
public class ProductServlet extends HttpServlet {

    private static final String sessionMemberKey = "sessMember";
    private boolean isPost = false;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession(true);
        String action = request.getParameter("action");
        JSONObject json = new JSONObject();
        json.put("result", "no_action");
        if (action == null) {
        } else if ("create_product".equals(action)) {
            if (session.getAttribute(sessionMemberKey) == null) {
                json.put("result", "no_login");
            } else {
                ShopDAO shopDAO = new ShopDAO();
                MemberDAO memberDAO = new MemberDAO();
                ProductDAO productDAO = new ProductDAO();
                Member member = (Member) session.getAttribute(sessionMemberKey);
                if (memberDAO.has_shop(member)) {
                    String productName = request.getParameter("product_name");
                    String productIntroduction = request.getParameter("product_introduction");
                    String productDetail = request.getParameter("product_detail");
                    int productCategoryID = Integer.parseInt((request.getParameter("product_category_id") == null || request.getParameter("product_category_id").equals("")) ? "0" : request.getParameter("product_category_id"));
                    double productPrice = Double.parseDouble(request.getParameter("product_price") == null ? "0.0" : request.getParameter("product_price"));
                    int productPromotionID = Integer.parseInt((request.getParameter("product_promotion_id") == null || request.getParameter("product_promotion_id").equals("")) ? "0" : request.getParameter("product_promotion_id"));
                    int amountStock = Integer.parseInt((request.getParameter("amount_stock") == null || request.getParameter("amount_stock").equals("")) ? "0" : request.getParameter("amount_stock"));
                    String strProductTabNames = request.getParameter("product_tab_names");
                    String strProductTypeNames = request.getParameter("product_type_names");
                    String strProductPhotoPaths = request.getParameter("product_photo_paths");
                    Timestamp timestamp = new Timestamp(new java.util.Date().getTime());

                    if (productName != null && productIntroduction != null && productDetail != null
                            && !"".equals(productName) && !"".equals(productIntroduction) && !"".equals(productDetail)
                            && productCategoryID > 0 && productPrice > 0.0 && amountStock > 0) {
                        Shop shop = shopDAO.getShopByMember(member);
                        Product product = new Product();
                        ProductCategory productCategory = new ProductCategory();
                        product.setShopID(shop.getShopID());
                        product.setProductName(productName);
                        product.setProductIntroduction(productIntroduction);
                        product.setProductDetail(productDetail);
                        product.setProductPrice(productPrice);
                        product.setProductPromotionID(productPromotionID);
                        product.setAmountStock(amountStock);
                        product.setAmountSold(0);
                        product.setStatus(1);
                        product.setAddedOn(timestamp);
                        product.setLastModified(timestamp);

                        productCategory.setProductCategoryID(productCategoryID);
                        product.setProductCategory(productCategory);

                        String[] arrProductTabNames = strProductTabNames.split(",");
                        product.setProductTabNames(arrProductTabNames);

                        String[] arrProductTypeNames = strProductTypeNames.split(",");
                        product.setProductTypeNames(arrProductTypeNames);

                        String[] arrProductPhotoPaths = strProductPhotoPaths.split(",");
                        if (arrProductPhotoPaths.length > 0) {
                            ProductPhoto[] productPhotos = new ProductPhoto[arrProductPhotoPaths.length];
                            for (int i = 0; i < productPhotos.length; i++) {
                                productPhotos[i] = new ProductPhoto();
                                productPhotos[i].setProductPhotoPath(arrProductPhotoPaths[i]);
                            }
                            product.setProductPhotos(productPhotos);
                        }
                        String result = productDAO.createProduct(product);
                        json.put("result", result);
                        json.put("product_id", product.getProductID());
                    } else {
                        json.put("result", "input_data_not_match");
                    }
                } else {
                    json.put("result", "not_create_shop");
                }
            }
        } else if ("delete_product".equals(action)) {
            if (session.getAttribute(sessionMemberKey) == null) {
                json.put("result", "no_login");
            } else {
                ShopDAO shopDAO = new ShopDAO();
                MemberDAO memberDAO = new MemberDAO();
                ProductDAO productDAO = new ProductDAO();
                Member member = (Member) session.getAttribute(sessionMemberKey);
                if (memberDAO.has_shop(member)) {
                    int productID = Integer.parseInt((request.getParameter("product_id") == null || request.getParameter("product_id").equals("")) ? "0" : request.getParameter("product_id"));
                    if (productID > 0) {
                        Shop shop = shopDAO.getShopByMember(member);
                        Product product = new Product();
                        product.setProductID(productID);
                        productDAO.getProduct(product);
                        if (product.getShopID() == shop.getShopID()) {
                            productDAO.deleteProduct(product);
                        } else {
                            json.put("result", "not_product_owner");
                        }
                    }
                } else {
                    json.put("result", "not_create_shop");
                }
            }
        } else if ("get_product".equals(action)) {
            int productID = Integer.parseInt((request.getParameter("product_id") == null || request.getParameter("product_id").equals("")) ? "0" : request.getParameter("product_id"));
            if (productID > 0) {
                Product product = new Product();
                product.setProductID(productID);
                String result = new ProductDAO().getProduct(product);
                setProductToJson(product, json);
                json.put("result", result);
            } else {
                json.put("result", "product_id_not_found");
            }
        } else if ("get_products".equals(action)) {
            String productIDs = request.getParameter("product_id");
            if ("".equals(productIDs)) {
                json.put("result", "product_id_not_found");
            } else {
                int[] arrProductIDs = Arrays.asList(productIDs.split(",")).stream().mapToInt(Integer::parseInt).toArray();
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < arrProductIDs.length; i++) {
                    JSONObject jsonObj = new JSONObject();
                    Product product = new Product();
                    product.setProductID(arrProductIDs[i]);
                    String result = new ProductDAO().getProduct(product);
                    json.put("result", result);
                    setProductToJson(product, jsonObj);
                    jsonArray.put(jsonObj);
                }
                json.put("products", jsonArray);
            }
        } else if ("get_all_products".equals(action)) {
            Product[] products = new ProductDAO().getProducts();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < products.length; i++) {
                JSONObject jsonObj = new JSONObject();
                setProductToJson(products[i], jsonObj);
                jsonArray.put(jsonObj);
            }
            json.put("products", jsonArray);
            json.put("result", "success");
        } else if ("get_all_products_by_shop_id".equals(action)) {
            int shopID = Integer.parseInt((request.getParameter("shop_id") == null || request.getParameter("shop_id").equals("")) ? "0" : request.getParameter("shop_id"));
            if (shopID > 0) {
                json.put("result", "shop_id_not_found");
            } else {
                Product[] products = new ProductDAO().getProductsByShopID(shopID);
                JSONArray jsonArray = new JSONArray();
                for (Product product : products) {
                    JSONObject jsonObj = new JSONObject();
                    setProductToJson(product, jsonObj);
                    jsonArray.put(jsonObj);
                }
                json.put("products", jsonArray);
            }
        } else if ("get_all_products_by_product_category_id".equals(action)) {
            int productCategoryID = Integer.parseInt((request.getParameter("product_category_id") == null || request.getParameter("product_category_id").equals("")) ? "0" : request.getParameter("product_category_id"));
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryID(productCategoryID);
            if (productCategoryID > 0) {
                json.put("result", "product_category_id_not_found");
            } else {
                Product[] products = new ProductDAO().getProductsByProductCategory(productCategory);
                JSONArray jsonArray = new JSONArray();
                for (Product product : products) {
                    JSONObject jsonObj = new JSONObject();
                    setProductToJson(product, jsonObj);
                    jsonArray.put(jsonObj);
                }
                json.put("products", jsonArray);
            }
        } else if ("get_all_products_by_keyword".equals(action)) {
            String keyword = request.getParameter("keyword");
            if ("".equals(keyword)) {
                json.put("result", "keyword_not_found");
            } else {
                Product[] products = new ProductDAO().getProductsByProductNameKey(keyword);
                JSONArray jsonArray = new JSONArray();
                for (Product product : products) {
                    JSONObject jsonObj = new JSONObject();
                    setProductToJson(product, jsonObj);
                    jsonArray.put(jsonObj);
                }
                json.put("products", jsonArray);
            }
        } else if ("get_all_products_by_tab".equals(action)) {
            int tabID = Integer.parseInt((request.getParameter("tab_id") == null || request.getParameter("tab_id").equals("")) ? "0" : request.getParameter("tab_id"));
            String tabName = request.getParameter("tab_name");
            if (tabID == 0 && tabName == null && "".equals(tabName)) {
                json.put("result", "keyword_not_found");
            } else {
                Product[] products = new ProductDAO().getProductsByProductTab(tabID, tabName);
                JSONArray jsonArray = new JSONArray();
                for (Product product : products) {
                    JSONObject jsonObj = new JSONObject();
                    setProductToJson(product, jsonObj);
                    jsonArray.put(jsonObj);
                }
                json.put("products", jsonArray);
            }
        } else if ("get_all_product_categories".equals(action)) {
            ProductCategory[] productCategories = new ProductDAO().getProductCategories();
            JSONArray jsonArray = new JSONArray();
            for (ProductCategory productCategory : productCategories) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("product_category_id", productCategory.getProductCategoryID());
                jsonObj.put("product_category_name", productCategory.getProductCategoryName());
                jsonArray.put(jsonObj);
            }
            json.put("product_categories", jsonArray);
        } else if ("update_product".equals(action)) {
            if (session.getAttribute(sessionMemberKey) == null) {
                json.put("result", "no_login");
            } else {
                int productID = Integer.parseInt((request.getParameter("product_id") == null || request.getParameter("product_id").equals("")) ? "0" : request.getParameter("product_id"));
                if (productID > 0) {
                    Product product = new Product();
                    ProductDAO productDAO = new ProductDAO();
                    product.setProductID(productID);
                    productDAO.getProduct(product);
                    if ("".equals(product.getProductName())) {
                        json.put("result", "product_id_not_found");
                    } else {
                        String productName = request.getParameter("product_name");
                        String productIntroduction = request.getParameter("product_introduction");
                        String productDetail = request.getParameter("product_detail");
                        double productPrice = Double.parseDouble(request.getParameter("product_price") == null ? "0.0" : request.getParameter("product_price"));
                        int amountStock = Integer.parseInt((request.getParameter("amount_stock") == null || request.getParameter("amount_stock").equals("")) ? "0" : request.getParameter("amount_stock"));

                        product.setProductName(productName);
                        product.setProductIntroduction(productIntroduction);
                        product.setProductDetail(productDetail);
                        product.setProductPrice(productPrice);
                        product.setAmountStock(amountStock);
                        String result = productDAO.updateProduct(product);
                        json.put("result", result);
                    }
                } else {
                    json.put("result", "product_id_not_found");
                }
            }
        } else if ("update_product_tab".equals(action)) {
            if (session.getAttribute(sessionMemberKey) == null) {
                json.put("result", "no_login");
            } else {
                int productID = Integer.parseInt((request.getParameter("product_id") == null || request.getParameter("product_id").equals("")) ? "0" : request.getParameter("product_id"));
                String strProductTabNames = request.getParameter("product_tab_names");
                String[] arrProductTabNames = strProductTabNames.split(",");
                if (productID > 0) {
                    Product product = new Product();
                    product.setProductID(productID);
                    product.setProductTabNames(arrProductTabNames);
                    String result = new ProductDAO().updateProductTabs(product);
                    json.put("result", result);
                }
            }
        } else if ("update_product_type".equals(action)) {
            if (session.getAttribute(sessionMemberKey) == null) {
                json.put("result", "no_login");
            } else {
                int productID = Integer.parseInt((request.getParameter("product_id") == null || request.getParameter("product_id").equals("")) ? "0" : request.getParameter("product_id"));
                String strProductTypeNames = request.getParameter("product_type_names");
                String[] arrProductTypeNames = strProductTypeNames.split(",");
                if (productID > 0) {
                    Product product = new Product();
                    product.setProductID(productID);
                    product.setProductTypeNames(arrProductTypeNames);
                    String result = new ProductDAO().updateProductTypes(product);
                    json.put("result", result);
                }
            }
        } else if ("update_product_promotion".equals(action)) {
            if (session.getAttribute(sessionMemberKey) == null) {
                json.put("result", "no_login");
            } else {
                int productID = Integer.parseInt((request.getParameter("product_id") == null || request.getParameter("product_id").equals("")) ? "0" : request.getParameter("product_id"));
                String productPromotionName = request.getParameter("product_promotion_name");
                int productPromotionType = Integer.parseInt((request.getParameter("product_promotion_type") == null || request.getParameter("product_promotion_type").equals("")) ? "0" : request.getParameter("product_promotion_type"));
                double productPromotionAmount = Double.parseDouble(request.getParameter("product_promotion_amount") == null ? "0.0" : request.getParameter("product_promotion_amount"));
                if (productID > 0) {
                    Product product = new Product();
                    product.setProductID(productID);
                    product.setProductPromotionName(productPromotionName);
                    product.setProductPromotionType(productPromotionType);
                    product.setProductPromotionAmount(productPromotionAmount);
                    String result = new ProductDAO().updateProductTypes(product);
                    json.put("result", result);
                }
            }
        } else if ("add_product_comment".equals(action)) {
            if (session.getAttribute(sessionMemberKey) == null) {
                json.put("result", "no_login");
            } else {
                int productID = Integer.parseInt((request.getParameter("product_id") == null || request.getParameter("product_id").equals("")) ? "0" : request.getParameter("product_id"));
                int productRating = Integer.parseInt((request.getParameter("product_comment_rating") == null || request.getParameter("product_comment_rating").equals("")) ? "0" : request.getParameter("product_comment_rating"));
                String commentDetails = request.getParameter("product_comment_detail");
                if (productID > 0) {
                    Product product = new Product();
                    ProductComment productComment = new ProductComment();
                    Member member = (Member) session.getAttribute(sessionMemberKey);

                    product.setProductID(productID);
                    productComment.setMemberID(member.getMemberID());
                    productComment.setCommentDetails(commentDetails);
                    productComment.setProductRating(productRating);
                    String result = new ProductDAO().addProductComment(product, productComment);
                    json.put("result", result);
                }
            }
        } else if ("delete_product_comment".equals(action)) {
            if (session.getAttribute(sessionMemberKey) == null) {
                json.put("result", "no_login");
            } else {
                int memberID = Integer.parseInt((request.getParameter("member_id") == null || request.getParameter("member_id").equals("")) ? "0" : request.getParameter("member_id"));
                int productID = Integer.parseInt((request.getParameter("product_id") == null || request.getParameter("product_id").equals("")) ? "0" : request.getParameter("product_id"));
                if (productID > 0) {
                    Product product = new Product();
                    ProductComment productComment = new ProductComment();

                    product.setProductID(productID);
                    productComment.setMemberID(memberID);
                    String result = new ProductDAO().deleteProductComment(product, productComment);
                    json.put("result", result);
                }
            }
        }

        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        isPost = false;
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        isPost = true;
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void setProductToJson(Product product, JSONObject json) {
        json.put("product_id", product.getProductID());
        json.put("shop_id", product.getShopID());
        json.put("product_name", product.getProductName());
        json.put("product_introduction", product.getProductIntroduction());
        json.put("product_detail", product.getProductDetail());
        json.put("product_price", product.getProductPrice());
        json.put("amount_stock", product.getAmountStock());
        json.put("amount_sold", product.getAmountSold());
        json.put("product_tab_id", product.getProductTabIDs());
        json.put("product_tab_names", product.getProductTabNames());
        json.put("product_type_id", product.getProductTypeIDs());
        json.put("product_type_names", product.getProductTypeNames());
        json.put("product_category_id", product.getProductCategory().getProductCategoryID());
        json.put("product_category_name", product.getProductCategory().getProductCategoryName());
        json.put("product_promotion_id", product.getProductPromotionID());
        json.put("product_promotion_name", product.getProductPromotionName());
        json.put("test", product.getProductTabIDs().length);
        
        switch (product.getProductPromotionType()) {
            case 1:
                json.put("product_promotion_price", product.getProductPrice() * product.getProductPromotionAmount());
                break;
            case 2:
                json.put("product_promotion_price", product.getProductPrice() - product.getProductPromotionAmount());
                break;
        }

        ProductPhoto[] productPhotos = product.getProductPhotos();
        if (productPhotos.length > 0) {
            String[] productPhotoPaths = new String[productPhotos.length];
            for (int i = 0; i < productPhotos.length; i++) {
                if (productPhotos[i] != null) {
                    productPhotoPaths[i] = productPhotos[i].getProductPhotoPath();
                }
            }
            json.put("product_photo_paths", productPhotoPaths);
        }

        ProductComment[] productComments = product.getProductComments();
        if (productComments.length > 0) {
            String[][] arrProductComments = new String[productComments.length][5];
            for (int i = 0; i < productComments.length; i++) {
                if (productComments[i] != null) {
                    int memberID = productComments[i].getMemberID();
                    Member member = new Member();
                    member.setMemberID(memberID);
                    new MemberDAO().getMember(member);
                    arrProductComments[i][0] = Integer.toString(member.getMemberID());
                    arrProductComments[i][1] = member.getNickname();
                    arrProductComments[i][2] = productComments[i].getCommentDetails();
                    arrProductComments[i][3] = Integer.toString(productComments[i].getProductRating());
                    arrProductComments[i][4] = productComments[i].getAddedOn().toString();
                }
            }
            json.put("product_comments", arrProductComments);
        }
    }
}
