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
import com.diaryblog.Exceptions.IncorrectPasswordException;
import com.diaryblog.Exceptions.EmailNotFound;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class ApiLogin extends HttpServlet {

   private static final long serialVersionUID = 1L;

   private User retrieveData(Connection connection, String email, String password)
         throws SQLException, IncorrectPasswordException, EmailNotFound, Exception {
      String retrieveDataSQL = "SELECT * FROM users where email = ?";

      try {
         PreparedStatement statement = connection.prepareStatement(retrieveDataSQL);
         statement.setString(1, email);
         ResultSet resultSet = statement.executeQuery();

         if (!resultSet.next()) {
            throw new EmailNotFound("Email not found");
         }

         int id = resultSet.getInt("id");
         String name = resultSet.getString("name");
         String em = resultSet.getString("email");
         String hash = resultSet.getString("password");

         if (!Utils.checkPassword(password, hash)) {
            throw new IncorrectPasswordException("Password is incorrect");
         }

         return new User(name, em, id);

      } catch (Exception e) {

         System.out.println("errorrrrr" + e.getMessage());
         throw e;
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

      ObjectMapper mapper = new ObjectMapper();
      JsonNode jsonData = mapper.readTree(jsonBuffer.toString());

      response.setContentType("application/json");
      PrintWriter out = response.getWriter();

      // get email and password
      try {
         String email = jsonData.get("email").asText();
         String password = jsonData.get("password").asText();

         Connection connection = DatabaseConnection.getConnection();

         User result = retrieveData(connection, email, password);

         String json = mapper.writeValueAsString(result);

         out.println(json);
      } catch (Exception e) {
         System.out.println("error" + e.getStackTrace());
         out.println("{\"error\":\"" + e.getMessage() + "\"}");
      }
   }

}

class User {
   @JsonProperty("name")
   private String name;
   @JsonProperty("email")
   private String email;
   @JsonProperty("id")
   private int id;

   public User(String name, String email, int id) {
      this.name = name;
      this.email = email;
      this.id = id;
   }
}