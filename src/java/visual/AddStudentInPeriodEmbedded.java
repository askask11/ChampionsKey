/*Editor: Johnson Gao 
 * Date This File Created: 2020-3-20 21:34:01
 * Description Of This Class: This class is never used. This is created just for fun. Please ignore this page when you are gradingmy project!
 */
package visual;

import connector.database.DatabaseMain;
import connector.database.StudyhallManagement;
import java.io.IOException;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *This class is never used. This is created just for fun. Please ignore this page when you are gradingmy project!
 * @author Jianqing Gao
 */
public class AddStudentInPeriodEmbedded extends javax.swing.JApplet
{

    private static final long serialVersionUID = 1L;

    /**
     * Initializes the applet AddStudentInPeriodEmbedded
     */
    @Override
    public void init()
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(AddStudentInPeriodEmbedded.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(AddStudentInPeriodEmbedded.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(AddStudentInPeriodEmbedded.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(AddStudentInPeriodEmbedded.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the applet */
        try
        {
            java.awt.EventQueue.invokeAndWait(() ->
            {
                initComponents();
                idField.setText(getParameter("studentID"));
                setVisible(true);
            });
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * This method is called from within the init() method to initialize the
     * form. WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        idField = new javax.swing.JFormattedTextField();
        confirmButton = new javax.swing.JButton();
        messageField = new javax.swing.JTextField();
        permanentCheckBox = new javax.swing.JCheckBox();

        jLabel1.setText("Add Student In Period");

        idField.setBorder(javax.swing.BorderFactory.createTitledBorder("Student ID"));
        idField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        confirmButton.setBackground(new java.awt.Color(51, 255, 51));
        confirmButton.setText("OK");
        confirmButton.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                confirmButtonActionPerformed(evt);
            }
        });

        messageField.setEditable(false);
        messageField.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                messageFieldActionPerformed(evt);
            }
        });

        permanentCheckBox.setText("Permanent");
        permanentCheckBox.setToolTipText("Check this if you are confirmed to enroll in this period.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(idField))
                .addGap(34, 34, 34))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(messageField)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(confirmButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(permanentCheckBox)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(confirmButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addComponent(permanentCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(messageField, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_confirmButtonActionPerformed
    {//GEN-HEADEREND:event_confirmButtonActionPerformed
        try
        {
            // TODO add your handling code here:
            DatabaseMain main = DatabaseMain.getDefaultInstance();
            StudyhallManagement studyhallManagement = main.manageStudyHall();
            String periodId = getParameter("periodID");
            String attandanceId = getParameter("attandanceID");
            ArrayList<Integer> data = new ArrayList<>(1);
            String studentId = idField.getText();
            
            int rows;
            data.add(Integer.parseInt(studentId));
            studyhallManagement.insertBatchIntoAttandanceHistoryDefault(Integer.parseInt(attandanceId), data, ZoneId.of("UTC-5"));
            messageField.setText("success!");
            //permanent register
            if (permanentCheckBox.isSelected())
            {
                rows = studyhallManagement.insertIntoStudentInPeriod(Integer.parseInt(studentId), Integer.parseInt(periodId));
                if (rows == 1)
                {
                    messageField.setText("Success!");
                } else
                {
                    messageField.setText("Maybe success");
                }
            } 

        } catch (ClassNotFoundException | SQLException | SAXException | IOException | ParserConfigurationException ex)
        {
            Logger.getLogger(AddStudentInPeriodEmbedded.class.getName()).log(Level.SEVERE, null, ex);
            messageField.setText("Failed!" + ex.toString());
        }

    }//GEN-LAST:event_confirmButtonActionPerformed

    private void messageFieldActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_messageFieldActionPerformed
    {//GEN-HEADEREND:event_messageFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_messageFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton confirmButton;
    private javax.swing.JFormattedTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField messageField;
    private javax.swing.JCheckBox permanentCheckBox;
    // End of variables declaration//GEN-END:variables
}
