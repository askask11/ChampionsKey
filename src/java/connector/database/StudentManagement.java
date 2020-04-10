/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-18 15:01:56
 * Description Of This Class:
 */
package connector.database;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Student;

/**
 *
 * @author Jianqing Gao
 */
public class StudentManagement
{

    private Connection dbConn;

    private StudentManagement()
    {
    }

    ;
    
    public StudentManagement(Connection dbConn)
    {
        this.dbConn = dbConn;
    }

    public ArrayList<Student> selectFromStudentList() throws SQLException
    {
        String sql = "SELECT * FROM students";
        Statement s = dbConn.createStatement();
        ResultSet rs = s.executeQuery(sql);
        Student student;
        ArrayList<Student> data = new ArrayList<>();
        while (rs.next())
        {
            student = new Student(rs.getInt(1), rs.getString(2), rs.getInt(3));
            data.add(student);
        }
        return data;
    }

    public ArrayList<Student> selectFromStudentListFilterByName(String name) throws SQLException
    {
        //select the complete list
        ArrayList<Student> completeList = selectFromStudentList();
        ArrayList<Student> data = new ArrayList<>();
        //filter the list by name
        for (int i = 0; i < completeList.size(); i++)
        {
            Student student = completeList.get(i);
            //if the name matches, add it to the list
            if (student.getName().contains(name))
            {
                data.add(student);
            }
        }
        //return filtered list
        return data;
    }

//    public ArrayList<
    public int updateStudentsNameById(int id, String name) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE students SET name=? WHERE id=?");
        ps.setString(1, name);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }

    /**
     * Update the grade of students by id.
     *
     * @param id The id of the student to be updated.
     * @param grade New grade of the student
     * @return
     * @throws SQLException
     */
    public int updateStudentsGradeById(int id, int grade) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE students SET grade=? WHERE id=?");
        ps.setInt(1, grade);
        ps.setInt(2, id);
        return ps.executeUpdate();
    }

    public int updateStudentsIdById(int oldId, int newId) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("UPDATE students SET id=? WHERE id=?");
        ps.setInt(1, newId);
        ps.setInt(2, oldId);
        return ps.executeUpdate();
    }

    public int deleteFromStudentsById(int id) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("DELETE FROM students WHERE id=?");
        ps.setInt(1, id);
        return ps.executeUpdate();
    }

    /**
     * Insert a new student into the database.
     *
     * @param student
     * @return
     * @throws SQLException
     */
    public int insertIntoStudents(Student student) throws SQLException
    {
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO students VALUES(?,?,?)");
        ps.setInt(1, student.getId());
        ps.setString(2, student.getName());
        ps.setInt(3, student.getGrade());
        return ps.executeUpdate();
    }

    public boolean isStudentExistsById(int id) throws SQLException
    {
        Statement s = dbConn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM students WHERE id=" + Integer.toString(id));
        return rs.next();
    }

    public int deleteStudentFromPeriodByStuId(int id) throws SQLException
    {
        Statement s = dbConn.createStatement();
        return s.executeUpdate("DELETE FROM studentsInPeriod WHERE studentID=" + id);
    }
    public String getStudentNameById(int id) throws SQLException
    {
       ResultSet rs = dbConn.createStatement().executeQuery("SELECT name FROM students WHERE id="+id);
       if(rs.next())
       {
           return rs.getString("name");
       }else
       {
           return "";
       }
        
    }
    
    public int[] insertStudentsInBatch(File in) throws IOException, SQLException
    {
        List<String> lines = Files.readAllLines(in.toPath());
        PreparedStatement ps = dbConn.prepareStatement("INSERT INTO students VALUES(?,?,?)");
        String id, name, grade;
        int idInt,gradeInt;
        String[] frags;
        for (String line : lines)
        {
            frags = line.split(",");
            id = frags[0].trim();
            name = frags[1].trim();
            grade = frags[2].trim();
            
            idInt = Integer.parseInt(id);
            gradeInt = Integer.parseInt(grade);
            
            //check if the student exists.
            if(!isStudentExistsById(idInt) && gradeInt >= 7 && gradeInt <= 12)
            {
                ps.setInt(1, idInt);
                ps.setString(2, name);
                ps.setInt(3, gradeInt);
                ps.addBatch();
            }
        }
        return ps.executeBatch();
    }
    
    public static void main(String[] args)
    {

        try
        {
            DatabaseMain databaseMain = DatabaseMain.getDefaultInstance();
            StudentManagement sm = databaseMain.manageStudents();
            sm.deleteStudentFromPeriodByStuId(3);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
