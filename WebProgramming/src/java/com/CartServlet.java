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
public class CartServlet extends HttpServlet {

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
        } else if ("checkout".equals(action)) {
            Member member = new Member();
            if (session.getAttribute(sessionMemberKey) != null) {
                member = (Member) session.getAttribute(sessionMemberKey);

                Cart cart = new Cart();
                cart.setMember(member);
                String result = new CartDAO().getCart(cart);
                json.put("result", result);

                String shippingName = request.getParameter("shipping_name");
                int shippingContactNumber = Integer.parseInt((request.getParameter("shipping_contact_number") == null || request.getParameter("shipping_contact_number").equals("")) ? "0" : request.getParameter("shipping_contact_number"));
                String shippingAddress = request.getParameter("shipping_address");

                if (shippingContactNumber > 0 && shippingName != null && shippingAddress != null && !"".equals(shippingName) && !"".equals(shippingAddress)) {
                    result = new CartDAO().checkOut(cart, cart.getCartProducts(), shippingName, shippingContactNumber, shippingAddress);
                    json.put("result", result);
                }
            } else {
                json.put("result", "no_login");
            }
        } else if ("get_cart".equals(action)) {
            Member member = new Member();
            if (session.getAttribute(sessionAdminKey) != null) {
                member.setMemberID(Integer.parseInt((request.getParameter("member_id") == null || request.getParameter("member_id").equals("")) ? "0" : request.getParameter("member_id")));
            } else if (session.getAttribute(sessionMemberKey) != null) {
                member = (Member) session.getAttribute(sessionMemberKey);
            } else {
                json.put("result", "no_login");
            }

            if (session.getAttribute(sessionAdminKey) != null || session.getAttribute(sessionMemberKey) != null) {
                Cart cart = new Cart();
                cart.setMember(member);
                String result = new CartDAO().getCart(cart);
                json.put("result", result);

                if ("success".equals(result)) {
                    JSONArray jsonArray = new JSONArray();
                    CartProduct[] cartProducts = cart.getCartProducts();
                    for (CartProduct cartProduct : cartProducts) {
                        JSONObject jsonObj = new JSONObject();
                        jsonObj.put("product_id", cartProduct.getProduct().getProductID());
                        jsonObj.put("product_name", cartProduct.getProduct().getProductName());
                        jsonObj.put("product_type_id", cartProduct.getProductTypeID());
                        jsonObj.put("product_type_name", cartProduct.getProductTypeName());
                        jsonObj.put("quantity", cartProduct.getQuantity());
                        jsonArray.put(jsonObj);
                    }
                    json.put("cart_products", jsonArray);
                }
            }
        } else if ("update_cart".equals(action)) {
            int productID = Integer.parseInt((request.getParameter("product_id") == null || request.getParameter("product_id").equals("")) ? "0" : request.getParameter("product_id"));
            int productTypeID = Integer.parseInt((request.getParameter("product_type_id") == null || request.getParameter("product_type_id").equals("")) ? "0" : request.getParameter("product_type_id"));
            int quantity = Integer.parseInt((request.getParameter("quantity") == null || request.getParameter("quantity").equals("")) ? "0" : request.getParameter("quantity"));
            Member member = new Member();
            if (session.getAttribute(sessionAdminKey) != null) {
                member.setMemberID(Integer.parseInt((request.getParameter("member_id") == null || request.getParameter("member_id").equals("")) ? "0" : request.getParameter("member_id")));
            } else if (session.getAttribute(sessionMemberKey) != null) {
                member = (Member) session.getAttribute(sessionMemberKey);
            } else {
                json.put("result", "no_login");
            }

            if (session.getAttribute(sessionAdminKey) != null || session.getAttribute(sessionMemberKey) != null) {
                Cart cart = new Cart();
                cart.setMember(member);
                String result = new CartDAO().getCart(cart);
                json.put("result", result);

                if ("success".equals(result)) {
                    CartProduct[] cartProducts = cart.getCartProducts();
                    for (int i = 0; i < cartProducts.length; i++) {
                        if (cartProducts[i].getProductID() == productID) {
                            JSONObject jsonObj = new JSONObject();
                            cartProducts[i].setProductTypeID(productTypeID);
                            cartProducts[i].setQuantity(quantity);
                            break;
                        }
                    }
                    cart.setCartProducts(cartProducts);
                    result = new CartDAO().updateCart(cart);
                    json.put("result", result);
                }
            }
        } else if ("add_cart_product".equals(action)) {
            int productID = Integer.parseInt((request.getParameter("product_id") == null || request.getParameter("product_id").equals("")) ? "0" : request.getParameter("product_id"));
            int productTypeID = Integer.parseInt((request.getParameter("product_type_id") == null || request.getParameter("product_type_id").equals("")) ? "0" : request.getParameter("product_type_id"));
            int quantity = Integer.parseInt((request.getParameter("quantity") == null || request.getParameter("quantity").equals("")) ? "0" : request.getParameter("quantity"));
            Member member = new Member();
            if (session.getAttribute(sessionAdminKey) != null) {
                member.setMemberID(Integer.parseInt((request.getParameter("member_id") == null || request.getParameter("member_id").equals("")) ? "0" : request.getParameter("member_id")));
            } else if (session.getAttribute(sessionMemberKey) != null) {
                member = (Member) session.getAttribute(sessionMemberKey);
            } else {
                json.put("result", "no_login");
            }

            if (session.getAttribute(sessionAdminKey) != null || session.getAttribute(sessionMemberKey) != null) {
                Cart cart = new Cart();
                cart.setMember(member);
                String result = new CartDAO().getCart(cart);
                json.put("result", result);

                if ("success".equals(result)) {
                    CartProduct[] cartProducts = cart.getCartProducts();
                    CartProduct[] newCartProducts = new CartProduct[(cartProducts == null)? 1:(cartProducts.length + 1)];
                    if (cartProducts != null) {
                        for (int i = 0; i < cartProducts.length; i++) {
                            newCartProducts[i] = new CartProduct();
                            newCartProducts[i].setProductID(cartProducts[i].getProductID());
                            newCartProducts[i].setProductTypeID(cartProducts[i].getProductTypeID());
                            newCartProducts[i].setQuantity(cartProducts[i].getQuantity());
                        }
                    }
                    newCartProducts[newCartProducts.length - 1] = new CartProduct();
                    newCartProducts[newCartProducts.length - 1].setProductID(productID);
                    newCartProducts[newCartProducts.length - 1].setProductTypeID(productTypeID);
                    newCartProducts[newCartProducts.length - 1].setQuantity(quantity);
                    cart.setCartProducts(newCartProducts);
                    result = new CartDAO().updateCart(cart);
                    json.put("result", result);
                }
            }
        } else if ("remove_cart_product".equals(action)) {
            int productID = Integer.parseInt((request.getParameter("product_id") == null || request.getParameter("product_id").equals("")) ? "0" : request.getParameter("product_id"));
            Member member = new Member();
            if (session.getAttribute(sessionAdminKey) != null) {
                member.setMemberID(Integer.parseInt((request.getParameter("member_id") == null || request.getParameter("member_id").equals("")) ? "0" : request.getParameter("member_id")));
            } else if (session.getAttribute(sessionMemberKey) != null) {
                member = (Member) session.getAttribute(sessionMemberKey);
            } else {
                json.put("result", "no_login");
            }

            if (session.getAttribute(sessionAdminKey) != null || session.getAttribute(sessionMemberKey) != null) {
                Cart cart = new Cart();
                cart.setMember(member);
                String result = new CartDAO().getCart(cart);
                json.put("result", result);

                if ("success".equals(result)) {
                    int iNewCartProducts = 0;
                    CartProduct[] cartProducts = cart.getCartProducts();
                    CartProduct[] newCartProducts = new CartProduct[cartProducts.length - 1];
                    for (int i = 0; i < cartProducts.length; i++) {
                        if (iNewCartProducts >= cartProducts.length) {
                            json.put("result", "product_not_found_in_cart");
                        } else if (cartProducts[i].getProductID() != productID) {
                            newCartProducts[iNewCartProducts++] = cartProducts[i];
                        }
                    }
                    cart.setCartProducts(newCartProducts);
                    result = new CartDAO().updateCart(cart);
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

}
