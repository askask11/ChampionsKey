/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-10 11:32:55
 * Description Of This Class: This is a blueprint of a servlet processor.
 */
package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A blueprint of typical servlet processor.
 *
 * @author Jianqing Gao
 */
public interface ServletProcessor
{

    //REQUIRED ATTRIBUTE request
    /**
     * Get the request send by the client.
     *
     * @return The request obj.
     */
    public HttpServletRequest getHttpServletRequest();

    public void setHttpServletRequest(HttpServletRequest request);

    //responds
    public HttpServletResponse getHttpServletResponse();

    public void setHttpServletResponse(HttpServletResponse response);

    //session
    public HttpSession getHttpSession();

    public void setHttpSession(HttpSession session);

    //userpath
    public String getServletPath();

    //set the constr
    /**
     * Initalize a processor.
     *
     * @param request
     * @param response
     * @param servlet
     */
    public void init(HttpServletRequest request, HttpServletResponse response, HttpServlet servlet);

    /**
     * Prcesses a servlet request. (Either doget or dopost)
     *
     * @throws ServletException
     * @throws IOException
     */
    public void process() throws ServletException, IOException;

}
