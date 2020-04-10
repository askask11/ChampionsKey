/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-11 8:48:56
 * Description Of This Class: This is the class for connecting and accessing database.
 */
package connector.database;

import connector.configuration.ConfigureationReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * This class is responsible for connecting to java database.
 *
 * @author Jianqing Gao
 */
public class DatabaseMain implements AutoCloseable
{

    /**
     * The connection object for up-to-date.
     */
    private Connection dbConn;

    /**
     * The name of the database connects to.
     */
    private String dbName;

    /**
     * This is the default database name.
     */
    public static final String DBNAME_CHAMPIONSKEY = "championskey";

    /**
     * The host this is connecting to.
     */
    private String dbHost = "";

    /**
     * Username and password for accessing db.
     */
    private String dbUsername, dbPassword;

    public static final String DBUSERNAME_REMOTE_PRIVATE_MAIN = "Main", DBPASSWORD_REMOTE_PRIVATE_MAIN = "45465445AA";

    /**
     * If to use SSL for this connection. By default this should be true. False
     * for this is only for debugging purposes.
     */
    private boolean dbUseSSL;

    public final static String DBHOST_REMOTE_PRIVATE = "74.208.31.251:3306";

    public final static String DBHOST_LOCALHOST = "localhost:3306";

    public final static String DBHOST_CURRENT = DBHOST_REMOTE_PRIVATE;

    public static final DateTimeFormatter DATE_FORMATTER =DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Creates a default db accessor.
     *
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPassword
     * @param useSSL
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public DatabaseMain(String dbName, String dbHost, String dbUsername, String dbPassword, boolean useSSL) throws ClassNotFoundException, SQLException
    {
        this.dbName = dbName;
        this.dbHost = dbHost;
        this.dbUsername = dbUsername;
        this.dbPassword = dbPassword;
        this.dbUseSSL = useSSL;
        setDbConn();
    }

    /**
     * This estbalished a connection, for an already existing one!.
     *
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    public void setDbConn(/*No Input variable, running by itself.*/) throws ClassNotFoundException, SQLException
    {

        //NO this.dbConn = dbConn;
        String connectionURL = "jdbc:mysql://" + dbHost + "/" + this.dbName;
        this.dbConn = null;
        //Find the driver and make connection;

        Class.forName("com.mysql.cj.jdbc.Driver"); //URL for new version jdbc connector.
        Properties properties = new Properties(); //connection system property
        properties.setProperty("user", dbUsername);
        properties.setProperty("password", dbPassword);
        properties.setProperty("useSSL", Boolean.toString(dbUseSSL));//set this true if domain suppotes SSL
//"-u root -p mysql1 -useSSL false"
        this.dbConn = DriverManager.getConnection(connectionURL, properties);

    }

    /**
     * Returns a default instance of this class, created from reading the dbaccess config file.
     * @return An instance of database accessor.
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException 
     */
    public static DatabaseMain getDefaultInstance() throws ClassNotFoundException, SQLException, SAXException, IOException, ParserConfigurationException
    {
        Properties property;// = new Properties();
        ConfigureationReader reader = new ConfigureationReader();
        property = reader.getDbConnProperties();
        return new DatabaseMain(property.getProperty("dbName"), property.getProperty("address"), property.getProperty("username"), property.getProperty("password"), Boolean.parseBoolean(property.getProperty("useSSL")));
    }

    /**
     * A method to produce a small class.
     * @return A object with this.dbconn and methods to manage staff table.
     */
    public StaffManagement manageStaff()
    {
        return new StaffManagement(dbConn);
    }

    /**
     * Most of the database managing content is in this class.
     * @return A class where contains most of the required files to manage a studyhall.
     */
    public StudyhallManagement manageStudyHall()
    {
        return new StudyhallManagement(dbConn);
    }

    /**
     * An instance of <code> StudentManagement</code> will be
     * constructed using the database connection given.
     * @return A class where most of the methods are for access students.
     */
    public StudentManagement manageStudents()
    {
        return new StudentManagement(dbConn);
    }

    public static void main(String[] args)
    {
//        try
//        {
//           // getDefaultInstance();
//        } catch (ClassNotFoundException | SQLException ex)
//        {
//            Logger.getLogger(DatabaseMain.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    
    ////Bunch of set and get methods ////// 
    
    public Connection getDbConn()
    {
        return dbConn;
    }

    public void setDbConn(Connection dbConn)
    {
        this.dbConn = dbConn;
    }

    public String getDbName()
    {
        return dbName;
    }

    public void setDbName(String dbName)
    {
        this.dbName = dbName;
    }

    public String getDbHost()
    {
        return dbHost;
    }

    public void setDbHost(String dbHost)
    {
        this.dbHost = dbHost;
    }

    public String getDbUsername()
    {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername)
    {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword()
    {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword)
    {
        this.dbPassword = dbPassword;
    }

    public boolean isDbUseSSL()
    {
        return dbUseSSL;
    }

    public void setDbUseSSL(boolean dbUseSSL)
    {
        this.dbUseSSL = dbUseSSL;
    }

    /**
     * {@inheritDoc }
     * @throws Exception 
     */
    @Override
    public void close() throws Exception
    {
        this.dbConn.close();
    }

}
