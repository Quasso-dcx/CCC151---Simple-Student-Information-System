import javax.swing.SwingUtilities;

import view.SSIS_Display;

public class Main{
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(SSIS_Display :: new);
        } catch (Exception e){
            System.out.println(e.toString());
        }
    }
}