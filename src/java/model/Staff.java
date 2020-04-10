/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-11 12:23:08
 * Description Of This Class:
 */
package model;

/**
 * This is an abstruction for staffs. It does not inculude the status of the account.
 * @author Jianqing Gao
 */
public class Staff
{
    private String email,username,password,legalName;
    private int id;

    public Staff(String email, String username, String password, String legalName, int id)
    {
        this.email = email;
        this.username = username;
        this.password = password;
        this.legalName = legalName;
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getLegalName()
    {
        return legalName;
    }

    public void setLegalName(String legalName)
    {
        this.legalName = legalName;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "Staff{" + "email=" + email + ", username=" + username + ", password=" + password + ", legalName=" + legalName + ", id=" + id + '}';
    }
    
    
    
    
}
