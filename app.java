package quizapplication.src;

import javax.swing.SwingUtilities;

import quizapplication.src.database.category;

public class app {
public static void main(String[] args) {
    //ensure swing gui task are executed on the event 
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run(){
        //create and display the title screen
        new titlescreengui().setVisible(true);
      //new createquestionscreengui().setVisible(true);
      //new qiuzscreengui(new category(1, "math"),10).setVisible(true);
        }
    });
}
}
