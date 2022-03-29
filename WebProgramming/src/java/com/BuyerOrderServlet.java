/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.IOException;
import java.io.PrintWriter;
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
public class BuyerOrderServlet extends HttpServlet {

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
        } else if ("delete_order".equals(action)) {
            if (session.getAttribute(sessionMemberKey) != null) {
                int buyerOrderID = Integer.parseInt((request.getParameter("order_id") == null || request.getParameter("order_id").equals("")) ? "0" : request.getParameter("order_id"));
                if (buyerOrderID > 0) {
                    BuyerOrder buyerOrder = new BuyerOrder();
                    buyerOrder.setBuyerOrderID(buyerOrderID);
                    String result = new BuyerOrderDAO().deleteBuyerOrder(buyerOrder);
                    json.put("result", result);
                } else {
                    json.put("result", "buyer_order_not_found");
                }
            } else {
                json.put("result", "permission_denied");
            }
        } else if ("get_buyer_order".equals(action)) {
            int buyerOrderID = Integer.parseInt((request.getParameter("order_id") == null || request.getParameter("order_id").equals("")) ? "0" : request.getParameter("order_id"));
            if (buyerOrderID > 0) {
                BuyerOrder buyerOrder = new BuyerOrder();
                buyerOrder.setBuyerOrderID(buyerOrderID);
                String result = new BuyerOrderDAO().getBuyerOrder(buyerOrder);
                json.put("result", result);

                if ("success".equals(result)) {
                    json.put("buyer_order_id", buyerOrder.getBuyerOrderID());
                    json.put("member_id", buyerOrder.getMemberID());
                    json.put("shop_id", buyerOrder.getShopID());
                    json.put("buyer_order_promotion_id", buyerOrder.getBuyerOrderPromotionID());
                    json.put("buyer_order_promotion_name", buyerOrder.getBuyerOrderPromotionName());
                    json.put("buyer_order_promotion_type", buyerOrder.getBuyerOrderPromotionType());
                    json.put("buyer_order_promotion_amount", buyerOrder.getBuyerOrderPromotionAmount());
                    json.put("total_price", buyerOrder.getTotalPrice());
                    json.put("shipping_name", buyerOrder.getShippingName());
                    json.put("shipping_contact_number", buyerOrder.getShippingContactNumber());
                    json.put("shipping_address", buyerOrder.getShippingAddress());
                    json.put("datetime", buyerOrder.getDatetime().toString());

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
                    json.put("order_products", jsonArray);
                }
            } else {
                json.put("result", "buyer_order_not_found");
            }
        } else if ("get_orders_by_member_id".equals(action)) {
            int memerID = Integer.parseInt((request.getParameter("member_id") == null || request.getParameter("member_id").equals("")) ? "0" : request.getParameter("member_id"));
            if (memerID > 0) {
                Member member = new Member();
                member.setMemberID(memerID);
                BuyerOrder[] buyerOrders = new BuyerOrderDAO().getBuyerOrdersByMemberID(member);

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
            } else {
                json.put("result", "buyer_order_not_found");
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

}
