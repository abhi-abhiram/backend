package com.brandondonnelson;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ApiLogin extends HttpServlet {

   private static final long serialVersionUID = 1L;

   public void doPost(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException {

      response.setContentType("application/json");

      PrintWriter out = response.getWriter();

      String username = request.getParameter("username");
      String password = request.getParameter("password");

      if (username.equals("admin") && password.equals("admin")) {
         out.print("{\"success\": true}");
      } else {
         out.print("{\"success\": false}");
      }
   }

}