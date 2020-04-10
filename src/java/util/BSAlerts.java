/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-12 12:33:57
 * Description Of This Class: This is the class for generating Boorstrap messages.
 */
package util;

/**
 * This is the class for generating Boorstrap messages. See also:
 * <a href="See also: https://www.w3schools.com/bootstrap/bootstrap_alerts.asp">https://www.w3schools.com/bootstrap/bootstrap_alerts.asp</a>
 *
 * @author Jianqing Gao
 */
public class BSAlerts
{

    /**
     * Generates an HTML code which indicates a positive message, or a potential
     * possitive action. This genetation is based on bootstrap. See also:
     * <a href="See also: https://www.w3schools.com/bootstrap/bootstrap_alerts.asp">https://www.w3schools.com/bootstrap/bootstrap_alerts.asp</a>
     *
     * @param title The title of the HTML message to generated.
     * @param content The body content of the HTML message.
     * @return The message generated.
     */
    public static String successMessage(String title, String content)
    {
        return "<div class=\"alert alert-success\">" + messageBase(title, content);
    }

    /**
     * This generates a neutral HTML message. See also:
     * <a href="See also: https://www.w3schools.com/bootstrap/bootstrap_alerts.asp">https://www.w3schools.com/bootstrap/bootstrap_alerts.asp</a>
     *
     * @param title The title of the message
     * @param content The body content of the message.
     * @return The HTML message generated.
     */
    public static String infoMessage(String title, String content)
    {
        return "<div class=\"alert alert-info\">" + messageBase(title, content);
    }

    /**
     * This generates a warning HTML message with potential negative action. See
     * also:
     * <a href="See also: https://www.w3schools.com/bootstrap/bootstrap_alerts.asp">https://www.w3schools.com/bootstrap/bootstrap_alerts.asp</a>
     *
     * @param title The title of the message
     * @param content The body content of the message.
     * @return The message genetated
     */
    public static String warningMessage(String title, String content)
    {
        return "<div class=\"alert alert-warning\">" + messageBase(title, content);
    }

    /**
     * This indicates a danger message, or a potential negative action.
     * <div style="background-color: pink; color: red;"><strong>Warning</strong></div>
     * See also:
     * <a href="See also: https://www.w3schools.com/bootstrap/bootstrap_alerts.asp">https://www.w3schools.com/bootstrap/bootstrap_alerts.asp</a>
     *
     * @param title The title of the danger message
     * @param content The body content of the danger message.
     * @return The HTML message generated.
     */
    public static String dangerMessage(String title, String content)
    {
        return "<div class=\"alert alert-danger\">" + messageBase(title, content);
    }

    /**
     * The common base of a message. See also:
     * <a href="See also: https://www.w3schools.com/bootstrap/bootstrap_alerts.asp">https://www.w3schools.com/bootstrap/bootstrap_alerts.asp</a>
     *
     * @param title The title of a message
     * @param content The content of the messages.
     * @return The message base genetated to be used with other methods.
     */
    private static String messageBase(String title, String content)
    {
        return "<div style=\"float:right; cursor:pointer;\" onclick=\"this.parentElement.classList.toggle('hide'); \">X</div>"
                + "<strong>" + title + "</strong> &nbsp;" + content + "</div>";
    }

}
