<%@ page language="java" import="java.sql.*" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" href="styles.css" type="text/css">
    </head>
    <body>
        <%
            // Get user information from session
            String email = (String) session.getAttribute("email");
            String password = (String) session.getAttribute("password");

            // Retrieve user details from the database
            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            String jdbcUrl = "jdbc:mysql://localhost:3306/StoreFolder";
            String dbUsername = "root";
            String dbPassword = "Akanksha@123";

            String fname = null;
            String lname = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
                stmt = conn.prepareStatement("SELECT * FROM user_registration WHERE email=?");
                stmt.setString(1, email);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    // Get user details
                    fname = rs.getString("first_name");
                    lname = rs.getString("last_name");
                }
            } catch (Exception e) {
                e.printStackTrace();
                // Handle any exceptions
            } finally {
                // Close resources
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
        %>

        <div class="user-info">
            <h1>User Information</h1>
            <table border="0">
                <tbody>
                    <tr>
                        <td>Email:</td>
                        <td><%= email%></td>
                    </tr>
                    <tr>
                        <td>User Name:</td>
                        <td><%= fname + " " + lname%></td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div>
            <h1>Create Folder and Upload Documents</h1>
            <!-- Form to create a folder -->
            <form id="folderForm" method="post" action="CreateFolderServlet">
                <label>Folder Name:</label>
                <input type="text" name="folderName" id="folderName" required>
                <button class="button" type="submit">Create Folder</button>
            </form>


            <!-- Form to upload documents to a folder -->
            <form id="uploadForm" method="post" action="UploadDocumentServlet" enctype="multipart/form-data">

                <label for="folderSelect">Select Folder:</label>
                <select name="folderSelect" id="folderSelect" required>
                    Folder options will be populated dynamically 
                    <%
                        try {
                            conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
                            stmt = conn.prepareStatement("SELECT * FROM user_folders WHERE email=?");
                            stmt.setString(1, email);
                            rs = stmt.executeQuery();
                            while (rs.next()) {
                                String folderName = rs.getString("folder_name");
                    %>
                    <option value="<%= folderName%>"><%= folderName%></option>
                    <%
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Handle any exceptions
                        } finally {
                            // Close resources
                            if (rs != null) {
                                rs.close();
                            }
                            if (stmt != null) {
                                stmt.close();
                            }
                            if (conn != null) {
                                conn.close();
                            }
                        }
                    %>
                </select>
                <input type="file" name="fileInput" id="fileInput" multiple required accept=".pdf,.doc,.docx,.jpg,.jpeg,.png">
                <button class="button" type="submit">Upload Documents</button>
            </form>
        </div>
        <div>
    <form method="get" action="ViewFolderDocumentsServlet" enctype="multipart/form-data">

        <label for="folderSelect">Select Folder:</label>
        <select name="folderSelect" id="folderSelect" required>
            <!-- Folder options will be populated dynamically -->
            <%
                try {
                    conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
                    stmt = conn.prepareStatement("SELECT * FROM user_folders WHERE email=?");
                    stmt.setString(1, email);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String folderName = rs.getString("folder_name");
            %>
            <option value="<%= folderName %>"><%= folderName %></option>
            <%
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle any exceptions
                } finally {
                    // Close resources
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                }
            %>
        </select>

        <button class="button" type="submit">View Documents</button>
    </form>
</div>

<div>
    
    <form method="post" action="DeleteFolderServlet">
        <label for="folderSelectDelete">Select Folder to Delete:</label>
        <select name="folderSelectDelete" id="folderSelectDelete" required>
            <!-- Populate folder options dynamically -->
            <%
                try {
                    conn = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
                    stmt = conn.prepareStatement("SELECT * FROM user_folders WHERE email=?");
                    stmt.setString(1, email);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        String folderName = rs.getString("folder_name");
            %>
            <option value="<%= folderName %>"><%= folderName %></option>
            <%
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Handle any exceptions
                } finally {
                    // Close resources
                    if (rs != null) {
                        rs.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                    if (conn != null) {
                        conn.close();
                    }
                }
            %>
        </select>
        <button class="button" type="submit">Delete Folder</button>
    </form>
</div>

    </body>
</html>
