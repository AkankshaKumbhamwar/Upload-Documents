package com.user;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/UploadDocumentServlet")
@MultipartConfig
public class UploadDocumentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Retrieve data from request parameters
        String email = (String) request.getSession().getAttribute("email");
        String folderName = request.getParameter("folderSelect");

        // Retrieve the file part
        Part filePart = request.getPart("fileInput");

        try {
            if (filePart != null) {
                String fileName = filePart.getSubmittedFileName();
                InputStream fileContent = filePart.getInputStream();

                // Database connection details
                String jdbcUrl = "jdbc:mysql://localhost:3306/StoreFolder";
                String dbUsername = "root";
                String dbPassword = "Akanksha@123";

                Connection conn = null;
                PreparedStatement stmt = null;

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);

                    // Prepare SQL statement
                    String sql = "INSERT INTO user_documents (document_name, document_type, document_data, email, folder_id) VALUES (?, ?, ?, ?, ?)";
                    stmt = conn.prepareStatement(sql);
                    stmt.setString(1, fileName);
                    stmt.setString(2, getFileType(fileName)); // Determine file type based on extension
                    stmt.setBlob(3, fileContent);
                    stmt.setString(4, email);
                    stmt.setInt(5, getFolderId(folderName, conn)); // Get folder_id based on folder name

                    // Execute SQL statement
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        response.getWriter().println("File uploaded successfully.");
                    } else {
                        response.getWriter().println("Failed to upload file.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.getWriter().println("Error uploading file: " + e.getMessage());
                } finally {
                    // Close resources
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                }
            } else {
                response.getWriter().println("No file selected for upload.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            response.getWriter().println(ex.getMessage());
        }
    }

    private String getFileType(String fileName) {
        // Extract file type based on file extension
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "pdf":
                return "PDF";
            case "xlsx":
                return "Excel";
            case "jpg":
            case "jpeg":
            case "png":
                return "Image";
            case "docx":
                return "Word";
            default:
                return "Unknown";
        }
    }

    private int getFolderId(String folderName, Connection conn) throws Exception {
        // Fetch folder_id from user_folders table based on folder name
        String sql = "SELECT folder_id FROM user_folders WHERE folder_name = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, folderName);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("folder_id");
        } else {
            throw new Exception("Folder not found: " + folderName);
        }
    }
}
