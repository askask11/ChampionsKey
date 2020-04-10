/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-10 11:32:55
 * Description Of This Class:
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
 * @author Jianqing Gao
 */
public interface ServletProcessor
{
    //REQUIRED ATTRIBUTE request
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
    
    public void init(HttpServletRequest request, HttpServletResponse response,HttpServlet servlet);
    
    public void process() throws ServletException, IOException;
    
}
