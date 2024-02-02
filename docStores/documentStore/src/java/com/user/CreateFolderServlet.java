/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CreateFolderServlet")
public class CreateFolderServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String folderName = request.getParameter("folderName");
        String email = (String) request.getSession().getAttribute("email");

        // Database connection details
        String jdbcUrl = "jdbc:mysql://localhost:3306/StoreFolder";
        String dbUsername = "root";
        String dbPassword = "Akanksha@123";
        
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
            
            // Check if folder name already exists for the current user
            String checkSql = "SELECT * FROM user_folders WHERE folder_name = ? AND email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, folderName);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                // Folder with the same name already exists for the user
                response.getWriter().println("Folder with the same name already exists.");
                return; // Stop further execution
            }

            // Insert the folder name into the database
            String insertSql = "INSERT INTO user_folders (folder_name, email) VALUES (?, ?)";
            stmt = conn.prepareStatement(insertSql);
            stmt.setString(1, folderName);
            stmt.setString(2, email);
            stmt.executeUpdate();
            response.sendRedirect("userData.jsp"); // Redirect after successful folder creation
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error creating folder: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
