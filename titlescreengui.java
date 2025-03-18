package quizapplication.src;

import javax.swing.*;

import quizapplication.src.constants.commonConstants;
import quizapplication.src.database.JDBC;
import quizapplication.src.database.category;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class titlescreengui extends JFrame {

    private JComboBox categoriesMenu;
    private JTextField numofquestionstTextField;

    public titlescreengui(){
        //call the constructor of the superclass with the title of "title screen"
        super("title Screen ");
        //set the size  of jframe to 400 pixel wide and 565 pixel tall 
        setSize(400, 565);
        //set the layout manager of the frame to null allowing manual positioning of the component 
            setLayout(null);
            //set the frame to centered of screen 
            setLocationRelativeTo(null);
            //disable resizing of frame by user 
            setResizable(false);
            //set the default close operation of the frame to exit 
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            //  change the background color 
            getContentPane().setBackground(commonConstants.LIGHT_BLUE);

            addGuicomponents();
    }
    private void addGuicomponents(){
     JLabel titleLabel = new JLabel("Quiz Game!");
     titleLabel.setFont(new Font("Arial",Font.BOLD,36));
     titleLabel.setBounds(0,20,390,43);
     titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
     titleLabel.setForeground(commonConstants.BRIGHT_YELLOW);
     add(titleLabel);

     //choose category label 
     JLabel chooseCategorylLabel = new JLabel("Choose a Category ");
     chooseCategorylLabel.setFont(new Font("Arial",Font.BOLD,16));
     chooseCategorylLabel.setBounds(0,80,400,43);
     chooseCategorylLabel.setHorizontalAlignment(SwingConstants.CENTER);
     chooseCategorylLabel.setForeground(commonConstants.BRIGHT_YELLOW);
     add(chooseCategorylLabel);

     //category drop down menu 
   ArrayList<String> categoryList = JDBC.getcategories();
     categoriesMenu = new JComboBox(categoryList.toArray());
     categoriesMenu.setBounds(20, 120, 337, 45);
     categoriesMenu.setForeground(commonConstants.DARK_BLUE);
     add(categoriesMenu);

     //num of question label 
     JLabel numofquestionslLabel = new JLabel("Number of Questions: ");
     numofquestionslLabel.setFont(new Font("Arial",Font.BOLD,16));
     numofquestionslLabel.setBounds(20,190,172,20);
     numofquestionslLabel.setHorizontalAlignment(SwingConstants.CENTER);
     numofquestionslLabel.setForeground(commonConstants.BRIGHT_YELLOW);
     add(numofquestionslLabel);

     //num of questions text input field 
     numofquestionstTextField = new JTextField("10");
     numofquestionstTextField.setFont(new Font("Arial",Font.BOLD,16));
     numofquestionstTextField.setBounds(200,190,148,26);
     numofquestionstTextField.setHorizontalAlignment(SwingConstants.CENTER);
     numofquestionstTextField.setForeground(commonConstants.DARK_BLUE);
     add(numofquestionstTextField);


     //start button 
     JButton startButton = new JButton("Start");
     startButton.setFont(new Font("Arial",Font.BOLD,16));
      startButton.setBounds(65, 290, 262, 45);
      startButton.setBackground(commonConstants.BRIGHT_YELLOW);
      startButton.setForeground(commonConstants.LIGHT_BLUE);
      startButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          if(validateInput()){
            //reterieve category 
            category category = JDBC.getCategory(categoriesMenu.getSelectedItem().toString());
          //invalid category  
            if(category == null) return ;

            int numberofquestions = Integer.parseInt(numofquestionstTextField.getText());

            //load quiz screen
            qiuzscreengui qiuzscreengui = new qiuzscreengui(category, numberofquestions);
            qiuzscreengui.setLocationRelativeTo(titlescreengui.this);

            //dispose of this screen
            titlescreengui.this.dispose();

            //display quiz screen
            qiuzscreengui.setVisible(true);
          }
          
        }
        
      });
      add(startButton); 

      //exit button 
      JButton exitButton = new JButton("Exit");
      exitButton.setFont(new Font("Arial",Font.BOLD,16));
       exitButton.setBounds(65, 350, 262, 45);
      exitButton.setBackground(commonConstants.BRIGHT_YELLOW);
      exitButton.setForeground(commonConstants.LIGHT_BLUE);
      exitButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          //dispose of this screen 
          titlescreengui.this.dispose();
        }
        
      });
      add(exitButton); 


    //create a question button 
     JButton createaquestionButton = new JButton("Create a Question");
     createaquestionButton.setFont(new Font("Arial",Font.BOLD,16));
       createaquestionButton.setBounds(65, 420, 262, 45);
      createaquestionButton.setBackground(commonConstants.BRIGHT_YELLOW);
      createaquestionButton.setForeground(commonConstants.LIGHT_BLUE);
      createaquestionButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          //create question screen gui 
          createquestionscreengui createquestionscreengui = new createquestionscreengui();
          createquestionscreengui.setLocationRelativeTo(titlescreengui.this);

          //dispose of this title screen
          titlescreengui.this.dispose();

          //display create a question screen gui 
          createquestionscreengui.setVisible(true);
        }
        
      });
      add(createaquestionButton); 

    }

    //true - valid input 
    // false - invalid input 
    private boolean validateInput(){
      //num of question field must not be empty 
      if(numofquestionstTextField.getText().replaceAll(" ", "").length() <= 0) return false;

      // no category is choosen 
      if(categoriesMenu.getSelectedItem() == null) return false;
      return true;
    }
}
