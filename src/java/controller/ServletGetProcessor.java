/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-10 11:28:48
 * Description Of This Class:This execute all methods of HTTP GET request
 */
package controller;

import connector.database.DatabaseMain;
import connector.database.StaffManagement;
import connector.database.StudentManagement;
import connector.database.StudyhallManagement;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Attandance;
import model.Staff;
import model.Student;
import util.BSAlerts;
import util.SmartCss;

/**
 *
 * @author Jianqing Gao
 */
public class ServletGetProcessor implements ServletProcessor
{

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private String userPath;
    private HttpServlet servlet;

    public ServletGetProcessor(HttpServletRequest request, HttpServletResponse response,HttpServlet servlet)
    {
        init(request, response,servlet);
    }

    @Override
    public void process() throws ServletException, IOException
    {
        //test, delete later
        //session.setAttribute("username", "askask11");

        switch (userPath)
        {
            case "/index":
            case "/SignUp":
            case "/AboutMe":
                forward(userPath);
                break;
            case "/Userhome":
            {
                try
                {
                    processUserhomeGET();
                } catch (SQLException ex)
                {
                    Logger.getLogger(ServletGetProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute("loginMessage", BSAlerts.dangerMessage("Connection ERROR", "Maybe your session is expired. Please login again!."));
                    response.sendRedirect(request.getContextPath() + "/index");
                }
            }
                break;


            case "/StudentSigninPortol":

            case "/ModifyStudyHall":
            case "/StartHall":
            case "/EditStudentEmbedded":
            case "/UserProfileEmbedded":
                forward("/WEB-INF" + userPath);
                break;
            case "/Verify":
                processVerifyGET();
                break;
            case "/ManageStudents":
                processManageStudentsGET();
                break;
            case "/ManageStudentsTable":
            {
                try
                {
                    processManageStudentsTableGET();
                } catch (SQLException ex)
                {
                    Logger.getLogger(ServletGetProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    //session.setAttribute("stext", BSAlerts.dangerMessage("Connection Error", "Please try again later."));
                }
            }
            break;

            case "/LogOut":
                session.invalidate();
                response.sendRedirect(request.getContextPath() + "/index");
                break;
            case "/ManagePeriods":
                processManagePeriodsGET();
                break;
            case "/HistoryAttandanceEmbedded":
                processHistoryAttandanceEmbeddedGET();
                break;
            case "/AttandanceStudentEmbedded":
                processAttandanceStudentEmbeddedGET();
                break;
            case "/PasswordRecovery":
                processPasswordRecoveryGET();
                break;
            default:
                processRequest();
                break;
        }
    }
    
    public void processAttandanceStudentEmbeddedGET()throws ServletException, IOException
    {
        String filter = request.getParameter("filter");
        String input = request.getParameter("input");
        String id = request.getParameter("id");
        if(filter == null || filter.isEmpty())
        {
            session.setAttribute("filter", null);
        }else
        {
            session.setAttribute("filter", filter);
            session.setAttribute("input", Integer.parseInt(input));
        }
        session.setAttribute("id", Integer.parseInt(id));
        session.setAttribute("studentMessage", "");
        request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);
    }
    
    public void processPasswordRecoveryGET() throws ServletException, IOException
    {
        request.getRequestDispatcher("/WEB-INF"+userPath + ".jsp").forward(request, response);
    }

    public void processUserhomeGET() throws ServletException, IOException, SQLException
    {
        //String email, password;
//        email = request.getParameter("email");
//        password = request.getParameter("password");
        DatabaseMain databaseMain = (DatabaseMain) session.getAttribute("DatabaseMain");//do class parsing to improve speed
        StaffManagement manager = databaseMain.manageStaff();
        StudyhallManagement hallManager = databaseMain.manageStudyHall();
        String loginMessage = "", currentStudyHallStatusInfo = "", currentPeriodName = "", activeAttandanceID = "";
        Attandance att = null;
        int activeSessionId;
        SmartCss activeDivCss = new SmartCss(), inactiveDivCss = new SmartCss();
        Staff staff = (Staff) session.getAttribute("staff");

        if (staff != null)
        {
            //process userhome 
            session.setAttribute("staff", staff);
            activeSessionId = hallManager.selectFromActiveSessionById(staff.getId());
            session.setAttribute("username", staff.getUsername());
            if (activeSessionId == -1)
            {
                activeDivCss.setProperty("display", "none");
                inactiveDivCss.setProperty("display", "block");
            } else
            {
                activeDivCss.setProperty("display", "block");
                inactiveDivCss.setProperty("display", "none");
                //1.select the whole attandance object from db
                att = hallManager.selectAttandanceRegisterByAttID(activeSessionId);
                //2. load information 
//                    currentStudyHallStatusInfo = "You are currentl";
//                   activeAttandanceID = att.getAttandanceID() + "";
                if (att != null)
                {
                    currentStudyHallStatusInfo = "You are currently in " + hallManager.selectDescriptionFromPeriodByID(att.getPeriodID()) + "";
                    activeAttandanceID = att.getAttandanceID() + "";
                    session.setAttribute("attandance", att);//attandance
                } else
                {
                    currentStudyHallStatusInfo = BSAlerts.warningMessage("Error", " There is an error in the system.");
                }
            }

            session.setAttribute("activeAttandanceID", activeAttandanceID);
            session.setAttribute("loginMessage", loginMessage);
            session.setAttribute("currentStudyHallStatusInfo", currentStudyHallStatusInfo);
            session.setAttribute("currentPeriodName", currentPeriodName);
            session.setAttribute("attandance", att);
            session.setAttribute("activeDivCss", activeDivCss.getAttributeStyle());
            session.setAttribute("inactiveDivCSS", inactiveDivCss.getAttributeStyle());
            request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);
        }else
        {
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }
    
    
    
    

    public void processHistoryAttandanceEmbeddedGET() throws ServletException, IOException
    {
        Object staff = session.getAttribute("staff");
        Object attandance = session.getAttribute("attandance");
        DatabaseMain dbMain = (DatabaseMain)session.getAttribute("DatabaseMain");
        String action = request.getParameter("action");
        String studentMessage = "";
        
        if (staff != null && dbMain != null)
        {
            if(action != null)
            {
                if(action.equals("delete"))
                {
                    try
                    {
                        deleteFromHistoryAttendanceById(dbMain);
                        studentMessage = BSAlerts.successMessage("Success", "This class has been successfully deleted.");
                    } catch (SQLException ex)
                    {
                        Logger.getLogger(ServletGetProcessor.class.getName()).log(Level.SEVERE, null, ex);
                        studentMessage = BSAlerts.dangerMessage("Failed", " Connection error! Please try again later.");
                    }
                }
                
            }
            session.setAttribute("periodMessage", studentMessage);
            request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);
        } else
        {
//            response.sendRedirect(request.getContextPath() + "/index");
            //tell user error
            try (PrintWriter out = response.getWriter())
            {
                out.println("<html><body>");
                out.println("<h1>Session is expired! Please login again!</h1>"
                        + "\n </html></body>");
            }
        }
    }

    private void deleteFromHistoryAttendanceById(DatabaseMain dbMain) throws SQLException
    {
        int id = Integer.parseInt(request.getParameter("id"));
        StudyhallManagement studyhallManagement = dbMain.manageStudyHall();
        studyhallManagement.deleteFromAttendanceHistoryRegisterByAttId(id);
    }
    /**
     * Process manange period.
     *
     * @throws ServletException
     * @throws IOException
     */
    public void processManagePeriodsGET() throws ServletException, IOException
    {
        String periodId = request.getParameter("period-id");
        //check if it is a plain get request or
        //check auth!!!always check auth for restricted area!
        Staff staff = (Staff) session.getAttribute("staff");
        SmartCss tableCss = new SmartCss();
        String managePeriodsMessage = "";
        int id;
        if (staff != null)
        {
            DatabaseMain dbMain = (DatabaseMain) session.getAttribute("DatabaseMain");

            if (dbMain != null)
            {
                StudyhallManagement manager = dbMain.manageStudyHall();
                if (periodId == null || periodId.length() == 0)
                {
                    //oh, it is a plain get request
                    //then just forward it, but set the table invisiable.
                    tableCss.setProperty("display", "none");

                    try
                    {
                        session.setAttribute("periods", manager.selectFromPeriods());

                    } catch (SQLException ex)
                    {
                        Logger.getLogger(ServletGetProcessor.class.getName()).log(Level.SEVERE, null, ex);
                        managePeriodsMessage = BSAlerts.dangerMessage("Error", "Your session might has been expired. Please sign-out and sign-in again.");
                    }
//                    session.setAttribute("managePeriodsMessage", managePeriodsMessage);
//                    request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);
                } else
                {
                    //the useer selected which period to mamage. Need to be able to respond it!
                    //create the table from here.
                    try
                    {
                        id = Integer.parseInt(periodId);
                        session.setAttribute("dbObj", manager.selectStudentsByPeriodId(id));
                        session.setAttribute("periodID", id);

                    } catch (NumberFormatException e)
                    {

                        //user should not get here unless he changed the source code.
                        managePeriodsMessage = BSAlerts.dangerMessage("Integer Parsing Error", "Please do not change the source code of HTML. We don't want to ban your IP!");

                    } catch (SQLException ex)
                    {
                        managePeriodsMessage = BSAlerts.dangerMessage("Connection error", " There is something wrong with the connection. Please try to logout and login again!");
                        Logger.getLogger(ServletGetProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                session.setAttribute("tableCss", tableCss);
                session.setAttribute("managePeriodsMessage", managePeriodsMessage);
                request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);
            } else
            {
                response.sendRedirect(request.getContextPath() + "/index");//db obj expired, kick away
            }

        } else
        {
            response.sendRedirect(request.getContextPath() + "/index");//no auth, kick back!
        }

    }

    public void processVerifyGET() throws ServletException, IOException //throws SQLException, ClassNotFoundException
    {
        String verifyMessageTitle;
        String verifyMessageBody = "";
        try //throws SQLException, ClassNotFoundException
        {
            String bash = request.getParameter("bash");
            DatabaseMain main = (DatabaseMain) session.getAttribute("DatabaseMain");//StaffManagement.getDefaultInstance();
            StaffManagement manager = main.manageStaff();
            if (manager.confirmVerifyBash(bash))
            {
                verifyMessageTitle = "Yes! Your account has been activated!";
                verifyMessageBody = "Now, you can login with your account!";
            } else
            {
                verifyMessageTitle = "Verification failed.";
                verifyMessageBody = "The code you provided is not valid. It may has expired or terminated.";
            }
//        } catch (  ex)
//        {
//            Logger.getLogger(ServletGetProcessor.class.getName()).log(Level.SEVERE, null, ex);
//            verifyMessageTitle = "Failed!";
//            verifyMessageBody= "Auth failed due to connection error.";
//        }
        } catch (Exception e)
        {
            e.printStackTrace();
            verifyMessageTitle = "Na! Unknown error!";
        }

        session.setAttribute("verifyMessageTitle", verifyMessageTitle);
        session.setAttribute("verifyMessageBody", verifyMessageBody);

        forward(userPath);

    }

    public void processManageStudentsGET() throws IOException, ServletException
    {
        if (session.getAttribute("username") == null)
        {
            response.sendRedirect(request.getContextPath() + "/index");

        } else
        {
            forward("/WEB-INF" + userPath);
        }
    }

    /**
     * Manage students, guide user to designated table.
     *
     * @throws IOException
     * @throws ServletException
     * @throws SQLException
     */
    public void processManageStudentsTableGET() throws IOException, ServletException, SQLException
    {
        Staff staff = (Staff) session.getAttribute("staff");
        String action = request.getParameter("action");
        String name = request.getParameter("name");
        //String manageStudentsTableMessage = action == null ? "";
        if(action != null)
        {
            session.setAttribute("manageStudentsTableMessage", "");
        }
        //possible actions = "select-all" or "select-by-name"

        if (null != staff)//check eligibility of operating
        {
            DatabaseMain dbMain = (DatabaseMain) session.getAttribute("DatabaseMain");
            if (dbMain != null)//check error
            {
                //1.get manager
                StudentManagement studentManagement = dbMain.manageStudents();

                //2.Declear attr
                ArrayList<Student> studentList;

               if(action == null)
               {
                   action = "select-all";
               }
                ///DETERMINE user's filter: either search by name or not.
                switch (action)
                {
                    case "select-all":
                        //3.Put all students in session
                        studentList = studentManagement.selectFromStudentList();
                        //remember the select mode
                        session.setAttribute("student-list-select-mode", action);
                        break;
                    case "select-by-name":
                        //check if the user has filled in the space
                        if (name != null)
                        {
                            //if yes, run search program according to the name.
                            studentList = studentManagement.selectFromStudentListFilterByName(name);
                        } else
                        {
                            //if no, load the complete list.
                            studentList = studentManagement.selectFromStudentList();
                        }
                        //remember the select mode
                        session.setAttribute("student-list-select-mode", action);
                        session.setAttribute("student-filter-name", name);

                        break;
                    default:
                        //session.setAttribute("", staff);
                        studentList = new ArrayList<>();
                        response.sendError(400, "Illegal parameter! Please do not try to send random param here!");
                        break;
                }

                //load the list into the session
                session.setAttribute("studentList", studentList);
                //session.setAttribute("manageStudentsTableMessage", "");//clear message

                //dispatch user
                request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);

            } else
            {
                session.setAttribute("stext", BSAlerts.dangerMessage("Session error", "Please login again, sorry for inconvinence"));
                response.sendRedirect(request.getContextPath() + "/index");
            }
        } else
        {
            session.setAttribute("stext", BSAlerts.dangerMessage("Restricted Area", "Please login to access the page."));
            response.sendRedirect(request.getContextPath() + "/index");
        }

    }

    //servlet process methods
    //<editor-fold>
    @Override
    public HttpServletRequest getHttpServletRequest()
    {
        return request;
    }

    @Override
    public void setHttpServletRequest(HttpServletRequest request)
    {
        this.request = request;
    }

    @Override
    public HttpServletResponse getHttpServletResponse()
    {
        return this.response;
    }

    @Override
    public void setHttpServletResponse(HttpServletResponse response)
    {
        this.response = response;
    }

    @Override
    public void init(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet)
    {
        this.request = request;
        this.response = response;
        this.session = request.getSession();
        this.userPath = request.getServletPath();
        this.servlet = servlet;
    }

    @Override
    public HttpSession getHttpSession()
    {
        return session;
    }

    @Override
    public void setHttpSession(HttpSession session)
    {
        this.session = session;
    }

    @Override
    public String getServletPath()
    {
        return userPath;
    }

    public void forward(String path) throws ServletException, IOException
    {
        request.getRequestDispatcher(path + ".jsp").forward(request, response);
    }

    protected void processRequest()
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
            out.println("You probably want to go " + userPath + "</body>");
            out.println("</html>");
        }
    }

    //</editor-fold>
}
