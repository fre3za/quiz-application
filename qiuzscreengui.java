package quizapplication.src;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import quizapplication.src.constants.commonConstants;
import quizapplication.src.database.JDBC;
import quizapplication.src.database.answer;
import quizapplication.src.database.category;
import quizapplication.src.database.question;
import revision.nestedswitch;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
public class qiuzscreengui extends JFrame implements ActionListener {
  private JLabel scoreLabel;
  private JTextArea questiontextarea;
  private JButton[] answerbutton;
  private JButton nextButton;
    //current quiz category
    private category category;

    //question based on category 
    private ArrayList<question>questions;
    private question currentquestion;
    private int currentquestionnumber;
    private int numberofquestions;
    private int score;
    private boolean firstchoicemade;


  public qiuzscreengui(category category,int numofquestions){
    super("Quiz Game");
    setSize(400,565);
    setLayout(null);
    setLocationRelativeTo(null);
    setResizable(false);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    getContentPane().setBackground(commonConstants.LIGHT_BLUE);

    answerbutton = new JButton[4];
    this.category = category;

    //load the question based on category 
    questions =JDBC.getQuestions(category);
 
    //adjust number of question to choose the min between the user input and the total number of question in database

    this.numberofquestions = Math.min(numofquestions, questions.size());

    //load the answer for each question
    for (question question : questions) {
      ArrayList<answer>answers = JDBC.getAnswers(question);
      question.setAnswers(answers);
    }
    //load current question 
    currentquestion = questions.get(currentquestionnumber);

    addGuicomponents();
  }
private void addGuicomponents(){
  //topic label 
  JLabel topicLabel = new JLabel("Topic: "+category.getCategoryName());
  topicLabel.setFont(new Font("Arial",Font.BOLD , 16));
  topicLabel.setBounds(15,15,250,20);
  topicLabel.setForeground(commonConstants.BRIGHT_YELLOW);
  add(topicLabel);

  //score label   
    scoreLabel = new JLabel("Score: " + score + "/" + numberofquestions);
    scoreLabel.setFont(new Font("Arial",Font.BOLD , 16));
  scoreLabel.setBounds(270,15,96,20);
  scoreLabel.setForeground(commonConstants.BRIGHT_YELLOW);
  add(scoreLabel);

  //question text area 
 questiontextarea = new JTextArea(currentquestion.getQuestiontext());
 questiontextarea.setFont(new Font("Arial",Font.BOLD , 32));
  questiontextarea.setBounds(15,50,350,91);
  questiontextarea.setLineWrap(true);
  questiontextarea.setWrapStyleWord(true);
  questiontextarea.setEditable(false);
  questiontextarea.setForeground(commonConstants.BRIGHT_YELLOW);
  questiontextarea.setBackground(commonConstants.LIGHT_BLUE);
  add(questiontextarea);

  addanswercomponents();

  // return to title 
  JButton returntotitleButton = new JButton("Return to Title");
  returntotitleButton.setFont(new Font("Arial",Font.BOLD , 16));
  returntotitleButton.setBounds(60,420,262,35);
  returntotitleButton.setBackground(commonConstants.BRIGHT_YELLOW);
  returntotitleButton.setForeground(commonConstants.LIGHT_BLUE);
  returntotitleButton.addActionListener(new ActionListener() {

    @Override
    public void actionPerformed(ActionEvent e) {
      // load title screen 
      titlescreengui titlescreengui = new titlescreengui();
      titlescreengui.setLocationRelativeTo(qiuzscreengui.this);

      //dispose of this screen 
      qiuzscreengui.this.dispose();
      //display title screen 
      titlescreengui.setVisible(true);
      
    }
    
  });
  add(returntotitleButton);

  // next button 
   nextButton = new JButton("Next");
  nextButton.setFont(new Font("Arial",Font.BOLD , 16));
  nextButton.setBounds(240,470,80,35);
  nextButton.setBackground(commonConstants.BRIGHT_YELLOW);
  nextButton.setForeground(commonConstants.LIGHT_BLUE);
  nextButton.setVisible(false);
  nextButton.addActionListener(new ActionListener() {

    @Override
    public void actionPerformed(ActionEvent e) {
       nextButton.setVisible(false); 

       //reset first choice flag 
       firstchoicemade = false;

       //update current question to the next question
       currentquestion = questions.get(++currentquestionnumber);
       questiontextarea.setText(currentquestion.getQuestiontext());

       //reset and update the  answer button 
        for (int i = 0; i<currentquestion.getAnswers().size(); i++) {
          answer answer = currentquestion.getAnswers().get(i);

          // reset background color for button
           answerbutton[i].setBackground(Color.WHITE);

           // update answer text 
            answerbutton[i].setText(answer.getAnswerText());
        }
    }
    
  });
  add(nextButton);


}
private void addanswercomponents(){
 //apply a 60 px vertical spacing between each button 
 int verticalspacing =60;

 for (int i = 0; i < currentquestion.getAnswers().size(); i++) {
     answer answer = currentquestion.getAnswers().get(i);

     JButton answerButton = new JButton(answer.getAnswerText());
     answerButton.setBounds(60 ,180 + (i * verticalspacing),262,45);
     answerButton.setFont(new Font("Arial",Font.BOLD,18));
     answerButton.setHorizontalAlignment(SwingConstants.LEFT);
     answerButton.setBackground(Color.WHITE);
     answerButton.setForeground(commonConstants.DARK_BLUE);
     answerButton.addActionListener(this);
     answerbutton[i] = answerButton;
     add(answerbutton[i]);
 }
}
@Override
public void actionPerformed (ActionEvent e){
  JButton answerButton = (JButton) e.getSource();

  //find correct answer 
  answer correctAnswer = null;
  for (answer answer : currentquestion.getAnswers()) {
    if(answer.iscorrect()){
    correctAnswer = answer;
    break;
    }
    
  }
  if(answerButton.getText().equals(correctAnswer.getAnswerText())){
    // user choose the righ answer


    // change button to green color
    answerButton.setBackground(commonConstants.LIGHT_GREEN);

    //increase score only if it was the first choice
      if(!firstchoicemade){
        scoreLabel.setText("Score: " + (++score) + "/" + numberofquestions );
      }
      //check to see if it was last question 
       if(currentquestionnumber == numberofquestions -1){
        //display final result 
        JOptionPane.showMessageDialog(qiuzscreengui.this, "You're Final Score Is " + score + "/" + numberofquestions);
       }else{
        // make next button visible
                  nextButton.setVisible(true);
       }
  }else{
    //make button red to indicate wrong answer
    answerButton.setBackground(commonConstants.LIGHT_RED);
  }
  firstchoicemade = true;

}
}
