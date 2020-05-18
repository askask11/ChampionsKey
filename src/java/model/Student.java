/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-18 14:47:52
 * Description Of This Class: This is an abstraction class of a student.
 */
package model;

/**
 * An abstraction class for students.
 *
 * @author jianqing gao
 */
public class Student
{

    private int id, grade;
    private String name;

    /**
     * Creates a instance of a student given all informatioon.
     *
     * @param id
     * @param name
     * @param grade
     */
    public Student(int id, String name, int grade)
    {
        this.id = id;
        this.grade = grade;
        this.name = name;
    }

    /**
     * Creates a default instance of student.
     */
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

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public String toString()
    {
        return "Student{" + "id=" + id + ", grade=" + grade + ", name=" + name + '}';
    }

    public static void main(String[] args)
    {
        System.out.println(new Student());
    }
}
