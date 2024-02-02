package com.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Retrieve form data
        String firstName = request.getParameter("fname");
        String lastName = request.getParameter("lname");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String country = request.getParameter("country");
        String state = request.getParameter("state");
        String city = request.getParameter("city");
        String password = request.getParameter("password");

        try (PrintWriter out = response.getWriter()) {
            // Save the user data to the database
            boolean registrationSuccessful = saveUserDataToDatabase(firstName, lastName, email, phone, country, state, city, password, out);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<body>");
            if (registrationSuccessful) {
                out.println("<h1>Registration Successful!</h1>");
                out.println("<p>First Name: " + firstName + "</p>");
                out.println("<p>Last Name: " + lastName + "</p>");
                out.println("<p>Email: " + email + "</p>");
                out.println("<p>Phone: " + phone + "</p>");
                out.println("<p>Country: " + country + "</p>");
                out.println("<p>State: " + state + "</p>");
                out.println("<p>City: " + city + "</p>");
            } else {
                out.println("<h1>Registration Failed!</h1>");
            }
            out.println("</body>");
            out.println("</html>");
        }
    }

    private boolean saveUserDataToDatabase(String firstName, String lastName, String email, String phone,
            String country, String state, String city, String password, PrintWriter out) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/StoreFolder", "root", "Akanksha@123")) {
            connection.setAutoCommit(false);

            // Check if the email already exists
            if (emailExists(connection, email)) {
                out.println("<script>alert('Email already exists! Please choose a different email.')</script>");
                return false; // Exit the method if email exists
            }

            // Insert user data into the database
            String sql = "INSERT INTO user_registration (first_name, last_name, email, phone, country, state, city, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, firstName);
                statement.setString(2, lastName);
                statement.setString(3, email);
                statement.setString(4, phone);
                statement.setString(5, country);
                statement.setString(6, state);
                statement.setString(7, city);
                statement.setString(8, password);

                statement.executeUpdate();
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean emailExists(Connection connection, String email) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM user_registration WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }
}
