/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-16 19:29:40
 * Description Of This Class: This is the history of an attendance record.
 */
package model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * An attendance record.
 *
 * @author Jianqing Gao
 */
public class AttandanceHistory extends Object
{

    public static final int LOCATION_UNINITALIZED = -1,
            LOCATION_UNKNOWN = 0,
            LOCATION_CHAMPION_HALL = 1,
            LOCATION_LEARNING_COMMON = 2,
            LOCATION_SCHOOL_STORE = 3;
    public static final int ATTANDANCE_UNINITALIZED = -1,
            ATTANDANCE_ABSENT = 0,
            ATTANDANCE_PRESENT = 1,
            ATTANDANCE_TARDY = 2;
    private int studentID, attandanceID;
    private LocalTime lastUpdateTime;
    private int location, attandanceCode;
    private String description;

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public AttandanceHistory()
    {
        super();
        studentID = -1;
        attandanceID = -1;
        lastUpdateTime = null;
        location = LOCATION_UNINITALIZED;
        attandanceCode = ATTANDANCE_UNINITALIZED;
        description = "";
    }

    public AttandanceHistory(int studentID, int attandanceID, LocalTime lastUpdateTime, int location, int attandance, String description)
    {
        this.studentID = studentID;
        this.attandanceID = attandanceID;
        this.lastUpdateTime = lastUpdateTime;
        this.location = location;
        this.attandanceCode = attandance;
        this.description = description;
    }

    public int getStudentID()
    {
        return studentID;
    }

    public void setStudentID(int studentID)
    {
        this.studentID = studentID;
    }

    public int getAttandanceID()
    {
        return attandanceID;
    }

    public void setAttandanceID(int attandanceID)
    {
        this.attandanceID = attandanceID;
    }

    public LocalTime getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public String getLastUpdateTimeAsString()
    {
        return TIME_FORMATTER.format(lastUpdateTime);
    }

    public void setLastUpdateTime(LocalTime lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getLocation()
    {
        return location;
    }

    public void setLocation(int location)
    {
        this.location = location;
    }

//    public String getLocationAsString()
//    {
//        switch(loca)
//    }
    public int getAttandanceCode()
    {
        return attandanceCode;
    }

    public void setAttandanceCode(int attandanceCode)
    {
        this.attandanceCode = attandanceCode;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public String toString()
    {
        return "StudentAttandanceRecord{" + "studentID=" + studentID + ", attandanceID=" + attandanceID + ", lastUpdateTime=" + lastUpdateTime + ", location=" + location + ", attandance=" + attandanceCode + ", description=" + description + '}';
    }
    
    public static void main(String[] args)
    {
        System.out.println(new AttandanceHistory());
    }

}
