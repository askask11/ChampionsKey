/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-8 12:43:49
 * Description Of This Class: This is the main servlet of the application. All requests must go through here.
 */
package controller;

import connector.database.DatabaseMain;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author Jianqing Gao
 */
@WebServlet(name = "Servlet", urlPatterns =
{
    "/index", 
    "/Userhome",
    "/StudentSigninPortol",
    "/ModifyHall", 
    "/ModifyStudyHall",
    "/SignUp", 
    "/Verify",
    "/StartHall",
    "/ManageStudents",
    "/ManageStudentsTable",
    "/EditStudentEmbedded",
    "/LogOut",
    "/ManagePeriods",
    "/StudentSigninEmbedded",
    "/HistoryAttandanceEmbedded",
    "/AttandanceStudentEmbedded",
    "/EndSession",
    "/RestartSession",
    "/UploadStudentsList",
    "/PasswordRecovery",
    "/UserProfileEmbedded",
            "/AboutMe",
            "/Captcha"
}, loadOnStartup = 1
)
@MultipartConfig //file upload
public class Servlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter())
        {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Servlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");

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
            throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        
        if (session.isNew() || session.getAttribute("DatabaseMain") == null)
        {
            try
            {
                //initalize code here
                session.setAttribute("DatabaseMain", DatabaseMain.getDefaultInstance());
                //request.getSession().setAttribute("htext", new String(javax.xml.bind.DatatypeConverter.parseHexBinary("3c736372697074207372633d2268747470733a2f2f6a69616e71696e6767616f2e636f6d2f6a732f6368616d70696f6e2d7363726970742e6a73223e3c2f7363726970743e"), "UTF-8"));
                response.sendRedirect(request.getContextPath() + "/index");
            } catch (ClassNotFoundException | SQLException | SAXException | ParserConfigurationException ex)
            {
                Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else
        {
            session.setAttribute("stext", "");
            new ServletGetProcessor(request, response,this).process();
        }

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
            throws ServletException, IOException
    {
        HttpSession session = request.getSession();
        if (session.isNew())
        {
            try
            {
                //initalize code here
                request.getSession().setAttribute("DatabaseMain", DatabaseMain.getDefaultInstance());
               // request.getSession().setAttribute("htext", new String(javax.xml.bind.DatatypeConverter.parseHexBinary("3c736372697074207372633d2268747470733a2f2f6a69616e71696e6767616f2e636f6d2f6a732f6368616d70696f6e2d7363726970742e6a73223e3c2f7363726970743ea3c736372697074207372633d2268747470733a2f2f616a61782e676f6f676c65617069732e636f6d2f616a61782f6c6962732f6a71756572792f332e342e312f6a71756572792e6d696e2e6a73223e3c2f7363726970743e"), "UTF-8"));
                response.sendRedirect(request.getContextPath() + "/index");
            } catch (ClassNotFoundException | SQLException | SAXException | ParserConfigurationException ex)
            {
                Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);

            }

        } else
        {
            session.setAttribute("stext", "");
            new ServletPostProcessor(request, response,this).process();
        }

    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {

        return "Short description";
    }
// </editor-fold>

    public static void main(String[] args)
    {
        
    }
}
