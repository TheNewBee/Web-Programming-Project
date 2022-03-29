/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

/**
 *
 * @author Raymond Chan
 */
public class MemberServlet extends HttpServlet {

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
        } else if ("register".equals(action)) {
            if (session.getAttribute(sessionMemberKey) == null) {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String verifyPassword = request.getParameter("verify_password");
                String nickname = request.getParameter("nickname");
                Timestamp timestamp = new Timestamp(new java.util.Date().getTime());

                if (email != null && password != null && nickname != null
                        && !"".equals(email) && !"".equals(password) && !"".equals(nickname)
                        && verifyPassword.equals(password) && checkEmailFormat(email)) {
                    Member member = new Member();
                    member.setEmail(email);
                    member.setNickname(nickname);
                    member.setMemberLevel(1);
                    member.setAmountBought(0);
                    member.setHasShop(false);
                    member.setDisabled(false);
                    member.setRegDate(timestamp);
                    member.setLastLogin(timestamp);
                    String result = new MemberDAO().registerMember(member, password);
                    json.put("result", result);
                    if ("success".equals(result)) {
                        session.setAttribute(sessionMemberKey, member);
                    }
                } else {
                    json.put("result", "input_data_not_match");
                }
            } else {
                json.put("result", "has_been_login");
            }
        } else if ("login".equals(action)) {
            if (session.getAttribute(sessionMemberKey) == null) {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                Timestamp timestamp = new Timestamp(new java.util.Date().getTime());

                if (email != null && password != null
                        && !"".equals(email) && !"".equals(password)
                        && checkEmailFormat(email)) {
                    Member member = new Member();
                    member.setEmail(email);
                    member.setLastLogin(timestamp);
                    String result = new MemberDAO().loginMember(member, password);
                    json.put("result", result);
                    if ("success".equals(result)) {
                        session.setAttribute(sessionMemberKey, member);
                    }
                } else {
                    json.put("result", "input_data_not_match");
                }
            } else {
                json.put("result", "has_been_login");
            }
        } else if ("logout".equals(action)) {
            if (session.getAttribute(sessionMemberKey) != null) {
                session.removeAttribute(sessionMemberKey);
                json.put("result", "success");
            } else {
                json.put("result", "no_login");
            }
        } else if ("change_password".equals(action)) {
            if (session.getAttribute(sessionMemberKey) != null) {
                String currentPassword = request.getParameter("current_password");
                String newPassword = request.getParameter("new_password");
                String verifyPassword = request.getParameter("verify_password");

                if (newPassword != null && currentPassword != null && verifyPassword != null
                        && !"".equals(newPassword) && !"".equals(currentPassword) && !"".equals(verifyPassword)
                        && verifyPassword.equals(newPassword)) {
                    Member member = (Member) session.getAttribute(sessionMemberKey);
                    String result = new MemberDAO().changePassword(member, newPassword, currentPassword);
                    json.put("result", result);
                } else {
                    json.put("result", "input_data_not_match");
                }
            } else {
                json.put("result", "no_login");
            }
        } else if ("change_nickname".equals(action)) {
            if (session.getAttribute(sessionMemberKey) != null) {
                String nickname = request.getParameter("nickname");
                String currentPassword = request.getParameter("current_password");

                if (nickname != null && currentPassword != null
                        && !"".equals(nickname) && !"".equals(currentPassword)) {
                    Member member = (Member) session.getAttribute(sessionMemberKey);
                    member.setNickname(nickname);
                    String result = new MemberDAO().updateMember(member, currentPassword);
                    json.put("result", result);
                } else {
                    json.put("result", "input_data_not_match");
                }
            } else {
                json.put("result", "no_login");
            }
        } else if ("check_login_status".equals(action)) {
            if (session.getAttribute(sessionMemberKey) != null) {
                Member member = (Member) session.getAttribute(sessionMemberKey);
                json.put("member_id", member.getMemberID());
                json.put("nickname", member.getNickname());
                json.put("member_level", member.getMemberLevel());
                json.put("has_shop", member.getHasShop()? 1:0);
                json.put("login", "1");
            } else {
                json.put("login", "0");
            }
            json.put("result", "success");
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

    private boolean checkEmailFormat(String email) {
        String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"; // based on RFC 5322
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
