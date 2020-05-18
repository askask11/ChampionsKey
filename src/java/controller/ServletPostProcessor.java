/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-10 11:29:52
 * Description Of This Class: This is a class for processing all post requests of 
 */
package controller;

import connector.database.DatabaseMain;
import connector.database.StaffManagement;
import connector.database.StudentManagement;
import connector.database.StudyhallManagement;
import connector.email.Mailer;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Attandance;
import model.AttandanceHistory;
import model.Staff;
import model.Student;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import util.BSAlerts;
import util.Captcha;
import util.Randomizer;
import util.SmartCss;

/**
 * This process all traffic of POST method.
 *
 * @author Jianqing Gao
 */
public class ServletPostProcessor implements ServletProcessor
{

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private String userPath;
    private HttpServlet servlet;

    /**
     * Initalizing method.
     *
     * @param request
     * @param response
     * @param servlet
     */
    public ServletPostProcessor(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet)
    {
        //super(request, response);
        init(request, response, servlet);
    }

    /**
     * {@inheritDoc }
     *
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void process() throws ServletException, IOException
    {
        switch (userPath)
        {
            case "/Userhome":
            {
                try
                {
                    //execute process method
                    processUserhomePOST();
                } catch (SQLException ex)
                {
                    //connection error handle
                    Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    session.setAttribute("loginMessage", BSAlerts.dangerMessage("Unable to connect to database", " We cannot connect to database, sorry!"));
                    response.sendRedirect(request.getContextPath() + "/index");
                }
            }
            break;

            case "/StudentSigninPortol":
            case "/PasswordRecovery":
                //for now, no check email etc.
                request.getRequestDispatcher("/WEB-INF" + request.getServletPath() + ".jsp").forward(request, response);
                break;
            case "/SignUp":
                processSignUpPOST();
                break;
            case "/StartHall":
            {
                try
                {
                    processStartHallPOST();
                } catch (SQLException ex)
                {
                    Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendError(500, ex.getMessage());
                }
            }
            break;
            case "/ManageStudentsTable":
            {
                try
                {
                    processStudentsTablePOST();
                } catch (SQLException ex)
                {
                    Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendError(500, ex.getMessage());
                }
            }
            break;
            case "/ManagePeriods":
                processManagePeriodsPOST();
                break;
            case "/StudentSigninEmbedded":
                processStudentSigninEmbeddedPOST();
                break;
            case "/AttandanceStudentEmbedded":
                processAttandanceStudentEmbeddedPOST();
                break;
            case "/EndSession":
                processEndSessionPOST();
                break;
            case "/RestartSession":
                processRestartSession();
                break;
            case "/UploadStudentsList":
                processAddStudentsBatch();
                break;
            case "/UserProfileEmbedded":
                processUserProfileEmbedded();
                break;
            default:
                processRequest(request, response);
                break;
        }

    }

    /**
     * User profile action activated!.
     *
     * @throws java.io.IOException
     * @throws javax.servlet.ServletException
     */
    protected void processUserProfileEmbedded() throws IOException, ServletException
    {
        //decleat attr, get required parameter.
        String action = request.getParameter("action");
        StaffManagement manager;
        DatabaseMain databaseMain = (DatabaseMain) session.getAttribute("DatabaseMain");
        Staff staff = (Staff) session.getAttribute("staff");
        //detect possible null pointer
        if (databaseMain == null || staff == null)
        {
            //reject, session expired.
            try (PrintWriter writer = response.getWriter())
            {
                writer.write("<h1>Your session is expired.</h1>"
                        + "Please <strong><a href='index' target='_parent'>log in>></a></strong>"
                        + "again!");
            }
        } else
        {
            //get manager
            manager = databaseMain.manageStaff();
            //determine action
            switch (action)
            {
                case "update-password":
                    //password update method
                    updateStaffPassword(staff, manager);
                    break;
                case "update-username":
                    //username handle method
                    updateStaffUsername(staff, manager);
                    break;
//            case "update-id":
//                break;
                case "update-legalname":
                    updateStaffLegalname(staff, manager);
                    break;
                default:
                    session.setAttribute("profileMessage", BSAlerts.dangerMessage("Invalid Action", "We don't regonize this action, please report to report@gaogato.com"));
                    break;
            }
        }

    }

    //<editor-fold>
    ///////////////////////////USER PROFILE UPDATES////////////////////////
    private void updateStaffLegalname(Staff staff, StaffManagement manager) throws ServletException, IOException
    {
        String legalName = request.getParameter("legalname");
        String profileMessage;
        //try to update user
        try
        {
            if (manager.updateLegalNameById(staff.getId(), legalName) == 1)
            {
                profileMessage = BSAlerts.successMessage("Successful!", "You name has been updated!");
                staff.setLegalName(legalName);
            } else
            {
                profileMessage = BSAlerts.infoMessage("Maybe updated", "Your info may be updated. If not, please contact report@gaogato.com!");
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, ex);
            profileMessage = BSAlerts.dangerMessage("Database error", " There is a error with database. If you continuously seeing this, please contact support@gaogato.com!");
        }
        //forward user
        session.setAttribute("profileMessage", profileMessage);
        request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);
    }

    private void updateStaffUsername(Staff staff, StaffManagement manager) throws ServletException, IOException
    {
        String userName = request.getParameter("username");
        String profileMessage;
        //try to update user
        try
        {
            if (manager.updateUserNameById(staff.getId(), userName) == 1)
            {
                profileMessage = BSAlerts.successMessage("Successful!", "You username has been updated!");
                staff.setUsername(userName);

            } else
            {
                profileMessage = BSAlerts.infoMessage("Maybe updated", "Your info may be updated. If not, please contact report@gaogato.com!");
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, ex);
            profileMessage = BSAlerts.dangerMessage("Database error", " There is a error with database. If you continuously seeing this, please contact support@gaogato.com!");
        }
        //forward user
        session.setAttribute("profileMessage", profileMessage);
        session.setAttribute("username", userName);
        request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);
    }

    private void updateStaffPassword(Staff staff, StaffManagement manager) throws ServletException, IOException
    {
        //declear vars
        String oldPassword, password1, password2;
        String profileMessage;
        //get params
        oldPassword = request.getParameter("oldPassword");
        password1 = request.getParameter("password1");
        password2 = request.getParameter("password2");
        if (!password1.equals(password2))
        {
            profileMessage = BSAlerts.warningMessage("Different Passwords", "Two passwords you entered are different. Please enter again!");
        } else
        {
            //they are same entries
            if (oldPassword.equals(password1))
            {
                //old password cannot equals to new password, reject
                profileMessage = BSAlerts.warningMessage("Same Password", "New password cannot be the same as the old ones.");
            } else
            {
                try
                {
                    manager.updatePasswordById(staff.getId(), password2);
                    profileMessage = BSAlerts.successMessage("Success!", "Your password has beed updated");
                    staff.setPassword(password2);
                    session.setAttribute("staff", staff);
                } catch (SQLException ex)
                {
                    Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, ex);
                    profileMessage = BSAlerts.dangerMessage("Database Error", " Please try again. If you continue seeing this, please contact support@gaogato.com !");
                }
            }
        }
        session.setAttribute("profileMessage", profileMessage);
        request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);
    }

    //</editor-fold>
    protected void processRestartSession() throws ServletException, IOException
    {
        Staff staff = (Staff) session.getAttribute("staff");
        DatabaseMain dbMain = (DatabaseMain) session.getAttribute("DatabaseMain");
        Attandance attandance = (Attandance) session.getAttribute("attandance");
        if (staff != null && dbMain != null && attandance == null)
        {
            try
            {
                StudyhallManagement manager = dbMain.manageStudyHall();
                if (manager.insertIntoActiveSession(Integer.parseInt(request.getParameter("id")), staff.getId()) == 1)
                {
                    session.setAttribute("attandance", manager.selectAttandanceRegisterByAttID(Integer.parseInt(request.getParameter("id"))));
                    response.sendRedirect(request.getContextPath() + "/Userhome");
                } else
                {
                    session.setAttribute("loginMessage", BSAlerts.infoMessage("We tried to update your request", "Please login again to see changes."));
                    response.sendRedirect(request.getContextPath() + "/index");
                }

            } catch (SQLException sqle)
            {
                Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, sqle);
                session.setAttribute("loginMessage", BSAlerts.dangerMessage("Connection error", " Please login again.. If this error continue occuring, please contact me."));
                response.sendRedirect(request.getContextPath() + "/index");
            } catch (NumberFormatException nfe)
            {
                session.setAttribute("loginMessage", BSAlerts.dangerMessage("NFError", " Please login again.. If this error continue occuring, please contact me."));
                response.sendRedirect(request.getContextPath() + "/index");
            }
        } else
        {
            session.setAttribute("loginMessage", BSAlerts.dangerMessage("Error", " Please login again.. If this error continue occuring, please contact me."));
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

    protected void processEndSessionPOST() throws ServletException, IOException
    {
        Staff staff = (Staff) session.getAttribute("staff");
        DatabaseMain dbMain = (DatabaseMain) session.getAttribute("DatabaseMain");
        Attandance attandance = (Attandance) session.getAttribute("attandance");
        if (staff != null && dbMain != null && attandance != null)
        {
            try
            {
                if (dbMain.manageStudyHall().deleteFromActiveSession(attandance.getAttandanceID(), staff.getId()) == 1)
                {
                    //yeah deleted.
                    session.setAttribute("attandance", null);
                    response.sendRedirect(request.getContextPath() + "/Userhome");
                } else
                {
                    session.setAttribute("loginMessage", BSAlerts.infoMessage("We tried to update your request", "Please login again to see changes."));
                    response.sendRedirect(request.getContextPath() + "/index");
                }
            } catch (SQLException ex)
            {
                Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, ex);
                session.setAttribute("loginMessage", BSAlerts.dangerMessage("Connection error", " Please login again.. If this error continue occuring, please contact me."));
                response.sendRedirect(request.getContextPath() + "/index");

            }
        } else
        {
            session.setAttribute("loginMessage", BSAlerts.dangerMessage("Error", " Please login again.. If this error continue occuring, please contact me."));
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

    public void processAttandanceStudentEmbeddedPOST() throws ServletException, IOException
    {
        Staff staff = (Staff) session.getAttribute("staff");
        DatabaseMain dbMain = (DatabaseMain) session.getAttribute("DatabaseMain");
        Attandance activeAttandance = (Attandance) session.getAttribute("attandance");
        boolean isEditingCurrentAttendance;
        StudyhallManagement manager;
        int attandanceID, studentID;
        String action;
        String studentMessage = "";
        int rowsAffected;
        String input;
        int inputInt;
        ZoneId timeZoneId = ZoneId.of("UTC-5");
        if (staff == null)
        {
            response.sendRedirect(request.getContextPath() + "/index");
        } else if (dbMain == null)
        {
            try (PrintWriter out = response.getWriter())
            {
                out.write("<h1>Your session is expired! Please logout and then login again!");
            }
        } else
        {
            manager = dbMain.manageStudyHall();
            try
            {
                attandanceID = Integer.parseInt(request.getParameter("attandanceID"));
                if (activeAttandance != null)
                {
                    isEditingCurrentAttendance = attandanceID == activeAttandance.getAttandanceID();
                } else
                {
                    isEditingCurrentAttendance = false;
                }
                studentID = Integer.parseInt(request.getParameter("studentID"));
                action = request.getParameter("action");
                //rowsAffected = 0;

                //possible action
                //update-location : update location code
                //update-attandance : attandanceCode
                //update-description : update the description for a student.
                switch (action)
                {
                    case "update-location":
                        input = request.getParameter("location");
                        inputInt = Integer.parseInt(input);
                        if (isEditingCurrentAttendance)
                        {
                            rowsAffected = manager.updateAttandanceHistoryLocationById(studentID, attandanceID, inputInt, timeZoneId);
                        } else
                        {
                            rowsAffected = manager.updateAttandanceHistoryLocationById(studentID, attandanceID, inputInt);
                        }
                        break;
                    case "update-attandance":
                        input = request.getParameter("code");
                        inputInt = Integer.parseInt(input);
                        if (isEditingCurrentAttendance)
                        {
                            rowsAffected = manager.updateAttandanceCodeById(studentID, attandanceID, inputInt, timeZoneId);
                        } else
                        {
                            rowsAffected = manager.updateAttandanceCodeById(studentID, attandanceID, inputInt);
                        }
                        break;
                    case "update-description":
                        input = request.getParameter("description");
                        if (isEditingCurrentAttendance)
                        {
                            rowsAffected = manager.updateAttandanceHistoryDescriptionById(studentID, attandanceID, input, timeZoneId);
                        } else
                        {
                            rowsAffected = manager.updateAttandanceHistoryDescriptionById(studentID, attandanceID, input);
                        }
                        break;
                    case "insert":

                        //temporarily insert a student in a class.
                        if (manager.isStduentExistsByAttandanceID(studentID, attandanceID))
                        {
                            rowsAffected = -1;//student exists, naaa.
                        } else
                        {
                            if (dbMain.manageStudents().isStudentExistsById(studentID))
                            {
                                //the student in db, insert
                                rowsAffected = manager.insertIntoAttendanceHistory(attandanceID, studentID, AttandanceHistory.ATTANDANCE_PRESENT, AttandanceHistory.LOCATION_CHAMPION_HALL, ZoneId.of("UTC-5"), "Temporary Register");
                            } else
                            {
                                rowsAffected = -1;
                                studentMessage = BSAlerts.dangerMessage("Student Not Found", "This student is not found in our database.");
                            }
                        }
                        break;
                    default:
                        rowsAffected = -1;
                        studentMessage = BSAlerts.warningMessage("Update Failed!", "Error: Unknown operation @Servlet Post Processor");
                        break;
                }

                switch (rowsAffected)
                {
                    case 1:
                        studentMessage = BSAlerts.successMessage("Success!", " Successfully updated student attendance!");
                        break;
                    case -1:
                        //do nothing
                        break;
                    default:
                        studentMessage = BSAlerts.infoMessage("Update attempted", "We have attempted to update student list. Please double check to make sure it is updated.");
                        break;
                }

            } catch (NumberFormatException nfe)
            {
                studentMessage = BSAlerts.dangerMessage("Please input numbers when necessary!", " Number parsing error!");
            } catch (SQLException sqle)
            {
                studentMessage = BSAlerts.dangerMessage("Cannot connect to database.", "Please logout and login again. If this error continue happening, please tell us!");
            }
            session.setAttribute("studentMessage", studentMessage);
            request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);
        }
    }

    /**
     *
     * @throws ServletException
     * @throws IOException
     */
    public void processStudentSigninEmbeddedPOST() throws ServletException, IOException
    {
//        String by = request.getParameter("by");
        String input = request.getParameter("id");
        Staff staff = (Staff) session.getAttribute("staff");
        SmartCss signinEmbedBodyCss = new SmartCss(), statusCss = new SmartCss(), successQuoteCss = new SmartCss();
        LocalTime now, tardyTime;
        String titleIcon = "", titleText = "", subTitle = "", statusText = "", successQuote = "", successQuoteFooter = "", toolText = "";
        Attandance attandance = (Attandance) session.getAttribute("attandance");

        int id;
        int index;
        if (staff != null && attandance != null)
        {
            DatabaseMain databaseMain = (DatabaseMain) session.getAttribute("DatabaseMain");
            if (databaseMain != null)
            {
                StudyhallManagement manager = databaseMain.manageStudyHall();
                StudentManagement studentManagement = databaseMain.manageStudents();
                //set iframe border property
                signinEmbedBodyCss.setProperty("background-color", "rgba(255,255,255,0.4)");
                signinEmbedBodyCss.setProperty("border-width", "4px");
                signinEmbedBodyCss.setProperty("border-style", "dashed");
                signinEmbedBodyCss.setProperty("border-color", "#003399");
                signinEmbedBodyCss.setProperty("height", "100% !important");
                successQuoteCss.setProperty("display", "none");
                now = LocalTime.now(ZoneId.of("UTC-5"));
                tardyTime = attandance.getTardyTime();
                try
                {
                    id = Integer.parseInt(input);
                    if (manager.isStduentExistsByAttandanceID(id, attandance.getAttandanceID()))
                    {
                        if (AttandanceHistory.ATTANDANCE_ABSENT == manager.getAttandanceCodeByStudentId(id, attandance.getAttandanceID()))
                        {
                            //if the update was successful
                            if (1 == manager.updateAttandanceCodeById(id, attandance.getAttandanceID(), ((now.isBefore(tardyTime)) ? AttandanceHistory.ATTANDANCE_PRESENT : AttandanceHistory.ATTANDANCE_TARDY), ZoneId.of("UTC-5")))
                            {
                                //set border color
                                signinEmbedBodyCss.setProperty("border-color", "#00cc00");
                                //DEFAULT location is champions hall.
                                manager.updateAttandanceHistoryLocationById(id, attandance.getAttandanceID(), AttandanceHistory.LOCATION_CHAMPION_HALL, ZoneId.of("UTC-5"));
                                //generate an emoji
                                titleIcon = "&#" + Attandance.SUCCESS_EMOJIES[Randomizer.randomInt(0, Attandance.SUCCESS_EMOJIES.length - 1)] + "";
                                titleText = "Welcome, " + studentManagement.getStudentNameById(id);//welcome title
                                subTitle = "Please make yourself comfortable and begin working.";
                                index = Randomizer.randomInt(0, Attandance.SUCCESS_QUOTES.length - 1);//generate success quote.
                                successQuote = Attandance.SUCCESS_QUOTES[index];
                                successQuoteFooter = Attandance.SUCCESS_QUOTE_FOOTERS[index];
                                successQuoteCss.setProperty("display", "block");
                                statusCss.setProperty("color", "#33ff33;");
                                //determine user attendance code.
                                statusText = "Success! Status OK " + (now.isBefore(attandance.getTardyTime()) ? "&#" + Attandance.ONTIME_EMOJI : "&#" + Attandance.LATE_EMOJI);

                            } else
                            {
                                //update failed
                                signinEmbedBodyCss.setProperty("border-color", "#ffff00");
                                titleIcon = "&#128550";
                                titleText = "Attention, " + studentManagement.getStudentNameById(id);
                                subTitle = "Later, please make sure with the teacher that you're marked as present.";

                                successQuoteCss.setProperty("display", "none");
                                statusCss.setProperty("color", "#ffff00;");
                                statusText = "Furture operation required. " + (now.isBefore(attandance.getTardyTime()) ? "&#" + Attandance.ONTIME_EMOJI : "&#" + Attandance.LATE_EMOJI);
                            }

                        } else
                        {
                            //user already sign in.
                            signinEmbedBodyCss.setProperty("border-color", "#00ffcc");
                            titleIcon = "&#" + Attandance.ALREADY_SIGNIN_EMOJI[Randomizer.randomInt(0, Attandance.ALREADY_SIGNIN_EMOJI.length - 1)];
                            titleText = "Hi, " + studentManagement.getStudentNameById(id);
                            subTitle = "It looks like you already signed in. If in doubt, please ask the teacher on duty. Thank you!";
                            successQuoteCss.setProperty("display", "none");
                            statusCss.setProperty("color", "#0033ff;");
                            statusText = "Already signed in. No further operation needed.";//"Furture operation required. " + (now.isBefore(attandance.getTardyTime())?"&#"+Attandance.ONTIME_EMOJI:"&#"+Attandance.LATE_EMOJI);

                        }

                    } else
                    {
                        //student not found;
                        if (studentManagement.isStudentExistsById(id))
                        {
                            signinEmbedBodyCss.setProperty("border-color", "#ff9900");
                            titleIcon = "&#128559";
                            titleText = "Hi, " + studentManagement.getStudentNameById(id);
                            subTitle = "You are found in our database but not in the period.";
//                            toolText = "<applet code=\"AddStudentInPeriodEmbedded.class\">"
//                                    + "<param name=\"attandanceID\" value=\"" + attandance.getAttandanceID() + "\">"
//                                    + "<param name=\"periodID\" value=\"" + attandance.getPeriodID() + "\">"
//                                    + "<param name=\"studentID\" value=\"" + id + "\">"
//                                    + "</applet>";
                            //security issues, do not use this
                        } else
                        {
                            signinEmbedBodyCss.setProperty("border-color", "#ff0033");
                            titleIcon = "&#128559";
                            titleText = "Student Not Found in the database!";
                            subTitle = " If you are enrolled in thie period studyhall, please tell your teacher later to register you in!";
                        }
                        successQuoteCss.setProperty("display", "none");
                        statusCss.setProperty("color", "#ff0000;");
                        statusText = "Further operation required!";

                        //"Furture operation required. " + (now.isBefore(attandance.getTardyTime())?"&#"+Attandance.ONTIME_EMOJI:"&#"+Attandance.LATE_EMOJI);
                    }
                    //any error catched here forward as usual
                } catch (SQLException ex)
                {
                    Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, ex);
                } catch (NumberFormatException nfe)
                {
                    signinEmbedBodyCss.setProperty("border-color", "#ff0000");
                    titleIcon = "&#128566";
                    titleText = "Please enter real numbers";
                    subTitle = "Only integers are accepted.";
                    successQuoteCss.setProperty("display", "none");
                    statusCss.setProperty("color", "#ff0000;");
                    statusText = "Furture operation required. "; //+ (now.isBefore(attandance.getTardyTime()) ? "&#" + Attandance.ONTIME_EMOJI : "&#" + Attandance.LATE_EMOJI);
                }

            } else
            {
                signinEmbedBodyCss.setProperty("border-color", "#ff0000");
                titleIcon = "&#128564";
                titleText = "Connection error! Session is expired.";
                subTitle = "Please logout and log back in.";
                successQuoteCss.setProperty("display", "none");
                statusCss.setProperty("color", "#ff0000;");
                statusText = "Please login again.";
            }
        } else
        {
            signinEmbedBodyCss.setProperty("border-color", "#ff0000");
            titleIcon = "&#128564";
            titleText = "Session is expired.";
            subTitle = "Please logout and log back in.";
            successQuoteCss.setProperty("display", "none");
            statusCss.setProperty("color", "#ff0000;");
            statusText = "Please login again.";
        }

        //Assign attributes to the session.
        session.setAttribute("signinEmbedBodyCss", signinEmbedBodyCss);
        session.setAttribute("statusCss", statusCss);
        session.setAttribute("successQuoteCss", successQuoteCss);
        session.setAttribute("titleIcon", titleIcon);
        session.setAttribute("titleText", titleText);
        session.setAttribute("subTitle", subTitle);
        session.setAttribute("statusText", statusText);
        session.setAttribute("successQuote", successQuote);
        session.setAttribute("successQuoteFooter", successQuoteFooter);
        session.setAttribute("toolText", toolText);

        //send out the request.
        request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);
        //psb results 1.success 2. already signin 3. student not found 4. found student, not in this period.
    }

    /**
     * A form about period is submitted.
     *
     * @throws ServletException
     * @throws IOException
     */
    public void processManagePeriodsPOST() throws ServletException, IOException
    {
        //declear attributes and set up params.
        String action = request.getParameter("action");
        // String param;
        String idString;
        Integer periodId;
        Staff staff = (Staff) session.getAttribute("staff");
        String managePeriodsMessage = "";
        if (staff != null)
        {
            DatabaseMain dbMain = (DatabaseMain) session.getAttribute("DatabaseMain");
            if (dbMain != null)
            {
                StudyhallManagement manager = dbMain.manageStudyHall();
                // StudentManagement studentManagement = dbMain.manageStudents();
                periodId = (Integer) session.getAttribute("periodID");
                switch (action)
                {
                    case "delete":
                        idString = request.getParameter("id");

                        {
                            try
                            {
                                if (1 == manager.deleteStudentIdFromPeriodsByPeriodId(Integer.parseInt(idString), periodId))
                                {
                                    managePeriodsMessage = BSAlerts.successMessage("Success", " The student has been successfully deleted!");

                                } else
                                {
                                    managePeriodsMessage = BSAlerts.infoMessage("Deletion attempted.", "We attempted to delete this student.");
                                }
                            } catch (SQLException ex)
                            {
                                Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, ex);
                                managePeriodsMessage = BSAlerts.dangerMessage("Connection error", "Please try to logout and login again.");
                            }
                        }

                        break;

                    case "insert":
                    {
                        try
                        {
                            if (!manager.isThisStudentInThisPeriodById(Integer.parseInt(request.getParameter("id")), periodId))
                            {
                                manager.insertIntoStudentInPeriod(Integer.parseInt(request.getParameter("id")), periodId);
                                managePeriodsMessage = BSAlerts.successMessage("Success", " Inserted successfully!");
                            } else
                            {
                                managePeriodsMessage = BSAlerts.warningMessage("ID exists", "The student already exists in this period.");
                            }

                        } catch (SQLException ex)
                        {
                            managePeriodsMessage = BSAlerts.dangerMessage("connection error", "We recommend you to logout and login again.");
                            Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (NumberFormatException nfe)
                        {
                            managePeriodsMessage = BSAlerts.dangerMessage("EMPTY", "No student available here.");
                        }
                    }
                    break;

                }
                try
                {
                    session.setAttribute("dbObj", manager.selectStudentsByPeriodId(periodId));//refresh
                } catch (SQLException ex)
                {
                    response.sendRedirect(request.getContextPath() + "/index");
                    Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, ex);
                }
                session.setAttribute("managePeriodsMessage", managePeriodsMessage);
                request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);
            } else
            {
                response.sendRedirect(request.getContextPath() + "/index");
            }

        } else
        {
            response.sendRedirect(request.getContextPath() + "/index");//no auth, kick user to index
        }

    }

    /**
     * Process the POST method of students table. Usually this is for updating
     * operation.
     *
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     */
    public void processStudentsTablePOST() throws ServletException, IOException, SQLException
    {
        //1.get what the user wanna to do.
        String action = request.getParameter("action");
        //2.get what should the system do when reloading the list
        //Object listLoadModeObj 
        String listLoadMode = (String) session.getAttribute("student-list-select-mode");
        String listLoadFilterName = (String) session.getAttribute("student-filter-name");
        // String listLoadFilterName;
        int rowsAffected;
        //2.2 get db operator
        DatabaseMain dbMain = (DatabaseMain) session.getAttribute("DatabaseMain");
        Staff staff = (Staff) session.getAttribute("staff");
        //get target student id
        String idString = request.getParameter("id");

        int id;// = Integer.parseInt(idString);//try-catch is not necessary here since user is not supposted to edit the hidden field.
        //if user does that, they deserve an error page.
        String param;
        String manageStudentsTableMessage = "";
        ArrayList<Student> list;

        //This attribute is required for insertion
        Student student = new Student();

        if (dbMain != null)
        {
            StudentManagement manager = dbMain.manageStudents();
            //3. check user editing privliage(if the user has logged in)
            if (staff != null)
            {
                //4. check for null operation mode
                if (listLoadMode == null)
                {
                    listLoadMode = "select-all";
                }
                //5.determine the actual action (what to update/delete)
                switch (action)
                {
                    case "update-name":
                        //update id by name.
                        param = request.getParameter("name");
                        id = Integer.parseInt(idString);
                        if (param != null)
                        {
                            if (param.length() != 0)
                            {
                                rowsAffected = manager.updateStudentsNameById(id, param);
                            } else
                            {
                                rowsAffected = -1;
                                manageStudentsTableMessage = BSAlerts.warningMessage("Invalid name", "A student must have name. Please enter the valid name for the student.");
                            }

                        } else
                        {
                            //if the rows affected is -1, then at the end, do not re-init the message
                            rowsAffected = -1;
                            manageStudentsTableMessage = BSAlerts.warningMessage("Empty Name", "Sorry. The student must have a name. ");
                        }
                        break;
                    case "update-id":
                        id = Integer.parseInt(idString);
                        param = request.getParameter("new-id");
                        if (param != null)
                        {
                            try
                            {
                                //execute update method.
                                if (!manager.isStudentExistsById(Integer.parseInt(param)))
                                {
                                    rowsAffected = manager.updateStudentsIdById(id, Integer.parseInt(param));
                                } else
                                {
                                    rowsAffected = -1;
                                    manageStudentsTableMessage = BSAlerts.warningMessage("Duplicated ID", "Student's ID is duplicated. Try again!");
                                }

                            } catch (NumberFormatException e)
                            {
                                rowsAffected = -1;
                                manageStudentsTableMessage = BSAlerts.warningMessage("Please enter valid numbers for ID", "Invalid input detected.");
                            }
                        } else
                        {
                            rowsAffected = -1;
                            manageStudentsTableMessage = BSAlerts.warningMessage("Empty ID", " A student must come with an id. Please do not leave this space blank.");
                        }
                        break;
                    case "update-grade":
                        //update student's grade
                        //get param accordingly
                        id = Integer.parseInt(idString);
                        param = request.getParameter("grade");
                        //check empty param
                        if (param != null)
                        {
                            try
                            {
                                rowsAffected = manager.updateStudentsGradeById(id, Integer.parseInt(param));
                            } catch (NumberFormatException e)
                            {
                                rowsAffected = -1;
                                manageStudentsTableMessage = BSAlerts.warningMessage("Empty/invalid grade", " The grade is empty/invalid. Please change the grade input.");
                            }
                        } else
                        {
                            rowsAffected = -1;
                            manageStudentsTableMessage = BSAlerts.warningMessage("Empty parameter.", " Please enter something in your input field.");
                        }
                        break;
                    case "delete":
                        id = Integer.parseInt(idString);
                        rowsAffected = manager.deleteFromStudentsById(id);
                        manager.deleteStudentFromPeriodByStuId(id);//delete all period record.
                        dbMain.manageStudyHall().deleteStudentsFromAttendanceHistoryByStuID(id);///clear all records of this stu.
                        break;
                    case "insert":
                        //check name
                        param = request.getParameter("name");
                        if (param == null || param.length() == 0)
                        {
                            //name invalid, break.
                            manageStudentsTableMessage = BSAlerts.warningMessage("Empty name", "Please re-enter student's name and try again!");
                            rowsAffected = -1;
                        } else
                        {
                            //set the name attribute of student
                            student.setName(param);
                            //check grade
                            param = request.getParameter("grade");
                            if (param == null || param.length() == 0)
                            {
                                //register fail
                                rowsAffected = -1;
                                manageStudentsTableMessage = BSAlerts.warningMessage("Empty grade", "Please select a grade!");
                            } else
                            {
                                try
                                {
                                    //write the grade to the student obj
                                    student.setGrade(Integer.parseInt(param));
                                    //check id
                                    param = request.getParameter("id");
                                    try
                                    {
                                        //try to parse ID into a int
                                        id = Integer.parseInt(param);
                                        student.setId(id);
                                        //*********FINAL INSERTION***********//
                                        if (!manager.isStudentExistsById(id))
                                        {
                                            rowsAffected = manager.insertIntoStudents(student);
                                        } else
                                        {
                                            rowsAffected = -1;
                                            manageStudentsTableMessage = BSAlerts.warningMessage("Duplicated ID", "ID is duplicated. Please try with another id.");
                                        }

                                    } catch (NumberFormatException e)
                                    {
                                        rowsAffected = -1;
                                        manageStudentsTableMessage = BSAlerts.warningMessage("Invalid entry for ID", " Please check the id part of the student.");
                                    }
                                } catch (NumberFormatException e)
                                {
                                    rowsAffected = -1;
                                    manageStudentsTableMessage = BSAlerts.warningMessage("Invalid entry for grade", "Please check the \"grade\" for the new student.");
                                }
                            }
                        }
                        break;
                    case "insert-batch":
                        //manageStudentsTableMessage = insertBatchIntoStudents();
                        rowsAffected = 0; // don't let the default handler handle this.
                        break;
                    default:
                        rowsAffected = -1;
                        manageStudentsTableMessage = BSAlerts.dangerMessage("Please do not change hidden input field!", " Please don't change any hidden attributes. We don't want to blacklist your IP.");
                        break;
                }

                //6.After successfully updated, reload the list.
                switch (rowsAffected)
                {
                    //see how many rows affected. IF -1,0 DO NOT RELOAD LIST.
                    case -1:
                        break;
                    default:
                        switch (listLoadMode)
                        {
                            case "select-all":
                                //select all students from the list
                                list = manager.selectFromStudentList();
                                //assign list to the table,
                                session.setAttribute("studentList", list);
                                break;
                            case "select-by-name":
                                //filter the student by name
                                if (listLoadFilterName != null)
                                {
                                    list = manager.selectFromStudentListFilterByName(listLoadFilterName);
                                } else
                                {
                                    list = manager.selectFromStudentList();
                                }
                                //assign list to the table.
                                session.setAttribute("studentList", list);
                                break;
                            //this is the switch inside switch.
                            default:
                                //if no mode, send redir
                                response.sendRedirect(request.getContextPath() + "/ManageStudentsTable");
                                break;
                        }
                        manageStudentsTableMessage = ((rowsAffected == 1) ? BSAlerts.successMessage("Successful", " Students list has been successfully updated.") : BSAlerts.infoMessage("Update attemped", "We tried to update your list, but the operation may not be successful. Please check your new list. (return=" + rowsAffected + ")"));
                        break;
                }

                // manageStudentsTableMessage = BSAlerts.successMessage("Success!", " The student list has been updated.");
                //7.Assign Object to session
                session.setAttribute("manageStudentsTableMessage", manageStudentsTableMessage);

                //8.redirection
                request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);

            } else
            {
                //nologin priviliage
                response.sendError(403, "You did not login. This is restricted area. If you are a staff, you session may expired. Please login again.");
            }
        } else
        {
            //db object lost
            response.sendError(400, "Session expired! Please click \"log out\" and login again.");
        }

    }

    public void processAddStudentsBatch() throws IOException
    {
        insertBatchIntoStudents();
        response.sendRedirect(request.getContextPath() + "/ManageStudentsTable");
    }

    /**
     * Insert a group of students into the database, read from student's file.
     *
     * @throws IOException Any file error.
     */
    public void insertBatchIntoStudents() throws IOException
    {
        final int MEMORY_THRESHOLD = 1024 * 1024 * 3;  // 3MB
        final int MAX_FILE_SIZE = 1024 * 1024 * 5; // 5MB
        final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
        String manageStudentsTableMessage = "";
        //final String UPLOAD_DIRECTORY = "upload";
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload;
        File storeFile = File.createTempFile("quickinsertstu", ".txt");
        storeFile.deleteOnExit();//no need to hold this file.
        DatabaseMain dbMain = (DatabaseMain) session.getAttribute("DatabaseMain");
        StudentManagement studentManagement = dbMain.manageStudents();
        int[] rows;
        int studentsAdded = 0;
        if (!ServletFileUpload.isMultipartContent(request))
        {
            manageStudentsTableMessage = BSAlerts.dangerMessage("File Type Error", "Sorry, it looks like you are not uploading a file here.");
        } else
        {

            // sets memory threshold - beyond which files are stored in disk
            factory.setSizeThreshold(MEMORY_THRESHOLD);
            // sets temporary location to store files
            factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
            upload = new ServletFileUpload(factory);

            // sets maximum size of upload file
            upload.setFileSizeMax(MAX_FILE_SIZE);

            // sets maximum size of request (include file + form data)
            upload.setSizeMax(MAX_REQUEST_SIZE);

            //<editor-fold>
            //there is no need to construct because file will be deleted anyway.
            // constructs the directory path to store upload file
            // this path is relative to application's directory
//            String uploadPath = servlet.getServletContext().getRealPath("")
//                    + File.separator + UPLOAD_DIRECTORY;
            // creates the directory if it does not exist
//            File uploadDir = new File(uploadPath);
//            if (!uploadDir.exists())
//            {
//                uploadDir.mkdir();
//            }
//</editor-fold>
            try
            {
                // parses the request's content to extract file data
                List<FileItem> formItems = upload.parseRequest(request);

                // iterates over form's fields
                for (FileItem item : formItems)
                {
                    // processes only fields that are not form fields
                    if (!item.isFormField())
                    {
//                            String fileName = new File(item.getName()).getName();
//                            String filePath = uploadPath + File.separator + fileName;
//                            File storeFile = new File(filePath);
                        // saves the file on disk
                        item.write(storeFile);
                    }
                }

                rows = studentManagement.insertStudentsInBatch(storeFile);

                for (int i = 0; i < rows.length; i++)
                {
                    int row = rows[i];
                    studentsAdded += row;
                }
                manageStudentsTableMessage = BSAlerts.successMessage("Successful!", studentsAdded + " students are added.");

//                }else
//                {
//                    manageStudentsTableMessage = BSAlerts.warningMessage("Empty upload", "Please upload something!");
//                }
            } catch (NumberFormatException nfex)
            {
                manageStudentsTableMessage = BSAlerts.warningMessage("Format error", "Please check if the file uploaded has followed the format.");
            } catch (Exception ex)
            {
                ex.printStackTrace();
                manageStudentsTableMessage = BSAlerts.dangerMessage("Unknown error", ex.getMessage());
            }

            //
        }

        session.setAttribute("manageStudentsTableMessage", manageStudentsTableMessage);
//        return manageStudentsTableMessage;
    }

    /**
     * POST method of start a hall. This is executed when user submit a data.
     *
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     */
    public void processStartHallPOST() throws ServletException, IOException, SQLException
    {

        int attandanceID = Randomizer.randomInt(0, 99999999);//generate a new attandance ID.
        Staff operatedBy = (Staff) session.getAttribute("staff");//get the user info
        //get db operator
        DatabaseMain database = (DatabaseMain) session.getAttribute("DatabaseMain");
        StudyhallManagement manager = database.manageStudyHall();
        //get user defined tardy time
        String tardyTimeString = request.getParameter("tardyTime");
        int periodId = Integer.parseInt(request.getParameter("periods"));
        boolean goSignin = request.getParameter("goSignin") != null;
        /**
         * *************************WRITE A sql CONFIGURATION TO SLOVE THIS
         * PROBLEM!DAYLIGHT SAVING******************************
         */
        LocalDate today = LocalDate.now(ZoneId.of("UTC-5"));//get our school date right now.
        LocalTime tardyTime = LocalTime.parse(tardyTimeString);//get user defined tardy time.

        if (operatedBy != null)
        {
            int teacherID = operatedBy.getId();
            int existingSession = manager.selectFromActiveSessionById(teacherID);
            Attandance attandance = new Attandance();
            if (existingSession == -1)
            {
                ArrayList<Integer> studentIDs;
                //1. register into an active session
                manager.insertIntoActiveSession(attandanceID, teacherID);
                //2. Create an attandance record
                attandance.setAttandanceID(attandanceID);
                attandance.setDate(today);
                attandance.setPeriodID(periodId);
                attandance.setTardyTime(tardyTime);
                attandance.setTeacherID(teacherID);
                manager.insertIntoAttandanceRegister(attandance);
                //3.flush all students in the record with the attandance id given.
                studentIDs = manager.selectFromStudentsInPeriodByPeriodId(periodId);
                manager.insertBatchIntoAttandanceHistoryDefault(attandanceID, studentIDs, ZoneId.of("UTC-5"));
                //4.kick user to sign-in page if the checkbox is checked. Else, kick to userhome.

                if (goSignin)
                {
                    session.setAttribute("attandance", attandance);//register it in.
                    response.sendRedirect(request.getContextPath() + "/StudentSigninPortol");
                } else
                {
                    response.sendRedirect(request.getContextPath() + "/Userhome");
                }
            } else
            {
                session.setAttribute("stext", BSAlerts.warningMessage("Existing session", "You currently have an active studyhall. Please end this hall before you start a new one."));
                response.sendRedirect(request.getContextPath() + "/Userhome");
            }
        } else
        {
            session.setAttribute("stext", BSAlerts.warningMessage("Login REQUIRED", "Please login first"));
            response.sendRedirect(request.getContextPath() + "/index");
        }

    }

    /**
     * Process the request for user to sign up.
     *
     * @throws ServletException
     * @throws IOException
     */
    public void processSignUpPOST() throws ServletException, IOException
    {
        String message;

        try
        {
            String username, email, legalname, password, id, bash, captchaInput;
            //String bashLink = "http://localhost:8080/ChampionsKey/Verify?bash=";
            DatabaseMain connector = DatabaseMain.getDefaultInstance();
            StaffManagement manager = connector.manageStaff();

            //get required params
            username = request.getParameter("username");
            email = request.getParameter("email");
            legalname = request.getParameter("legalname");
            password = request.getParameter("password");
            id = request.getParameter("id");
            captchaInput = request.getParameter("captcha");

            Captcha captcha = (Captcha) session.getAttribute("captcha");

            if (captchaInput.equals(captcha.getBody()))
            {
                //check if this is a staff email
                if (LocalDateTime.now(ZoneId.of("UTC")).isBefore(captcha.getExpireTime()))
                {
                    if (email.endsWith("@thevillageschool.com") || email.equals("jianqing_gao@s.thevillageschool.com"))
                    {
                        //check email duplicatioon
                        if (!manager.isUserExistsByEmail(email))
                        {
                            //CHECK USERNAME DUPLICATION
                            if (!manager.isUserExistsByUsername(username))
                            {
                                //try to register the account first
                                manager.insertIntoStaffTable(new Staff(email, username, password, legalname, Integer.parseInt(id)), -1);
                                bash = Randomizer.randomLetterNumber(60, true, "qwertyuiopasdfghjklzxcvbnm".toCharArray());
                                //bashLink += bash;

                                if (manager.registerBash(Integer.parseInt(id), bash) == 1)
                                {
                                    Mailer.sendUserVerificationMail(email, bash);
                                    message = BSAlerts.successMessage("success", "Please check your email for update! If you found a blank mail, click \"expand trimmed content.\" If there is actually nothing, please contact report@gaogato.com. ");
                                } else
                                {
                                    message = BSAlerts.warningMessage("Warning", "Maybe there is an exception! There is something wrong anyway.");
                                }

                            } else
                            {
                                message = BSAlerts.warningMessage("Username is registered", "Please use another username.");
                            }
                        } else
                        {
                            //email duplicated
                            message = BSAlerts.warningMessage("Email is registered", "Please check your email");
                        }
                    } else
                    {
                        message = BSAlerts.infoMessage("Restricted Register", "Sorry! We only allow staffs to sign up for this system."
                                + "If you are a village staff, please sign up use your school email!");
                    }
                } else
                {
                    message = BSAlerts.warningMessage("Code expired", " Your verification code is expired. Please try again.");
                }
            } else
            {
                //verifiction code wrong
                message = BSAlerts.dangerMessage("Verification code wrong", " You have entered wrong verification code. Please try again later.");
            }
            //onnector.getDbConn().close();
        } catch (SQLException ex)
        {
            Logger.getLogger(ServletPostProcessor.class.getName()).log(Level.SEVERE, null, ex);
            message = BSAlerts.dangerMessage("Database Exception", "We failed to save your info, please try again later!");
        } catch (NumberFormatException nfe)
        {
            message = BSAlerts.dangerMessage("Please enter number in ID", "Please enter numbers only in ID field!<script>document.getElementById('idField').focus();</script>");
        } catch (MessagingException me)
        {
            message = BSAlerts.dangerMessage("Email error", "We failed to send your email! sorry!");//"<div class='alert alert-warning><strong></strong></div>";
            me.printStackTrace();
        } catch (Exception ue)
        {
            message = BSAlerts.dangerMessage("Error", "Unknown exception!");//"<div class='alert alert-warning><strong></strong></div>";
            ue.printStackTrace();
        }

        session.setAttribute("signupMessage", message);
        request.getRequestDispatcher(userPath + ".jsp").forward(request, response);

    }

    /**
     * User login method,
     *
     * @throws SQLException
     * @throws IOException
     * @throws ServletException
     */
    public void processUserhomePOST() throws SQLException, IOException, ServletException
    {
        String email, password;
        email = request.getParameter("email");
        password = request.getParameter("password");
        DatabaseMain databaseMain = (DatabaseMain) session.getAttribute("DatabaseMain");//do class parsing to improve speed
        StaffManagement manager = databaseMain.manageStaff();
        StudyhallManagement hallManager = databaseMain.manageStudyHall();
        String loginMessage = "", currentStudyHallStatusInfo = "", currentPeriodName = "", activeAttandanceID = "";
        Attandance att = null;
        int activeSessionId;
        SmartCss activeDivCss = new SmartCss(), inactiveDivCss = new SmartCss();
        if (email != null && password != null)
        {

            Staff staff = manager.selectStaffByEmail(email, password);
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
                        //session.setAttribute("attandance", att);//attandance
                    } else
                    {
                        currentStudyHallStatusInfo = BSAlerts.warningMessage("Error", " There is an error in the system.");
                    }
                }

                //Set attribute back
                session.setAttribute("activeAttandanceID", activeAttandanceID);
                session.setAttribute("loginMessage", loginMessage);
                session.setAttribute("currentStudyHallStatusInfo", currentStudyHallStatusInfo);
                session.setAttribute("currentPeriodName", currentPeriodName);
                session.setAttribute("attandance", att); ////// ////// ///// IF USER ON ACTIVE ATTANDANCE, THEN INSERT "ATTANDANCE" OBJECT IN JAVABEANS///// //// ////
                session.setAttribute("activeDivCss", activeDivCss.getAttributeStyle());
                session.setAttribute("inactiveDivCSS", inactiveDivCss.getAttributeStyle());
                request.getRequestDispatcher("/WEB-INF" + userPath + ".jsp").forward(request, response);
            } else
            {
                //send redirect back to index
                session.setAttribute("loginMessage", BSAlerts.warningMessage("Incorrect email/password", " Please check your email again!"));
                response.sendRedirect(request.getContextPath() + "/index");
            }
        } else
        {
            loginMessage = BSAlerts.warningMessage("Info missing ", "Please enter your email!");
            response.sendRedirect(request.getContextPath() + "/index");
        }
    }

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

    /**
     * default security page.
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
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

    //</editor-fold>
}
