/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-19 23:31:16
 * Description Of This Class:
 */
package model;

/**
 *
 * @author Jianqing Gao
 */
public class StudentInPeriod extends Student
{
    private int periodId;

    public StudentInPeriod(int periodId, int id, String name, int grade)
    {
        super(id, name, grade);
        this.periodId = periodId;
    }

    public int getPeriodId()
    {
        return periodId;
    }

    public void setPeriodId(int periodId)
    {
        this.periodId = periodId;
    }

    @Override
    public String toString()
    {
        return super.toString() + " {periodId =" + periodId + "}";
    }

    
 
    
    
}
