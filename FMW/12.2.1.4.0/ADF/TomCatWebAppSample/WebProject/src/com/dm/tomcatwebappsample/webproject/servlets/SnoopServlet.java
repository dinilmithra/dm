package com.dm.tomcatwebappsample.webproject.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "SnoopServlet", urlPatterns = { "/snoopservlet" })
public class SnoopServlet extends HttpServlet {
    private static final String CONTENT_TYPE = "text/html; charset=windows-1252";

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                                                   IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            try {
                RequestDispatcher view = request.getRequestDispatcher("welcome.jsp");
                view.forward(request, response);

            } finally {
                out.close();
            }
        }

    }

    /**Process the HTTP doGet request.
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE);
        //        PrintWriter out = response.getWriter();
        //        out.println("<html>");
        //        out.println("<head><title>SnoopServlet</title></head>");
        //        out.println("<body>");
        //        out.println("<p>The servlet has received a GET. This is the reply.</p>");
        //        out.println("</body></html>");
        //        out.close();

        processRequest(request, response);
    }
}
