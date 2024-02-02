package com.user;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

@WebServlet(name = "ViewDocumentServlet", urlPatterns = {"/ViewDocumentServlet"})
public class ViewDocumentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/octet-stream");

        // Retrieve the document name from request parameters
        String documentName = request.getParameter("documentName");
        String jdbcUrl = "jdbc:mysql://localhost:3306/StoreFolder";
        String dbUsername = "root";
        String dbPassword = "Akanksha@123";

        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT document_data FROM user_documents WHERE document_name = ?");
            stmt.setString(1, documentName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Retrieve document data
                InputStream inputStream = rs.getBinaryStream("document_data");

                // Set response headers for file download
                String headerKey = "Content-Disposition";
                String headerValue = "attachment; filename=" + documentName;
                response.setHeader(headerKey, headerValue);

                // Copy the document's input stream to response's output stream
                OutputStream outputStream = response.getOutputStream();
                byte[] buffer = new byte[4096];
                int bytesRead = -1;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outputStream.close();
            } else {
                // Document not found
                response.getWriter().println("Document not found: " + documentName);
            }

            // Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            // Handle any database errors
            e.printStackTrace();
            response.getWriter().println("Error retrieving document: " + e.getMessage());
        }
    }

}
