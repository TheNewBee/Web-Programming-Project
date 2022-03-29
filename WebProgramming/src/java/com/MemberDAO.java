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

import com.lambdaworks.crypto.SCryptUtil;

/**
 *
 * @author Raymond Chan
 */
public class MemberDAO {

    private Connection conn;
    private Statement stmt;

    public MemberDAO() {
        conn = new DBConnection().getConnection();
        try {
            stmt = conn.createStatement();
        } catch (SQLException E) {
        }
    }

    public String registerMember(Member member, String password) {
        try {
            String email = member.getEmail();
            String query = "SELECT member_id FROM member WHERE email='" + email + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                return "email_has_been_register";
            } else {
                query = "INSERT INTO member(email, password, nickname, member_level, amount_bought, has_shop, disabled, regdate, last_login) "
                        + "VALUES (?,?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setString(1, email);
                preparedStmt.setString(2, encryptPassword(password));
                preparedStmt.setString(3, member.getNickname());
                preparedStmt.setInt(4, member.getMemberLevel());
                preparedStmt.setInt(5, member.getAmountBought());
                preparedStmt.setInt(6, member.getHasShop() ? 1 : 0);
                preparedStmt.setInt(7, member.getDisabled() ? 1 : 0);
                preparedStmt.setTimestamp(8, member.getRegDate());
                preparedStmt.setTimestamp(9, member.getLastLogin());
                preparedStmt.executeUpdate();

                query = "SELECT member_id FROM member WHERE email='" + email + "'";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    member.setMemberID(rs.getInt("member_id"));
                    return "success";
                } else {
                    return "error_304";
                }
            }
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String loginMember(Member member, String password) {
        try {
            String query = "SELECT * FROM member WHERE email='" + member.getEmail() + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (verifyPassword(password, rs.getString("password"))) {
                    int memberID = rs.getInt("member_id");
                    member.setMemberID(memberID);
                    member.setEmail(rs.getString("email"));
                    member.setNickname(rs.getString("nickname"));
                    member.setMemberLevel(rs.getInt("member_level"));
                    member.setAmountBought(rs.getInt("amount_bought"));
                    member.setHasShop((rs.getInt("has_shop") == 1));
                    member.setDisabled((rs.getInt("disabled") == 1));
                    member.setRegDate(rs.getTimestamp("regdate"));

                    query = "UPDATE member SET last_login=? WHERE member_id=?";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setTimestamp(1, member.getLastLogin());
                    preparedStmt.setInt(2, memberID);
                    preparedStmt.executeUpdate();
                    return "success";
                } else {
                    return "wrong_email_or_password";
                }
            } else {
                return "wrong_email_or_password";
            }
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String changePassword(Member member, String password, String currentPassword) {
        try {
            String query = "SELECT * FROM member WHERE email='" + member.getEmail() + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (verifyPassword(currentPassword, rs.getString("password"))) {
                    query = "UPDATE member SET password=? WHERE member_id=?";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setString(1, encryptPassword(password));
                    preparedStmt.setInt(2, member.getMemberID());
                    preparedStmt.executeUpdate();
                    return "success";
                } else {
                    return "wrong_email_or_password";
                }
            } else {
                return "wrong_email_or_password";
            }
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String updateMember(Member member, String currentPassword) {
        try {
            String query = "SELECT * FROM member WHERE email='" + member.getEmail() + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (verifyPassword(currentPassword, rs.getString("password"))) {
                    query = "UPDATE member SET nickname=?,member_level=?,amount_bought=?,has_shop=?,disabled=? WHERE member_id=?";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setString(1, member.getNickname());
                    preparedStmt.setInt(2, member.getMemberLevel());
                    preparedStmt.setInt(3, member.getAmountBought());
                    preparedStmt.setInt(4, member.getHasShop() ? 1 : 0);
                    preparedStmt.setInt(5, member.getDisabled() ? 1 : 0);
                    preparedStmt.setInt(6, member.getMemberID());
                    preparedStmt.executeUpdate();
                    return "success";
                } else {
                    return "wrong_email_or_password";
                }
            } else {
                return "wrong_email_or_password";
            }
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String deleteMember(Member member) {
        try {
            String query = "DELETE FROM member WHERE member_id=?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, member.getMemberID());
            preparedStmt.executeUpdate();
            return "success";
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public String getMember(Member member) {
        try {
            int memberID = member.getMemberID();
            String email = member.getEmail();
            if (memberID == 0 && "".equals(email)) {
                return "no_key_found";
            } else {
                if (memberID == 0) {
                    String query = "SELECT member_id FROM member WHERE email='" + email + "'";
                    ResultSet rs = stmt.executeQuery(query);
                    if (rs.next()) {
                        memberID = rs.getInt("member_id");
                    } else {
                        return "email_not_found_in_database";
                    }
                }
                String query = "SELECT * FROM member WHERE member_id='" + memberID + "'";
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    member.setMemberID(memberID);
                    member.setEmail(rs.getString("email"));
                    member.setNickname(rs.getString("nickname"));
                    member.setMemberLevel(rs.getInt("member_level"));
                    member.setAmountBought(rs.getInt("amount_bought"));
                    member.setHasShop((rs.getInt("has_shop") == 1));
                    member.setDisabled((rs.getInt("disabled") == 1));
                    member.setRegDate(rs.getTimestamp("regdate"));
                    member.setLastLogin(rs.getTimestamp("last_login"));
                    return "success";
                } else {
                    return "member_not_found";
                }
            }
        } catch (SQLException E) {
            return "error_507";
        }
    }

    public Member[] getMembers() {
        try {
            String query = "SELECT COUNT(*) as row_count FROM member";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                Member[] members = new Member[rs.getInt("row_count")];
                query = "SELECT * FROM member";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    for (int i = 0; i < members.length; i++, rs.next()) {
                        members[i] = new Member();
                        members[i].setMemberID(rs.getInt("member_id"));
                        members[i].setEmail(rs.getString("email"));
                        members[i].setNickname(rs.getString("nickname"));
                        members[i].setMemberLevel(rs.getInt("member_level"));
                        members[i].setAmountBought(rs.getInt("amount_bought"));
                        members[i].setHasShop((rs.getInt("has_shop") == 1));
                        members[i].setDisabled((rs.getInt("disabled") == 1));
                        members[i].setRegDate(rs.getTimestamp("regdate"));
                        members[i].setLastLogin(rs.getTimestamp("last_login"));
                    }
                }
                return members;
            }
        } catch (SQLException E) {
        }
        return null;
    }

    public boolean has_shop(Member member) {
        try {
            String query = "SELECT has_shop FROM member WHERE member_id='" + member.getMemberID() + "'";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                if (rs.getInt("has_shop") == 1) {
                    return true;
                }
            }
        } catch (SQLException E) {
        }
        return false;
    }

    private String encryptPassword(String password) {
        return SCryptUtil.scrypt(password, 16384, 16, 1);
    }

    private boolean verifyPassword(String password, String hashedPassword) {
        return SCryptUtil.check(password, hashedPassword);
    }
}
