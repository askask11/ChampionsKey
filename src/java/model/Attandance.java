/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-14 23:09:50
 * Description Of This Class:
 */
package model;

//import java.sql.Time;
import connector.database.DatabaseMain;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import util.Randomizer;

/**
 *
 * @author Jianqing Gao
 */
public class Attandance
{
//db date format: yyyy-MM-dd time format:HH:mm:ss
    public static final String DATE_FORMAT = "yyyy-MM-dd", TIME_FORMAT="hh:mm:ss";
    
    private int attandanceID;
    private int teacherID;
    private int periodID;
    private LocalDate date;// = new SimpleDateFormat("dd/MM/yyyy").parse(""); SQL DATE FORMAT: YYYY-MM-DD
    private LocalTime tardyTime;
    
    public static final int[] SUCCESS_EMOJIES=new int[]{128568,
    128570,
    128571,
    128587,
    128539,
    128540,
    128541,
    128526,
    128525,
    128523,
    128522,
    128521,
    128519,
    128515,
    128516,
    128512,
    128175,
    127775,
    129321};
    
    public static final int LATE_EMOJI = 128012,ONTIME_EMOJI=128007;
    
    public static final int[] ALREADY_SIGNIN_EMOJI={128572,128570,128539,128516,128515,128512,128513};
    
    public static final String[] SUCCESS_QUOTES = new String[]{
        "Everyone has inside them a piece of good news. The good news is you don’t know how great you can be! How much you can love! What you can accomplish! And what your potential is.",
        "The best revenge is massive success.",
        "It is time for us all to stand and cheer for the doer, the achiever – the one who recognizes the challenges and does something about it.",
        "Press on – nothing can take the place of persistence. Talent will not; nothing is more common than unsuccessful men with talent. Genius will not; unrewarded genius is almost a proverb. Education will not; the world is full of educated derelicts. Perseverance and determination alone are omnipotent.",
        "Get going. Move forward. Aim High. Plan a takeoff. Don’t just sit on the runway and hope someone will come along and push the airplane. It simply won’t happen. Change your attitude and gain some altitude. Believe me, you’ll love it up here.",
        "Character cannot be developed in ease and quiet. Only through experience of trial and suffering can the soul be strengthened, ambition inspired, and success achieved.",
        "In essence, if we want to direct our lives, we must take control of our consistent actions. It’s not what we do once in a while that shapes our lives, but what we do consistently.",
        "Fall seven times, stand up eight.",
        "Most of us, swimming against the tides of trouble the world knows nothing about, need only a bit of praise or encouragement – and we will make the goal.",
        "When you get to your wits end, you will find, God lives there.",
        "It is not the mountain we conquer but ourselves.",
        "Some luck lies in not getting what you thought you wanted but getting what you have, which once you have got it you may be smart enough to see is what you would have wanted had you known."
    };
    
    public static final String[] SUCCESS_QUOTE_FOOTERS = new String[]{
    "Anne Frank",
        "Frank Sinatra",
        "Vince Lombardi",
        "Calvin Coolidge",
        "Donald Trump",
        "Hellen Keller",
        "Tony Robbins",
        "Japanese Proverb",
        "Jerome Fleishman",
        "Unknown",
        "Edmund Hillary",
        "Garrison Keillor"};

   public Attandance()
   {
       //LocalTime.of(periodID, periodID, periodID);
       attandanceID = -1;
       teacherID = -1;
       periodID= -1;
       date  = LocalDate.now();
       tardyTime = LocalTime.now();
   }

    public int getAttandanceID()
    {
        return attandanceID;
    }

    public void setAttandanceID(int attandanceID)
    {
        this.attandanceID = attandanceID;
    }

    public int getTeacherID()
    {
        return teacherID;
    }

    public void setTeacherID(int teacherID)
    {
        this.teacherID = teacherID;
    }

    public int getPeriodID()
    {
        return periodID;
    }

    public void setPeriodID(int periodID)
    {
        this.periodID = periodID;
    }

    public LocalDate getDate()
    {
        return date;
    }

    public String getDateAsString()
    {
        return DatabaseMain.DATE_FORMATTER.format(date);
    }
    
    
    public void setDate(LocalDate date)
    {
        this.date = date;
    }

    public LocalTime getTardyTime()
    {
        return tardyTime;
    }
    
    public String getTardyTimeAsString()
    {
        return DatabaseMain.TIME_FORMATTER.format(tardyTime);
    }

    public void setTardyTime(LocalTime tardyTime)
    {
        this.tardyTime = tardyTime;
    }

    @Override
    public String toString()
    {
        return "Attandance{" + "attandanceID=" + attandanceID + ", teacherID=" + teacherID + ", periodID=" + periodID + ", date=" + date + ", tardyTime=" + tardyTime + '}';
    }
    
    
    public static void main(String[] args)
    {
        Attandance attendance = new Attandance();
        attendance.setAttandanceID(Randomizer.randomInt(111111, 999999));
        attendance.setDate(LocalDate.now(ZoneId.of("UTC-5")));
        attendance.setPeriodID(1);
        attendance.setTardyTime(LocalTime.of(13, 00, 00));
        attendance.setTeacherID(12332);
        System.out.println(attendance);
    }
   
   
}
