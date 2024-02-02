<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Login</title>
</head>
<body>
    <%
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String message = "";
        
        // Database connection parameters
        String jdbcUrl = "jdbc:mysql://localhost:3306/StoreFolder";
        String dbUsername = "root";
        String dbPassword = "Akanksha@123";
        String driver = "com.mysql.cj.jdbc.Driver";
        
        try {
            // Load the driver
            Class.forName(driver);
            
            // Create connection
            Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
            
            // Prepare a statement to check login credentials
            String sql = "SELECT * FROM user_registration WHERE email=? AND password=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            // Execute the query
            ResultSet rs = pstmt.executeQuery();
            
            // Check if user exists
            if (rs.next()) {
                // User authenticated
//                message = "Login successful";
                session = request.getSession();
                session.setAttribute("email", email);
                session.setAttribute("password",password);
                response.sendRedirect("userData.jsp");
            } else {
                // Authentication failed
                message = "Invalid email or password";
            }
            
            // Close resources
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            // Handle exceptions
            message = "Error: " + e.getMessage();
            e.printStackTrace();
        }
    %>
    
    <h1>Login Status:</h1>
    <p><%= message %></p>
</body>
</html>
