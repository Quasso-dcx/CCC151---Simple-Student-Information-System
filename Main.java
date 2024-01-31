import javax.swing.SwingUtilities;

import view.SSISMainDisplay;

public class Main{
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(SSISMainDisplay :: new);
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
}