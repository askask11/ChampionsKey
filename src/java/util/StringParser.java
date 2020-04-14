/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-14 0:42:48
 * Description Of This Class: This parse the String from hex to encoded string.
 */
package util;

import java.io.UnsupportedEncodingException;
import javax.xml.bind.DatatypeConverter;

/**
 * Get a string from hex code!
 * @author Jianqing Gao
 */
public class StringParser
{
    public static String getStringFromHex(String hex) throws UnsupportedEncodingException
    {
       return new String(DatatypeConverter.parseHexBinary(hex),"UTF-8");
       //
    }
}
