/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
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
public class ShopServlet extends HttpServlet {

    private static final String sessionMemberKey = "sessMember";
    private static final String sessionAdminKey = "sessAdmin";
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
        } else if ("create_shop".equals(action)) {
            if (session.getAttribute(sessionMemberKey) == null) {
                json.put("result", "no_login");
            } else {
                ShopDAO shopDAO = new ShopDAO();
                MemberDAO memberDAO = new MemberDAO();
                ProductDAO productDAO = new ProductDAO();
                Member member = (Member) session.getAttribute(sessionMemberKey);
                if (memberDAO.has_shop(member)) {
                    json.put("result", "already_has_shop");
                } else {
                    String shopName = request.getParameter("shop_name");
                    String shopIntroduction = request.getParameter("shop_introduction");
                    String shopDetail = request.getParameter("shop_detail");
                    int shopCategoryID = Integer.parseInt((request.getParameter("shop_category_id") == null || request.getParameter("shop_category_id").equals("")) ? "0" : request.getParameter("shop_category_id"));
                    int contactNumber = Integer.parseInt((request.getParameter("contact_number") == null || request.getParameter("contact_number").equals("")) ? "0" : request.getParameter("contact_number"));
                    String contactEmail = request.getParameter("contact_email");
                    String website = request.getParameter("website");
                    String strShopTabNames = request.getParameter("shop_tab_names");
                    Timestamp timestamp = new Timestamp(new java.util.Date().getTime());

                    if (shopName != null && shopIntroduction != null && shopDetail != null
                            && !"".equals(shopName) && !"".equals(shopIntroduction) && !"".equals(shopDetail)
                            && shopCategoryID > 0 && contactNumber > 0) {
                        Shop shop = new Shop();
                        ShopCategory shopCategory = new ShopCategory();
                        shopCategory.setShopCategoryID(shopCategoryID);
                        shop.setShopName(shopName);
                        shop.setShopIntroduction(shopIntroduction);
                        shop.setShopDetail(shopDetail);
                        shop.setShopCategory(shopCategory);
                        shop.setMember(member);
                        shop.setContractNumber(contactNumber);
                        shop.setContractEmail(contactEmail);
                        shop.setWebsite(website);
                        shop.setCreatedOn(timestamp);
                        shop.setLastModified(timestamp);

                        String[] arrShopTabNames = strShopTabNames.split(",");
                        shop.setShopTabNames(arrShopTabNames);

                        String result = shopDAO.createShop(shop);
                        json.put("result", result);
                        json.put("shop_id", shop.getShopID());
                    } else {
                        json.put("result", "input_data_not_match");
                    }
                }
            }
        } else if ("delete_shop".equals(action)) {
            Member member = new Member();
            Shop shop = new Shop();
            ShopDAO shopDAO = new ShopDAO();
            if (session.getAttribute(sessionAdminKey) != null) {
                member.setMemberID(Integer.parseInt((request.getParameter("member_id") == null || request.getParameter("member_id").equals("")) ? "0" : request.getParameter("member_id")));
                shop.setShopID(Integer.parseInt((request.getParameter("shop_id") == null || request.getParameter("shop_id").equals("")) ? "0" : request.getParameter("shop_id")));
            } else if (session.getAttribute(sessionMemberKey) != null) {
                member = (Member) session.getAttribute(sessionMemberKey);
                if (member.getHasShop()) {
                    shop = shopDAO.getShopByMember(member);
                } else {
                    json.put("result", "member_no_shop");
                }
            } else {
                json.put("result", "no_login");
            }

            if (session.getAttribute(sessionAdminKey) != null || session.getAttribute(sessionMemberKey) != null) {
                if (shop.getShopID() > 0) {
                    String result = shopDAO.deleteShop(shop);
                    json.put("result", result);
                } else {
                    json.put("result", "shop_not_found");
                }
            }
        } else if ("get_shop_by_id".equals(action)) {
            int shopID = Integer.parseInt((request.getParameter("shop_id") == null || request.getParameter("shop_id").equals("")) ? "0" : request.getParameter("shop_id"));

            if (shopID > 0) {
                Shop shop = new Shop();
                shop.setShopID(shopID);
                String result = new ShopDAO().getShop(shop);
                json.put("result", result);

                if ("success".equals(result)) {
                    putShopToJsonObject(shop, json);
                }
            } else {
                json.put("result", "shop_not_found");
            }
        } else if ("get_shop".equals(action)) {
            Member member = new Member();
            Shop shop = new Shop();
            ShopDAO shopDAO = new ShopDAO();
            if (session.getAttribute(sessionMemberKey) != null) {
                member = (Member) session.getAttribute(sessionMemberKey);
                if (new MemberDAO().has_shop(member)) {
                    shop = shopDAO.getShopByMember(member);
                    putShopToJsonObject(shop, json);
                    json.put("result", "success");
                } else {
                    json.put("result", "member_no_shop");
                }
            } else {
                json.put("result", "no_login");
            }
        } else if ("get_shop_by_category".equals(action)) {
            int shopCategoryID = Integer.parseInt((request.getParameter("shop_category_id") == null || request.getParameter("shop_category_id").equals("")) ? "0" : request.getParameter("shop_category_id"));

            if (shopCategoryID > 0) {
                ShopCategory shopCategory = new ShopCategory();
                shopCategory.setShopCategoryID(shopCategoryID);
                Shop[] shops = new ShopDAO().getShopsByCategory(shopCategory);
                JSONArray jsonArray = new JSONArray();
                for (Shop shop : shops) {
                    JSONObject jsonObj = new JSONObject();
                    putShopToJsonObject(shop, jsonObj);
                    jsonArray.put(jsonObj);
                }
                json.put("shops", jsonArray);
                json.put("result", "success");
            } else {
                json.put("result", "no_shop_found");
            }
        } else if ("get_all_shop_categories".equals(action)) {
            ShopCategory[] shopCategories = new ShopDAO().getShopCategories();
            JSONArray jsonArray = new JSONArray();
            for (ShopCategory shopCategory : shopCategories) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("shop_category_id", shopCategory.getShopCategoryID());
                jsonObj.put("shop_category_name", shopCategory.getShopCategoryName());
                jsonArray.put(jsonObj);
            }
            json.put("shop_categories", jsonArray);
            json.put("result", "success");
        } else if ("get_shop_buyer_orders".equals(action)) {
            Member member = new Member();
            Shop shop = new Shop();
            ShopDAO shopDAO = new ShopDAO();
            if (session.getAttribute(sessionMemberKey) != null) {
                member = (Member) session.getAttribute(sessionMemberKey);
                if (member.getHasShop()) {
                    shop = shopDAO.getShopByMember(member);
                    BuyerOrder[] buyerOrders = shopDAO.getShopBuyerOrders(shop);

                    JSONArray jsonBuyerOrders = new JSONArray();
                    for (BuyerOrder buyerOrder : buyerOrders) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("buyer_order_id", buyerOrder.getBuyerOrderID());
                        jsonObject.put("member_id", buyerOrder.getMemberID());
                        jsonObject.put("shop_id", buyerOrder.getShopID());
                        jsonObject.put("buyer_order_promotion_id", buyerOrder.getBuyerOrderPromotionID());
                        jsonObject.put("buyer_order_promotion_name", buyerOrder.getBuyerOrderPromotionName());
                        jsonObject.put("buyer_order_promotion_type", buyerOrder.getBuyerOrderPromotionType());
                        jsonObject.put("buyer_order_promotion_amount", buyerOrder.getBuyerOrderPromotionAmount());
                        jsonObject.put("total_price", buyerOrder.getTotalPrice());
                        jsonObject.put("shipping_name", buyerOrder.getShippingName());
                        jsonObject.put("shipping_contact_number", buyerOrder.getShippingContactNumber());
                        jsonObject.put("shipping_address", buyerOrder.getShippingAddress());
                        jsonObject.put("datetime", buyerOrder.getDatetime().toString());

                        BuyerOrderProduct[] buyerOrderProducts = buyerOrder.getBuyerOrderProducts();
                        JSONArray jsonArray = new JSONArray();
                        for (BuyerOrderProduct buyerOrderProduct : buyerOrderProducts) {
                            JSONObject jsonObj = new JSONObject();
                            jsonObj.put("product_id", buyerOrderProduct.getProductID());
                            jsonObj.put("product_name", buyerOrderProduct.getProduct().getProductName());
                            jsonObj.put("product_photo_path", buyerOrderProduct.getProduct().getProductPhotos()[0].getProductPhotoPath());
                            jsonObj.put("order_product_price", buyerOrderProduct.getProductPrice());
                            jsonObj.put("order_product_type_id", buyerOrderProduct.getProductTypeID());
                            jsonObj.put("order_product_type_name", buyerOrderProduct.getProductTypeName());
                            jsonObj.put("quantity", buyerOrderProduct.getQuantity());
                            jsonArray.put(jsonObj);
                        }
                        jsonObject.put("order_products", jsonArray);
                        jsonBuyerOrders.put(jsonObject);
                    }
                    json.put("orders", jsonBuyerOrders);
                    json.put("result", "success");
                } else {
                    json.put("result", "member_no_shop");
                }
            } else {
                json.put("result", "no_login");
            }

            ShopCategory[] shopCategories = new ShopDAO().getShopCategories();
            JSONArray jsonArray = new JSONArray();
            for (ShopCategory shopCategory : shopCategories) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("shop_category_id", shopCategory.getShopCategoryID());
                jsonObj.put("shop_category_name", shopCategory.getShopCategoryName());
                jsonArray.put(jsonObj);
            }
            json.put("shop_categories", jsonArray);
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

    private void putShopToJsonObject(Shop shop, JSONObject jsonObject) {
        jsonObject.put("shop_id", shop.getShopID());
        jsonObject.put("shop_name", shop.getShopName());
        jsonObject.put("shop_introduction", shop.getShopIntroduction());
        jsonObject.put("shop_detail", shop.getShopDetail());
        jsonObject.put("shop_category_id", shop.getShopCategory().getShopCategoryID());
        jsonObject.put("shop_category_name", shop.getShopCategory().getShopCategoryName());
        jsonObject.put("shop_level", shop.getShopLevel());
        jsonObject.put("member_id", shop.getMember().getMemberID());
        jsonObject.put("member_nickname", shop.getMember().getNickname());
        jsonObject.put("contact_number", shop.getContractNumber());
        jsonObject.put("contact_email", shop.getContactEmail());
        jsonObject.put("website", shop.getWebsite());
        jsonObject.put("amount_sold", shop.getAmountSold());
        jsonObject.put("shop_tab_ids", shop.getShopTabIDs());
        jsonObject.put("shop_tab_names", shop.getShopTabNames());
    }
}
