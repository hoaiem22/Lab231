/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emvh.controller;

import emvh.dao.RegistrationDAO;
import emvh.dto.Role;
import emvh.dto.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Admin
 */
public class SearchController extends HttpServlet {

    private final String ERROR = "error.jsp";
    private final String SUCCESS = "admin.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            String searchValue = request.getParameter("txtSearch");
            String searchRole = request.getParameter("searchRole");
            RegistrationDAO dao = new RegistrationDAO();
            List<User> listUser = new ArrayList<>();
            if (searchRole != null) {
                if(searchRole.equals("ALL")){
                    searchRole = "%";
                }
                listUser = dao.searchLikeNameWithRole(searchValue, searchRole);
            } else {
                listUser = dao.searchLikeName(searchValue);
            }
            if (listUser != null) {
                url = SUCCESS;
                request.setAttribute("listUser", listUser);
                List<Role> listRole = dao.getAllRole();
                request.setAttribute("listRole", listRole);
                request.setAttribute("ROLE", searchRole);
                Map<String, Integer> map = dao.countRole();
                request.setAttribute("map", map);
                request.setAttribute("TOTAL", dao.countAllUser());
            } else {
                request.setAttribute("ERROR", "Search failed");
            }
        } catch (Exception e) {
            log("ERROR at SearchController: " + e.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
