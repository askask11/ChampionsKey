/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-12 19:35:06
 * Description Of This Class:This is the "handler" of the config reader.
 */
package connector.configuration;

import java.util.Properties;
//import jdk.internal.org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This is the "handler" of the config reader.
 * @author Jianqing Gao
 */
public class DBConfigureHandler extends DefaultHandler
{
    private boolean readUsername = false;
    private boolean readPassword = false;
    private boolean readAddress = false;
    private boolean readDbName=false;
    private boolean readUseSSL = false;
    private Properties properties;

    /**
     * Creates a default object of the handler.
     */
    public DBConfigureHandler()
    {
        properties = new Properties();
    }

    /**
     * Create an object of this handler useing given properties.
     * @param properties 
     */
    public DBConfigureHandler(Properties properties)
    {
        this.properties = properties;
    }

    /**
     * {@inheritDoc }
     * @param uri
     * @param localname
     * @param qName
     * @param attributes 
     */
    @Override
    public void startElement(String uri, String localname, String qName, Attributes attributes)
    {
        switch (qName)
        {
            case "username":
                readUsername = true;
                break;
            case "password":
                readPassword = true;
                break;
            case "address":
                readAddress = true;
                break;
            case "dbName":
                readDbName = true;
                break;
            case "useSSL":
                readUseSSL = true;
                break;
        }
    }

    /**
     * {@inheritDoc}
     * @param uri
     * @param localname
     * @param qName
     * @throws SAXException 
     */
    @Override
    public void endElement(String uri, String localname, String qName) throws SAXException
    {
        super.endElement(uri, localname, qName);
    }

    /**
     * {@inheritDoc}
     * @param ch
     * @param start
     * @param length 
     */
    @Override
    public void characters(char ch[], int start, int length)
    {
        String content = new String(ch, start, length).trim();
        if (readUsername)
        {
            //System.out.println("Username " + content.trim());
            properties.put("username", content);
            readUsername = false;
        } else if (readAddress)
        {
            properties.put("address", content);
            readAddress = false;
        } else if (readPassword)
        {
            properties.put("password", content);
            readPassword  = false;
        }else if(readDbName)
        {
            properties.put("dbName", content);
            readDbName  = false;
        }else if(readUseSSL)
        {
            properties.put("useSSL", content);
            readUseSSL = false;
        }
        
    }

    
    ////////////////////
    ///////////////////SET AND GET METHODS///////////////////
    //////////////////
    public boolean isReadUsername()
    {
        return readUsername;
    }

    public void setReadUsername(boolean readUsername)
    {
        this.readUsername = readUsername;
    }

    public boolean isReadPassword()
    {
        return readPassword;
    }

    public void setReadPassword(boolean readPassword)
    {
        this.readPassword = readPassword;
    }

    public boolean isReadAddress()
    {
        return readAddress;
    }

    public void setReadAddress(boolean readAddress)
    {
        this.readAddress = readAddress;
    }

    public boolean isReadDbName()
    {
        return readDbName;
    }

    public void setReadDbName(boolean readDbName)
    {
        this.readDbName = readDbName;
    }

    public boolean isReadUseSSL()
    {
        return readUseSSL;
    }

    public void setReadUseSSL(boolean readUseSSL)
    {
        this.readUseSSL = readUseSSL;
    }

    public Properties getProperties()
    {
        return properties;
    }

//    public void setProperties(Properties properties)
//    {
//        this.properties = properties;
//    }
    
    public static void main(String[] args)
    {
        DBConfigureHandler handler = new DBConfigureHandler();
        ///This is an embedded class. Testing method is not capable for this.
    }
    
}
