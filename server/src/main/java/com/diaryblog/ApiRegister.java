package com.diaryblog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

public class ApiRegister extends HttpServlet {

   private static final long serialVersionUID = 1L;

   private void insertData(Connection connection, String name, String email, String password) throws SQLException {
      String insertDataSQL = "INSERT INTO users (name,email,password) VALUES (?, ?, ?)";
      try {
         PreparedStatement statement = connection.prepareStatement(insertDataSQL);
         statement.setString(1, name);
         statement.setString(2, email);
         statement.setString(3, password);
         statement.execute();
      } catch (Exception e) {
         System.out.println("errror" + e.getStackTrace());
      }
   }

   public void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {

      BufferedReader reader = request.getReader();
      StringBuilder jsonBuffer = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
         jsonBuffer.append(line);
      }

      // Convert JSON string to a JSON Node
      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonData = mapper.readTree(jsonBuffer.toString());

      // send json response
      response.setContentType("application/json");

      String name = jsonData.get("name").asText();
      String email = jsonData.get("email").asText();
      String password = jsonData.get("password").asText();
      String confirmPassword = jsonData.get("confirm_password").asText();

      PrintWriter out = response.getWriter();

      if (!password.equals(confirmPassword)) {
         String json = "{\"error\":\"Passwords do not match\"}";
         out.println(json);
         return;
      }

      String hashedPassword = Utils.hashPassword(password);

      try {
         // Get a database connection
         Connection connection = DatabaseConnection.getConnection();
         insertData(connection, name, email, hashedPassword);

         connection.close();

         // Display the result
         String json = "{\"success\":\"true\"}";

         out.println(json);

      } catch (SQLException e) {
         e.printStackTrace();
         out.println("Database Error: " + e.getMessage());
      }
   }

}