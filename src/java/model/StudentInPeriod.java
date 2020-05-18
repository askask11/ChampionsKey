/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-19 23:31:16
 * Description Of This Class:This registers a student that enrolled in a period.
 */
package model;

/**
 * This registers a student that enrolled in a period.
 *
 * @author Jianqing Gao
 */
public class StudentInPeriod extends Student
{

    private int periodId;

    /**
     * Creates a default object of student in periodn given informations.
     *
     * @param periodId
     * @param id
     * @param name
     * @param grade
     */
    public StudentInPeriod(int periodId, int id, String name, int grade)
    {
        super(id, name, grade);
        this.periodId = periodId;
    }

    /**
     * Creates a default instande of student in period.
     */
    public StudentInPeriod()
    {
        super();
        this.periodId = -1;
    }

    /**
     * Get the unique id for the current period.
     *
     * @return
     */
    public int getPeriodId()
    {
        return periodId;
    }

    /**
     * Set the id for this period.
     *
     * @param periodId
     */
    public void setPeriodId(int periodId)
    {
        this.periodId = periodId;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public String toString()
    {
        return super.toString() + " {periodId =" + periodId + "}";
    }
    
    public static void main(String[] args)
    {
        System.out.println(new StudentInPeriod());
    }

}
