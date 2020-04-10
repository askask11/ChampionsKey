/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-11 8:49:27
 * Description Of This Class: This is a database class specfically to manage staffs.
 */
package connector.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import model.Staff;
import org.xml.sax.SAXException;

/**
 *This is a database class specfically to manage staffs.
 * @author Jianqing Gao
 */
public class StaffManagement
{

    /**
     * This is the db connection object.
     */
    private Connection dbConn;

    /**
     * Never let anybody instantiate the class in this way.
     */
    private StaffManagement()
    {
    }

    
    
    public StaffManagement(Connection dbConn)
    {
        this.dbConn = dbConn;
    }

    ///insert db
    /**
     * Insert a new staff into the table.
     * @param profile The profile of the staff.
     * @param status The status of the staff account.
     * @return The rows affected.
     * @throws SQLException 
     */
    public int insertIntoStaffTable(Staff profile, int status) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO staffs VALUES(?,?,?,?,?,?)");
        ps.setInt(1, profile.getId());
        ps.setString(2, profile.getEmail());
        ps.setString(3, profile.getUsername());
        ps.setString(4, profile.getPassword());
        ps.setString(5, profile.getLegalName());
        ps.setInt(6, status);
        return ps.executeUpdate();
    }
    
    //select db
    /**
     * Select the staffs that are registered with given email.
     * @param email
     * @param password
     * @return
     * @throws SQLException 
     */
    public Staff selectStaffByEmail(String email, String password) throws SQLException
    {
         return selectCommandIDfill("SELECT * FROM staffs WHERE email=? AND password=?", email, password);
    }
    
    /**
     * Select the staffs that are registered with given username.
     * @param username
     * @param password
     * @return
     * @throws SQLException 
     */
    public Staff selectStaffByUsername(String username, String password)throws SQLException
    {
        return selectCommandIDfill("SELECT * FROM staffs WHERE username=? AND password=?", username, password);
    }
    
    /**
     * automatically fill in two parameters.
     * @param command
     * @param id
     * @param pass
     * @return
     * @throws SQLException 
     */
    private Staff selectCommandIDfill(String command, String id, String pass) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement(command);
        ResultSet rs ;
        ps.setString(1, id);
        ps.setString(2, pass);
        rs = ps.executeQuery();
        
        return readOneStaffFromResultSet(rs);
    }
    
    /**
     * Select a staff by email.
     * @param email
     * @return
     * @throws SQLException 
     */
    public Staff selectStaffByEmail(String email) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM staffs WHERE email=?");
        ResultSet rs;
        ps.setString(1, email);
        rs = ps.executeQuery();
        return readOneStaffFromResultSet(rs);
    }
    
    /**
     * Read a staff from the given result set.
     * @param rs
     * @return
     * @throws SQLException 
     */
    private Staff readOneStaffFromResultSet(ResultSet rs) throws SQLException
    {
        if(rs.next())
        {
            return new Staff(rs.getString("email"), rs.getString("username"), rs.getString("password"), rs.getString("legalname"), rs.getInt("id"));
        }else 
        {
            return null;
        }
    }
    
    //check all cridentials
    /**
     * Check if someone is already registered with the given username.
     * @param username The username of the user.
     * @return <code>true</code> if the user exists in the database. <code>false</code> otherwise.
     * @throws SQLException 
     */
    public boolean isUserExistsByUsername(String username) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM staffs WHERE username=?");
        ResultSet rs;
        ps.setString(1, username);
        rs = ps.executeQuery();
        return rs.next();
    }
    
    /**
     * Check if someone is already registered with the given email.
     * @param email The email of the user.
     * @return <code>true</code> if the user exists in the database. <code>false</code> otherwise.
     * @throws SQLException 
     */
    public boolean isUserExistsByEmail(String email) throws SQLException
    {
       PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM staffs WHERE email=?");
        ResultSet rs;
        ps.setString(1, email);
        rs = ps.executeQuery();
        return rs.next();
    }
    /**
     * Check if someone is already registered with the given id.
     * @param id The id of the user.
     * @return <code>true</code> if the user exists in the database. <code>false</code> otherwise.
     * @throws SQLException 
     */
    public boolean isUserExistsById(int id) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM staffs WHERE id=?");
        ResultSet rs;
        ps.setInt(1, id);
        rs = ps.executeQuery();
        return rs.next();
    }
    
    /**
     * Update the status of a user by id.
     * @param id
     * @param status
     * @return
     * @throws SQLException 
     */
    public int updateStaffStatusById(int id, int status) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE staffs SET status=? WHERE id=?");
        ps.setInt(1, status);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }
    
    /**
     * Update the status of the staff by id.
     * @param id
     * @param password
     * @return
     * @throws SQLException 
     */
    public int updatePasswordById(int id, String password) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE staffs SET password=? WHERE id=?");
        ps.setString(1, password);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }
    
    /**
     * Change the legal name of the user in the database.
     * @param id
     * @param name
     * @return
     * @throws SQLException 
     */
    public int updateLegalNameById(int id, String name) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE staffs SET legalname=? WHERE id=?");
        ps.setString(1, name);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }    
    
    /**
     * Update the username of the user by given id.
     * @param id
     * @param name
     * @return
     * @throws SQLException 
     */
    public int updateUserNameById(int id, String name) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE staffs SET username=? WHERE id=?");
        ps.setString(1, name);
        ps.setInt(2, id);
        return ps.executeUpdate();
    } 
    
    
    
    //////////////////////////USER BASH////////////////////////////////
    
    /**
     * Register a user into the bash table.
     * This method will automatically if user has requested a code. If ther user has
     * 
     * requested a code, then update. If the user has never requested a code, the use the innsert mathod.
     * @param id The id of the user to be checked.
     * @param bash The bashcode user for validation.
     * @return The number of rows affected.
     * @throws SQLException 
     */
    public int registerBash(int id, String bash) throws SQLException
    {
        if(isVerifyBashExistById(id))
        {
           return updateVerifyBash(id,bash);
        }else
        {
           return insertIntoVerifyBash(id, bash);
        }
    }
    
    /**
     * Check and delete the bashcode of verifiying user.
     * @param bash
     * @return
     * @throws SQLException 
     */
    public boolean confirmVerifyBash(String bash) throws SQLException
    {
        int id = selectIdByBashcodeVerify(bash);
        if(id == -1)
        {
            return false;
        }else
        {
            if(updateStaffStatusById(id,0) == 1)
            {
                deleteVerifyBashcode(bash);
                return true;
            }else
            {
                return false;
            }
        }
    }

    /**
     * Delete a verifify bashcode from the bashtable.
     * @param bash
     * @return
     * @throws SQLException 
     */
    public int deleteVerifyBashcode(String bash) throws SQLException
    {
        String sql = "DELETE FROM verifyBash WHERE bash=?";
        PreparedStatement ps = dbConn.prepareStatement(sql);
        ps.setString(1, bash);
        return ps.executeUpdate();
    }
    
    /**
     * select an id from bashtable with given bashcode.
     * @param bashcode
     * @return
     * @throws SQLException 
     */
    public int selectIdByBashcodeVerify(String bashcode) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT id FROM verifyBash WHERE bash=?");
        ResultSet rs;
        ps.setString(1, bashcode);
        rs=ps.executeQuery();
        if(rs.next())
        {
            return rs.getInt(1);
        }else
        {
            return -1;
        }
    }
    
    /**
     * Determine if the user has already registered a bash.
     * @param id
     * @return
     * @throws SQLException 
     */
    public boolean isVerifyBashExistById(int id) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM verifyBash WHERE id=?");
        ResultSet rs;
        ps.setInt(1, id);
        rs = ps.executeQuery();
        return rs.next();
    }
    
    /**
     * Insert a new user into verify bash table.
     * @param id
     * @param bash
     * @return
     * @throws SQLException 
     */
    public int insertIntoVerifyBash(int id, String bash) throws SQLException
    {
        String sql = "INSERT INTO verifyBash VALUES(?,?)";
        PreparedStatement ps = dbConn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.setString(2, bash);
        return ps.executeUpdate();
    }
    
    /**
     * Update users verify bashtable.
     * @param id
     * @param bash
     * @return
     * @throws SQLException 
     */
    public int updateVerifyBash(int id, String bash) throws SQLException
    {
        ///
        PreparedStatement ps = dbConn.prepareStatement("UPDATE verifyBash SET bash=? WHERE id=?");
        ps.setString(1, bash);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }
    
    
    ///////////////////
    ////////////////////////////////////////////////RECOVERY BASH/////////////////////////////
    /////////////////////
    //
    //run sql delete command with recovery bash table
    public int deleteFromRecoverBash(String bash) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("DELETE FROM recoveryBash WHERE bash=?");
        ps.setString(1, bash);
       return ps.executeUpdate();
    }
    
    /**
     * Insert a record into the recovery bash table.
     * @param bash
     * @param id
     * @return
     * @throws SQLException 
     */
    public int insertIntoRecoveryBash(String bash, int id) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO recoveryBash VALUES(?,?)");
        ps.setInt(1, id);
        ps.setString(2, bash);
        return ps.executeUpdate();
    }
    
    /**
     * See if an id is already registered in the bash table.
     * @param id
     * @return
     * @throws SQLException 
     */
    public boolean isIdInRecoverBash(int id) throws SQLException
    {
        Statement s = dbConn.createStatement();
        return s.executeQuery("SELECT * FROM recoveryBash WHERE id=" +id).next();
    }
    
    /**
     * Update the old bash with a new bash for the id.
     * @param id
     * @param bash
     * @return
     * @throws SQLException 
     */
    public int updateRecoverBash(int id, String bash) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE recoveryBash SET bash=? WHERE id=?");
        ps.setString(1, bash);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }
    
    /**
     * request a recovery bash.
     * @param id
     * @param bash
     * @return
     * @throws SQLException 
     */
    public int requestRecoveryBash(int id, String bash) throws SQLException
    {
        
        if(isIdInRecoverBash(id))
        {
            return updateRecoverBash(id, bash);
        }else
        {
           return insertIntoRecoveryBash(bash, id);
        }
    }
    
    /**
     * Select the staff id form the bash,
     * @param bash
     * @return
     * @throws SQLException 
     */
    public int selectIdFromRecoveryBash(String bash) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT id FROM recoveryBash WHERE bash=?");
        ResultSet rs;
        ps.setString(1, bash);
        rs = ps.executeQuery();
       if(rs.next())
       {
           return rs.getInt(1);
       }else
       {
           return -1;
       }
    }
    
    
    
    
    
    
    
    
    public static void main(String[] args)
    {
        try
        {
            DatabaseMain main = DatabaseMain.getDefaultInstance();
            StaffManagement sm = new StaffManagement(main.getDbConn());
            //System.out.println(sm.insertIntoStaffTable(new Staff("test@gmail.com", "username", "pw", "lm", 0), -1));
            System.out.println(sm.selectStaffByUsername("username", "pw"));
        } catch (ClassNotFoundException | SQLException ex)
        {
            Logger.getLogger(StaffManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex)
        {
            Logger.getLogger(StaffManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(StaffManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex)
        {
            Logger.getLogger(StaffManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static StaffManagement getDefaultInstance() throws SQLException, ClassNotFoundException, SAXException, IOException, ParserConfigurationException
    {
        return DatabaseMain.getDefaultInstance().manageStaff();
    }
    

}
