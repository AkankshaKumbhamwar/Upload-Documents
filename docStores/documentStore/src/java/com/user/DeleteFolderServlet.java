package com.user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "DeleteFolderServlet", urlPatterns = {"/DeleteFolderServlet"})
public class DeleteFolderServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the folder name to be deleted from the request parameter
        String folderName = request.getParameter("folderSelectDelete");

        // Database connection details
        String jdbcUrl = "jdbc:mysql://localhost:3306/StoreFolder";
        String dbUsername = "root";
        String dbPassword = "Akanksha@123";

        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);

            // Prepare SQL statement to delete the folder and its associated documents
            String deleteFolderSQL = "DELETE FROM user_folders WHERE folder_name = ?";
            String deleteDocumentsSQL = "DELETE FROM user_documents WHERE folder_id IN (SELECT folder_id FROM user_folders WHERE folder_name = ?)";
            
            // Create a transaction to ensure consistency
            conn.setAutoCommit(false);

            // Execute the delete operation for documents first
            try (PreparedStatement deleteDocumentsStmt = conn.prepareStatement(deleteDocumentsSQL)) {
                deleteDocumentsStmt.setString(1, folderName);
                deleteDocumentsStmt.executeUpdate();
            }

            // Then, execute the delete operation for the folder
            try (PreparedStatement deleteFolderStmt = conn.prepareStatement(deleteFolderSQL)) {
                deleteFolderStmt.setString(1, folderName);
                deleteFolderStmt.executeUpdate();
            }

            // Commit the transaction
            conn.commit();

            // Close the database connection
            conn.close();

            // Redirect back to the user dashboard or any appropriate page
            response.sendRedirect("userData.jsp");
        } catch (SQLException e) {
            // Handle any database errors
            e.printStackTrace();
            response.getWriter().println("Error deleting folder: " + e.getMessage());
        }
    }
}
