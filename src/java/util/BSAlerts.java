/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-12 12:33:57
 * Description Of This Class:
 */
package util;

/**
 *
 * @author Jianqing Gao
 */
public class BSAlerts
{
    public static String successMessage(String title, String content)
    {
        return "<div class=\"alert alert-success\">"+messageBase(title, content);
    }
    
    public static String infoMessage(String title, String content)
    {
         return "<div class=\"alert alert-info\">"+messageBase(title, content);
    }
    
    public static String warningMessage(String title, String content)
    {
         return "<div class=\"alert alert-warning\">"+messageBase(title, content);
    }
    
    public static String dangerMessage(String title, String content)
    {
         return "<div class=\"alert alert-danger\">"+messageBase(title, content);
    }
    
    
    private static String messageBase(String title, String content)
    {
        return "<div style=\"float:right; cursor:pointer;\" onclick=\"this.parentElement.classList.toggle('hide'); \">X</div>"+
                "<strong>"+title+"</strong> &nbsp;" + content +"</div>";
    }
    
    
}
