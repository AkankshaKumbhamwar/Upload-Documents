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

@WebServlet(name = "DeleteDocumentServlet", urlPatterns = {"/DeleteDocumentServlet"})
public class DeleteDocumentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve the document ID from request parameters
        int documentId = Integer.parseInt(request.getParameter("documentId"));

        // Database connection details
        String jdbcUrl = "jdbc:mysql://localhost:3306/StoreFolder";
        String dbUsername = "root";
        String dbPassword = "Akanksha@123";

        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);

            // Prepare SQL statement to delete the document
            String sql = "DELETE FROM user_documents WHERE document_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, documentId);

            // Execute the delete operation
            int rowsAffected = stmt.executeUpdate();

            // Close resources
            stmt.close();
            conn.close();

            // Redirect back to the view folder documents page
            // Redirect back to the view folder documents page
            response.sendRedirect("ViewFolderDocumentsServlet?folderSelect=" + request.getParameter("folderSelect"));

        } catch (SQLException e) {
            // Handle any database errors
            e.printStackTrace();
            response.getWriter().println("Error deleting document: " + e.getMessage());
        }
    }
}
