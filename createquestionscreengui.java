package quizapplication.src;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import quizapplication.src.constants.commonConstants;
import quizapplication.src.database.JDBC;
import quizapplication.src.database.answer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class createquestionscreengui extends JFrame{
   private JTextArea questiontextArea;  
   private JTextField categorytextfield;
   private JTextField[] answerTextFields;
   private ButtonGroup buttonGroup;
   private JRadioButton[] answerRadioButtons;


 public createquestionscreengui(){
    super("Create a Question");
    setSize(851, 565);
    setLayout(null);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    getContentPane().setBackground(commonConstants.LIGHT_BLUE);


    answerRadioButtons = new JRadioButton[4];
    answerTextFields = new JTextField[4];
    buttonGroup = new ButtonGroup();


    addGuicomponents();
 }
 private void  addGuicomponents(){
    //title label 
        JLabel titleLabel = new JLabel("Create your own Question");
        titleLabel.setFont(new Font("Arial",Font.BOLD,24));
        titleLabel.setBounds(50,15,310,29);
        titleLabel.setForeground(commonConstants.BRIGHT_YELLOW);
        add(titleLabel); 

        //question label
        JLabel questionLabel = new JLabel("Question: ");
        questionLabel.setFont(new Font("Arial",Font.BOLD,16));
        questionLabel.setBounds(50,60,93,20);
        questionLabel.setForeground(commonConstants.BRIGHT_YELLOW);
        add(questionLabel); 

        //question text area
        questiontextArea = new JTextArea();
        questiontextArea.setFont(new Font("Arial",Font.BOLD,16));
        questiontextArea.setBounds(50,90,310,110);
        questiontextArea.setForeground(commonConstants.DARK_BLUE);
      questiontextArea.setLineWrap(true);
      questiontextArea.setWrapStyleWord(true);

        add(questiontextArea);
        
        //category label 
        JLabel categoryLabel = new JLabel("Category: ");
        categoryLabel.setFont(new Font("Arial",Font.BOLD,16));
        categoryLabel.setBounds(50,250,93,20);
        categoryLabel.setForeground(commonConstants.BRIGHT_YELLOW);
        add(categoryLabel); 

      //category text input field 
      categorytextfield = new JTextField();
      categorytextfield.setFont(new Font("Arial",Font.BOLD,16));
      categorytextfield.setBounds(50,280,310,36);
      categorytextfield.setForeground(commonConstants.DARK_BLUE);
       
      add(categorytextfield); 
         

      addAnswercomponents();


      //submit button 
      JButton submitButton = new JButton("Submit");
      submitButton.setFont(new Font("Arial",Font.BOLD,16));
      submitButton.setBounds(300,450,262,45);
      submitButton.setForeground(commonConstants.DARK_BLUE);
      submitButton.setBackground(commonConstants.BRIGHT_YELLOW);

      submitButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          if(validateInput()){
            String question = questiontextArea.getText();
            String category = categorytextfield.getText();
            String[] answers = new String[answerTextFields.length];
            int correctindex = 0;
            for (int i = 0; i < answerTextFields.length; i++) {
              answers[i] = answerTextFields[i].getText();
              if(answerRadioButtons[i].isSelected()){
                correctindex = i;
              }
            }
           //update database
           if(JDBC.savequestioncategoryandanswerstodb(question, category, answers, correctindex)){
            //update successfully 
            JOptionPane.showMessageDialog(createquestionscreengui.this, "Successfully Added Question!!");

            //reset fields
            resetFields();
           }else{
            //update failed 
            JOptionPane.showMessageDialog(createquestionscreengui.this, "Failed to Add Question...");
           }

          }else{
            //invalid input 
            JOptionPane.showMessageDialog(createquestionscreengui.this, "Error: Invalid Input");
          }
          
        }
        
      });
       
      add(submitButton); 
      // go back label 
      JLabel gobackLabel = new JLabel("Go Back");
      gobackLabel.setFont(new Font("Arial",Font.BOLD,16));
      gobackLabel.setBounds(300,500,262,20);
      gobackLabel.setForeground(commonConstants.BRIGHT_YELLOW);
    
      gobackLabel.setHorizontalAlignment(SwingConstants.CENTER);
   
       gobackLabel.addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
          //display title screen
          titlescreengui titlescreengui = new titlescreengui();
          titlescreengui.setLocationRelativeTo(createquestionscreengui.this);

          //dispose of this screen
          createquestionscreengui.this.dispose();
          // make this title screen visible
          titlescreengui.setVisible(true);
        }
        
       });
      add(gobackLabel); 
 }
 private void addAnswercomponents(){
    //vertical spacing between each answer component 
    int verticalspacing = 100;

    //create 4 answer label ,4 radio buttons , 4 text input fields
    for (int i = 0; i < 4; i++) {
        //answer label 
        JLabel answerLabel = new JLabel("Answer #" + (i + 1));
        answerLabel.setFont(new Font("Arial",Font.BOLD,16));
        
        answerLabel.setBounds(470, 60+(i*verticalspacing), 93, 20);
        answerLabel.setForeground(commonConstants.BRIGHT_YELLOW);
        add(answerLabel);

        // radio buttons 
        answerRadioButtons[i]= new JRadioButton();
        answerRadioButtons[i].setBounds(440, 100+(i*verticalspacing), 21, 21);
        answerRadioButtons[i].setBackground(null);
        buttonGroup.add(answerRadioButtons[i]);
        add(answerRadioButtons[i]);

        //answer text input field
        answerTextFields[i]= new JTextField(); 
        answerTextFields[i].setBounds(470, 90+(i*verticalspacing), 310, 36);
      answerTextFields[i].setFont(new Font("Arial",Font.PLAIN,16));
      answerTextFields[i].setForeground(commonConstants.DARK_BLUE);
        add(answerTextFields[i]);



        
    }
    //give default value to  the radio buttons 
    answerRadioButtons[0].setSelected(true);
 }

 //true - valid input
 //false - invalid input
 private boolean validateInput(){
     // make sure that question field is not empty 
     if(questiontextArea.getText().replaceAll(" ", "").length()<=0)return false;

     // make sure category field is not empty 
     if(categorytextfield.getText().replaceAll(" ", "").length()<=0) return false;
     // make sure all answer field are not empty 
   for (int i = 0; i < answerTextFields.length; i++) {
    if(answerTextFields[i].getText().replaceAll(" ", "").length()<=0) return false;
    
   }
   return true;  
}

private void resetFields(){
  questiontextArea.setText("");
  categorytextfield.setText("");
  for (int i = 0; i < answerTextFields.length; i++) {
    answerTextFields[i].setText("");
  }
}

}

