/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-18 14:47:52
 * Description Of This Class:
 */
package model;

/**
 * An abstraction class for students.
 * @author jianqing gao
 */
public class Student
{
    private int id,grade;
    private String name;

    public Student(int id, String name, int grade)
    {
        this.id = id;
        this.grade = grade;
        this.name = name;
    }

    public Student()
    {
        id = -1;
        grade = -1;
        name = "";
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getGrade()
    {
        return grade;
    }

    public void setGrade(int grade)
    {
        this.grade = grade;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "Student{" + "id=" + id + ", grade=" + grade + ", name=" + name + '}';
    }
    
    
    
    
}
