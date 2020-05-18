/*
Jianqing gao
This is where will execute one line of system command and get feedback.
 */
package util;

//import static CmdExec.s;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jianqing
 */
public class Commander {

    public static String executeCommand(String command) throws InterruptedException, IOException {
        //s = new Scanner(System.in);
        //System.out.print("$ ");
        //String cmd = s.nextLine();
        final Process p = Runtime.getRuntime().exec(command);
        String wholeOutput = "";
        
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            try {
                while ((line = input.readLine()) != null) {
                    wholeOutput += line;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        

        p.waitFor();
        return wholeOutput;
    }
    
    public static void main(String[] args) {
        try {
            System.out.println(executeCommand("curl -g https://email.gaogato.com/ChampionsKey_PasswordRecovery"));
        } catch (InterruptedException ex) {
            Logger.getLogger(Commander.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Commander.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
