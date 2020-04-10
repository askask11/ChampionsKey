/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-13 13:04:00
 * Description Of This Class:
 */
package connector.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import model.Attandance;
import model.AttandanceHistory;
import model.Student;
import org.xml.sax.SAXException;

/**
 *
 * @author Jianqing Gao
 */
public class StudyhallManagement
{

    private Connection dbConn;

    public StudyhallManagement(Connection dbConn)
    {
        this.dbConn = dbConn;
    }

    public Connection getDbConn()
    {
        return dbConn;
    }

    public void setDbConn(Connection dbConn)
    {
        this.dbConn = dbConn;
    }

    ////////////////////////////////////////
    ///////////////ACTIVE SESSION TABLE/////////////////////
    ///////////////////////////////////////////
    /**
     *
     * @param teacherID
     * @return
     * @throws SQLException
     */
    public int selectFromActiveSessionById(int teacherID) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT attandanceID FROM activeSessions WHERE teacherID=?");
        ResultSet rs;
        ps.setInt(1, teacherID);
        rs = ps.executeQuery();
        //ps.setDate(1, LocalDateTime.now());
        if (rs.next())
        {
            return rs.getInt(1);
        } else
        {
            return -1;
        }
    }

    public int insertIntoActiveSession(int sessionID, int teacherID) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO activeSessions VALUES(?,?)");
        ps.setInt(1, sessionID);
        ps.setInt(2, teacherID);
        return ps.executeUpdate();
    }

    public int deleteFromActiveSession(int sessionID, int teacherID) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("DELETE FROM activeSessions WHERE attandanceID=? AND teacherID=?");
        ps.setInt(1, sessionID);
        ps.setInt(2, teacherID);
        return ps.executeUpdate();
    }

    //////////////////////////////////////////////////////////
    /////////////////////////ATTENDANCE REGISTER/////////////// 
    /////////////////////////////////////////////////////
    //db date format: yyyy-MM-dd time format:HH:mm:ss
    public Attandance selectAttandanceRegisterByAttID(int attandanceID) throws SQLException
    {
        String sql = "SELECT * FROM attandanceRegister WHERE attendanceID=?";
        PreparedStatement ps = dbConn.prepareStatement(sql);
        ResultSet rs;
        Attandance data = new Attandance();
        //String date,tardyTime;
        String[] frags;
        ps.setInt(1, attandanceID);
        rs = ps.executeQuery();
        if (rs.next())
        {
            data.setAttandanceID(attandanceID);
            data.setTeacherID(rs.getInt(2));
            data.setPeriodID(rs.getInt(3));
            frags = rs.getString(4).split("-");
            data.setDate(LocalDate.of(Integer.parseInt(frags[0]), Integer.parseInt(frags[1]), Integer.parseInt(frags[2])));
            frags = rs.getString(5).split(":");
            data.setTardyTime(LocalTime.of(Integer.parseInt(frags[0]), Integer.parseInt(frags[1]), Integer.parseInt(frags[2])));
            System.out.println(data);
            return data;
        } else
        {
            return null;
        }

    }

    public int insertIntoAttandanceRegister(Attandance attandance) throws SQLException
    {
        String sql = "INSERT INTO attandanceRegister VALUES(?,?,?,?,?)";
        PreparedStatement ps = dbConn.prepareStatement(sql);
        ps.setInt(1, attandance.getAttandanceID());
        ps.setInt(2, attandance.getTeacherID());
        ps.setInt(3, attandance.getPeriodID());
        ps.setString(4, attandance.getDate().format(DateTimeFormatter.ISO_DATE.withZone(ZoneId.of("UTC-5")).ofPattern(Attandance.DATE_FORMAT)));
        ps.setString(5, attandance.getTardyTime().format(DateTimeFormatter.ofPattern(Attandance.TIME_FORMAT)));
        return ps.executeUpdate();
    }

    public ArrayList<Attandance> selectFromAttandancesByTeacherID(int teacherID) throws SQLException
    {
        String sql = "SELECT * FROM attandanceRegister WHERE teacherID=? ORDER BY date DESC";
        PreparedStatement ps = dbConn.prepareStatement(sql);
        ResultSet rs;
        ArrayList<Attandance> data = new ArrayList<>();
        Attandance row;
        ps.setInt(1, teacherID);
        rs = ps.executeQuery();

        while (rs.next())
        {
            row = new Attandance();
            row.setAttandanceID(rs.getInt(1));
            row.setTeacherID(teacherID);
            row.setPeriodID(rs.getInt(3));
            row.setDate(LocalDate.parse(rs.getString(4), DatabaseMain.DATE_FORMATTER));
            row.setTardyTime(LocalTime.parse(rs.getString(5), DatabaseMain.TIME_FORMATTER));
            data.add(row);
        }
        return data;
    }

    ////////////////////////////////////////////////////////
    //////////////////////ATTENDANCE HISTORY///////////////////////
    ///////////////////////////////////////////////////
    /**
     * Default period flushing method.
     * @param attandanceID
     * @param studentIDs
     * @param timezone
     * @return
     * @throws SQLException 
     */
    public int[] insertBatchIntoAttandanceHistoryDefault(int attandanceID, ArrayList<Integer> studentIDs, ZoneId timezone) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO attandanceHistory VALUES(?,?,?,?,?,?)");
        for (Integer id : studentIDs)
        {
            ps.setInt(1, attandanceID);//attendance id
            ps.setInt(2, id);//student id
            ps.setString(3, DatabaseMain.TIME_FORMATTER.format(LocalTime.now(timezone)));//last update time
            ps.setInt(4, AttandanceHistory.LOCATION_UNKNOWN);// location
            ps.setInt(5, AttandanceHistory.ATTANDANCE_ABSENT);// attendance code
            ps.setNull(6, Types.VARCHAR);//description
            ps.addBatch();
        }
        return ps.executeBatch();
    }

    public int insertIntoAttendanceHistory(int attendanceID, int studentID, int attendanceCode, int locationCode, ZoneId timeZone, String description) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO attandanceHistory VALUES(?,?,?,?,?,?)");
        ps.setInt(1, attendanceID);//attendance id
        ps.setInt(2, studentID);//student id
        ps.setString(3, DatabaseMain.TIME_FORMATTER.format(LocalTime.now(timeZone)));//last update time
        ps.setInt(4, locationCode);// location
        ps.setInt(5, attendanceCode);// attendance code
        // ps.setNull(6, Types.VARCHAR);//description
        ps.setString(6, description);
        return ps.executeUpdate();
    }

    public ArrayList<AttandanceHistory> selectFromAttandanceHistoryByAttId(int attandanceID) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM attandanceHistory WHERE attendanceID=?");
        ps.setInt(1, attandanceID);
        return readAttHistoryFromResultSet(ps.executeQuery());
    }

    private ArrayList<AttandanceHistory> readAttHistoryFromResultSet(ResultSet rs) throws SQLException
    {
        ArrayList<AttandanceHistory> data = new ArrayList<>();
        AttandanceHistory row;
        while (rs.next())
        {
            row = new AttandanceHistory();
            row.setAttandanceID(rs.getInt(1));
            row.setStudentID(rs.getInt(2));
            row.setLastUpdateTime(LocalTime.parse(rs.getString(3), DatabaseMain.TIME_FORMATTER));
            row.setLocation(rs.getInt(4));
            row.setAttandanceCode(rs.getInt(5));
            row.setDescription(rs.getString(6));
            data.add(row);
        }
        return data;
    }

    public ArrayList<AttandanceHistory> selectFromAttandanceHistoryByAttCodeId(int attandanceCode, int attandanceID) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM attandanceHistory WHERE attendanceID=? AND attendanceCode=?");
        ResultSet rs;
        ps.setInt(1, attandanceID);
        ps.setInt(2, attandanceCode);
        rs = ps.executeQuery();
        return readAttHistoryFromResultSet(rs);
    }

    public ArrayList<AttandanceHistory> selectFromAttandanceHistoryByLocationAndAttId(int location, int attandanceID) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM attandanceHistory WHERE attendanceID=? AND location=?");
        ps.setInt(1, attandanceID);
        ps.setInt(2, location);
        return readAttHistoryFromResultSet(ps.executeQuery());
    }

    public boolean isStduentExistsByAttandanceID(int studentID, int attandanceID) throws SQLException
    {
        Statement s = dbConn.createStatement();
        return s.executeQuery("SELECT * FROM attandanceHistory WHERE studentID=" + studentID + " AND attendanceID=" + attandanceID).next();
    }

    public int getAttandanceCodeByStudentId(int studentID, int attandanceID) throws SQLException
    {
        Statement s = dbConn.createStatement();
        ResultSet rs = s.executeQuery("SELECT attendanceCode FROM attandanceHistory WHERE studentID=" + studentID + " AND attendanceID=" + attandanceID);
        if (rs.next())
        {
            return rs.getInt("attendanceCode");
        } else
        {
            return -1;
        }
    }

    public int getAttandanceCodeByStudentName(String name, int attandanceID) throws SQLException
    {
        StudentManagement sm = new StudentManagement(dbConn);
        ArrayList<Student> stuList = sm.selectFromStudentList();
        int studentID;
        for (int i = 0; i < stuList.size(); i++)
        {
            Student student = stuList.get(i);
            if (student.getName().equals(name))
            {
                studentID = student.getId();
                return getAttandanceCodeByStudentId(studentID, attandanceID);
            }
        }
        return -1;
    }

    //4 main update methods
    public int updateAttandanceCodeById(int studentID, int attandanceID, int attandanceCode, ZoneId timezone) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE attandanceHistory SET attendanceCode=?, lastUpdateTime=? WHERE studentID=? AND attendanceID=?");
        ps.setInt(1, attandanceCode);
        ps.setString(2, DatabaseMain.TIME_FORMATTER.format(LocalTime.now(timezone)));
        ps.setInt(3, studentID);
        ps.setInt(4, attandanceID);
        return ps.executeUpdate();
    }

    public int updateAttandanceCodeById(int studentID, int attandanceID, int attandanceCode) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE attandanceHistory SET attendanceCode=? WHERE studentID=? AND attendanceID=?");
        ps.setInt(1, attandanceCode);
        ps.setInt(2, studentID);
        ps.setInt(3, attandanceID);
        return ps.executeUpdate();
    }

    public int updateAttandanceHistoryLocationById(int studentID, int attandanceID, int location) throws SQLException
    {
        return dbConn.createStatement().executeUpdate("UPDATE attandanceHistory SET location=" + location + " WHERE studentID=" + studentID + " AND attendanceID=" + attandanceID);
    }

    public int updateAttandanceHistoryLocationById(int studentID, int attandanceID, int location, ZoneId timeZoneId) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE attandanceHistory SET location=?, lastUpdateTime=? WHERE studentID=? AND attendanceID=?");
        ps.setInt(1, location);
        ps.setString(2, DatabaseMain.TIME_FORMATTER.format(LocalTime.now(timeZoneId)));
        ps.setInt(3, studentID);
        ps.setInt(4, attandanceID);
        return ps.executeUpdate();
    }

    public int updateAttandanceHistoryDescriptionById(int studentID, int attandanceID, String description) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE attandanceHistory SET description=? WHERE studentID=? AND attendanceID=?");
        ps.setString(1, description);
        ps.setInt(2, studentID);
        ps.setInt(3, attandanceID);
        return ps.executeUpdate();
    }

    public int updateAttandanceHistoryDescriptionById(int studentID, int attandanceID, String description, ZoneId timeZoneId) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE attandanceHistory SET description=?, lastUpdateTime=? WHERE studentID=? AND attendanceID=?");
        ps.setString(1, description);
        ps.setString(2, DatabaseMain.TIME_FORMATTER.format(LocalTime.now(timeZoneId)));
        ps.setInt(3, studentID);
        ps.setInt(4, attandanceID);
        return ps.executeUpdate();
    }

//    public ArrayList<Attandance> selectA
//    public int updateAttandanceCodeByName(String name, int attandanceID ,int attandanceCode) throws SQLException
//    {
//        ArrayList<Student> stuList = new StudentManagement(dbConn).selectFromStudentList();
//        int stuID;
//        
//    }
    public int deleteStudentsFromAttendanceHistoryByStuID(int id) throws SQLException
    {
        Statement s = dbConn.createStatement();
        return s.executeUpdate("DELETE FROM attandanceHistory WHERE studentID=" + id);//it's an int, so no sql injection attack.
    }

    ////////////////////////////////////////////////////////////////////
    ///////////////////////////STUDENTS IN PERIOD////////////////////////////
    //////////////////////////////////////////////////////////////
    public ArrayList<Integer> selectFromStudentsInPeriodByPeriodId(int periodID) throws SQLException
    {
        String dbQuary = "SELECT studentID FROM studentsInPeriod WHERE periodID=?";
        PreparedStatement ps = dbConn.prepareStatement(dbQuary);
        ArrayList<Integer> list = new ArrayList<>();
        ResultSet rs;
        ps.setInt(1, periodID);
        rs = ps.executeQuery();
        while (rs.next())
        {
            list.add(rs.getInt("studentID"));
        }
        return list;
    }

    /////////////////////////////////////////////////////
    ///////////////////////////////PERIODS//////////////////////////////////////////
    /////////////////////////////////////////////////
    public String selectDescriptionFromPeriodByID(int periodID) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT description FROM periods WHERE periodID=?");
        ResultSet rs;
        ps.setInt(1, periodID);
        rs = ps.executeQuery();
        if (rs.next())
        {
            return rs.getString("description");
        } else
        {
            return null;
        }

    }

    /**
     * Select from all periods. DO NOT RENAME. USED IN JSP PAGE
     *
     * @return All info of periods.
     * @throws SQLException
     */
    public ArrayList<String[]> selectFromPeriods() throws SQLException
    {
        Statement s = dbConn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM periods");
        String[] row;// = new String[2];
        ArrayList<String[]> list = new ArrayList<>();
        while (rs.next())
        {
            row = new String[2];
            row[0] = rs.getString(1);//period id
            row[1] = rs.getString(2);//description
            list.add(row);
        }
        return list;
    }

    public ArrayList<Integer> selectStudentIdByPeriodId(int periodId) throws SQLException
    {
        //create statement
        Statement s = dbConn.createStatement();
        ResultSet rs = s.executeQuery("SELECT studentID FROM studentsInPeriod WHERE periodID=" + periodId);
        ArrayList<Integer> data = new ArrayList<>();
        //read from rs
        while (rs.next())
        {
            data.add(rs.getInt("studentID"));
        }
        return data;
    }

    public ArrayList<Student> selectStudentsByPeriodId(int periodId) throws SQLException
    {
        //orginal full list of students
        ArrayList<Student> students = new StudentManagement(dbConn).selectFromStudentList();
        //all student ids needed
        ArrayList<Integer> studentIds = selectStudentIdByPeriodId(periodId);
        //student data
        ArrayList<Student> data = new ArrayList<>();
        for (int i = 0; i < students.size(); i++)
        {
            Student student = students.get(i);
            int id = student.getId();
            //check for all unchecked ids
            for (int j = 0; j < studentIds.size(); j++)
            {
                //if the id found a match in student
                if (id == studentIds.get(j))
                {
                    //add it to the new arraylist.
                    data.add(student);
                    studentIds.remove(j);
                    j = studentIds.size() + 1;//alternative of break;
                }
            }
        }
        return data;
    }

    public ArrayList<Student> selectStudentFilterOutByPeriodId(int periodID) throws SQLException
    {
        //orginal full list of students
        ArrayList<Student> students = new StudentManagement(dbConn).selectFromStudentList();
        //all student ids needed
        ArrayList<Integer> studentIds = selectStudentIdByPeriodId(periodID);

        for (int i = 0; i < studentIds.size(); i++)
        {
            Integer get = studentIds.get(i);
            for (int j = 0; j < students.size(); j++)
            {
                Student stu = students.get(j);
                if (stu.getId() == get)
                {
                    students.remove(j);
                    j = students.size() + 1;//break the loop
                }
            }
        }
        return students;
    }

    public int deleteStudentIdFromPeriodsByPeriodId(int studentId, int periodId) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("DELETE FROM studentsInPeriod WHERE studentID=? AND periodID=?");
        ps.setInt(1, studentId);
        ps.setInt(2, periodId);
        return ps.executeUpdate();
    }

    public static void main(String[] args)
    {
        try
        {
            DatabaseMain dbMain = DatabaseMain.getDefaultInstance();
//            LocalTime time = LocalTime.now(ZoneId.of("UTC-5"));
//            System.out.println(DatabaseMain.TIME_FORMATTER.format(time));
            StudyhallManagement manager = dbMain.manageStudyHall();
//           System.out.println(manager.selectFromAttandanceHistoryByLocationAndAttId(AttandanceHistory.LOCATION_CHAMPION_HALL,23907430));

            System.out.println(manager.selectFromPeriods());
        } catch (ClassNotFoundException | SQLException | SAXException | IOException | ParserConfigurationException ex)
        {
            Logger.getLogger(StudyhallManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isThisStudentInThisPeriodById(int studentID, int periodID) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM studentsInPeriod WHERE periodID=? AND studentID=?");
        ps.setInt(1, periodID);
        ps.setInt(2, studentID);
        return ps.executeQuery().next();
    }

    public int insertIntoStudentInPeriod(int studentID, int periodID) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO studentsInPeriod VALUES(?,?)");
        ps.setInt(1, periodID);
        ps.setInt(2, studentID);
        return ps.executeUpdate();
    }

    //////////////////////////////////////////////
    /////////////CROSS TABLES////////////////////
    /////////////////////////////////////////////
    public void deleteFromAttendanceHistoryRegisterByAttId(int id) throws SQLException
    {
        Statement s = dbConn.createStatement();
        s.executeUpdate("DELETE FROM attandanceHistory WHERE attendanceID=" + id);
        s.executeUpdate("DELETE FROM attandanceRegister WHERE attendanceID=" + id);
    }

}
