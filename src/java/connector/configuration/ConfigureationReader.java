/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-12 19:33:37
 * Description Of This Class: This is to read the configuration file for the database.
 */
package connector.configuration;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Jianqing Gao
 */
public class ConfigureationReader
{

    public static final String DB_USERNAME_PROPERTY_NAME="username";
    
    public final URL URL_DBACCESS_CONFIG= getClass().getResource("dbAccess.xml");
    
    
    
    public Properties getDbConnProperties() throws SAXException, IOException, ParserConfigurationException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        Properties properties = new Properties();
        DBConfigureHandler handler = new DBConfigureHandler(properties);
        parser.parse(URL_DBACCESS_CONFIG.openStream(), handler);
        return properties;
    }
    
    public static void main(String[] args)
    {
        try
        {
            //creates an object
            Properties pt = new ConfigureationReader().getDbConnProperties();
            //try to print the result
            System.out.println( "username=" + pt.getProperty("username") + ""
                    + "\npassword=" + pt.getProperty("password"));
            
        } catch (SAXException | IOException | ParserConfigurationException ex)
        {
            //exception handling
            Logger.getLogger(ConfigureationReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
