package com.user;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(name = "ViewFolderDocumentsServlet", urlPatterns = {"/ViewFolderDocumentsServlet"})
public class ViewFolderDocumentsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Retrieve the selected folder name from request parameters
        String folderName = request.getParameter("folderSelect");
        String jdbcUrl = "jdbc:mysql://localhost:3306/StoreFolder";
        String dbUsername = "root";
        String dbPassword = "Akanksha@123";

        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
            PreparedStatement stmt = conn.prepareStatement("SELECT document_id, document_name FROM user_documents WHERE folder_id IN (SELECT folder_id FROM user_folders WHERE folder_name = ?)");
            stmt.setString(1, folderName);
            ResultSet rs = stmt.executeQuery();

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Documents in Folder</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Documents in Folder: " + folderName + "</h1>");
            out.println("<ul>");

// Iterate through the result set and display document names with clickable links
            while (rs.next()) {
                int documentId = rs.getInt("document_id");
                String documentName = rs.getString("document_name");
                // Generate link for viewing document
                out.println("<li><a href='ViewDocumentServlet?documentName=" + documentName + "'>" + documentName + "</a> "
                        + "<form method='post' action='DeleteDocumentServlet'>"
                        + "<input type='hidden' name='documentId' value='" + documentId + "'>"
                        + "<button type='submit'>Delete</button></form></li>");
            }

            out.println("</ul>");
            out.println("</body>");
            out.println("</html>");

            // Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            // Handle any database errors
            e.printStackTrace();
            out.println("Error retrieving documents: " + e.getMessage());
        } finally {
            out.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response); // Delegate to doGet method
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
