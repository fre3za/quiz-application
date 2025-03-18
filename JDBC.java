package quizapplication.src.database;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale.Category;

import com.mysql.cj.protocol.Resultset;

public class JDBC {
    //my sql configuration 
    private static final String DB_URL ="jdbc:mysql://127.0.0.1:3306/quiz_gui_db";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD ="yash123";


/*
 question - the question to be inserted 
 category - category of the question to be inserted if not already in the database 
 answer - answer to be inserted 
 correctindex - determine which of the answer is correct answer  
 */
    public static boolean savequestioncategoryandanswerstodb(String question,String category,String[] answers,int correctIndex){
        try{
           // establish a database connection 
           Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME,DB_PASSWORD);

           //insert category if new , otherwise retrieve it from database
           category categoryobj = getCategory(category);
                      if(categoryobj == null){
                       // insert new category to database 
                       categoryobj = insertCategory(category);
                       
                      }

                      //insert question to database
                      question questionobj = insertQuestion(categoryobj,question);

                      // insert answer to database 
                     return insertAnswers(questionobj , answers,correctIndex); 
           
                   }catch(Exception e){
                       e.printStackTrace();
                   }
                   return false;
           
               }
               //question method 
               
                 public static ArrayList<question>getQuestions(category category){
                ArrayList<question> questions = new ArrayList<>();
                try {
                    // establish a database connection 
          Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME,DB_PASSWORD);
          

           //query that reterive all th equestion of a category in random order 
            PreparedStatement getQuestionsQuery = connection.prepareStatement("SELECT * FROM QUESTION JOIN CATEGORY " +
                            "ON QUESTION.CATEGORY_ID = CATEGORY.CATEGORY_ID " +
                            "WHERE CATEGORY.CATEGORY_NAME = ? ORDER BY RAND()");

            getQuestionsQuery.setString(1, category.getCategoryName());
            ResultSet resultSet = getQuestionsQuery.executeQuery();
            while(resultSet.next()){
                int questionId = resultSet.getInt("question_id");
                int categoryId = resultSet.getInt("category_id");
                String question = resultSet.getString("question_text");
                questions.add(new question(questionId, categoryId, question));
            }
          return questions;

                  } catch (Exception e) {
                       e.printStackTrace();
                  }
                  // return null if could not find the question in database
                  return null;
               }
               
               private static question insertQuestion(category categoryobj,String questiontext){
                               try {
                                   // establish a database connection 
                         Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME,DB_PASSWORD);
               
               
                         PreparedStatement insertQuestionQuery = connection.prepareStatement(
                          "INSERT INTO QUESTION(CATEGORY_ID,QUESTION_TEXT)" + "VALUE(?,?)",Statement.RETURN_GENERATED_KEYS
                         ) ;
               
                         insertQuestionQuery.setInt(1, categoryobj.getCategoryId());
                         insertQuestionQuery.setString(2, questiontext);
                         insertQuestionQuery.executeUpdate();
               
               
                         // check for question id 
                         ResultSet resultSet = insertQuestionQuery.getGeneratedKeys();
                         if(resultSet.next()){
                           int questionId = resultSet.getInt(1);
                           return new question(questionId, categoryobj.getCategoryId(),questiontext);
          }
                  } catch (Exception e) {
                       e.printStackTrace();
                  }
                 // return null if there is any error while inserting question to database
                  return null;
               }
               //category method       
               public static category getCategory(String category) {
                   try {
                     // establish a database connection 
           Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME,DB_PASSWORD);


           PreparedStatement getCategoryQuery = connection.prepareStatement(
            "SELECT * FROM CATEGORY WHERE CATEGORY_NAME = ?"
           ) ;

           getCategoryQuery.setString(1, category);

           // execute query and store result  
           ResultSet resultset = getCategoryQuery.executeQuery();
                    if(resultset.next()){
                        // found the category 
                        int categoryId = resultset.getInt("category_id");
                        return new category(categoryId, category);
                    }
                   } catch (Exception e) {
                        e.printStackTrace();
                   }
                   // return null if could not find the category in database
                   return null;
               }
              
               public static ArrayList<String>getcategories(){
                ArrayList<String>categoryList = new ArrayList<>();
                try {
                    // establish a database connection 
          Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME,DB_PASSWORD);


          Statement getcategoriesQuery = connection.createStatement();
          ResultSet resultSet = getcategoriesQuery.executeQuery("SELECT * FROM CATEGORY");

          while(resultSet.next()){
            String categoryName = resultSet.getString("category_name");
            categoryList.add(categoryName);
          }
          return categoryList;

                  } catch (Exception e) {
                       e.printStackTrace();
                  }
                  // return null if could not find the categories in database
                  return null;
               }

               private static category insertCategory(String category){
                try {
                    // establish a database connection 
          Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME,DB_PASSWORD);


          PreparedStatement insertCategoryQuery = connection.prepareStatement(
           "INSERT INTO CATEGORY(CATEGORY_NAME)"+ "VALUE(?)",Statement.RETURN_GENERATED_KEYS
          );

          insertCategoryQuery.setString(1, category);
          insertCategoryQuery.executeUpdate();

          // get the category id that get  automatically increment for each new insert in category table 
          ResultSet resultSet = insertCategoryQuery.getGeneratedKeys();
          if(resultSet.next()){
            int category_id = resultSet.getInt(1);
            return new category(category_id, category);
          }

          
                  } catch (Exception e) {
                       e.printStackTrace();
                  }
                  // return null if could not find the category in database
                  return null;
               }
               
               // answer method 
               
               public static ArrayList<answer>getAnswers(question question){
                ArrayList<answer> answers = new ArrayList<>();
                try {
                    // establish a database connection 
          Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME,DB_PASSWORD);
          

           //query that reterive all th answer of a question in random order 
            PreparedStatement getanswersQuery = connection.prepareStatement( "SELECT * FROM QUESTION JOIN ANSWER " +
            "ON QUESTION.QUESTION_ID = ANSWER.QUESTION_ID " +
            "WHERE QUESTION.QUESTION_ID = ? ORDER BY RAND()");
                   getanswersQuery.setInt(1, question.getQuestionId());

                   ResultSet resultSet = getanswersQuery.executeQuery();
                   while(resultSet.next()){
                    int answerId = resultSet.getInt("idanswer");
                    String answertext = resultSet.getString("answer_text");
                    boolean iscorrect = resultSet.getBoolean("is_correct");
                    answer answer = new answer(answerId, question.getQuestionId(),answertext,iscorrect);
                    answers.add(answer);
                   }

            
          return answers;

                  } catch (Exception e) {
                       e.printStackTrace();
                  }
                  // return null if could not find the answer in database
                  return null;
               }
               //true-sucessfully inserted answer
               //false-failed to inserted answer
               private static boolean insertAnswers(question question, String[]answers,int correctIndex){
                try {
                    // establish a database connection 
          Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME,DB_PASSWORD);


          PreparedStatement insertAnswerQuery = connection.prepareStatement(
           "INSERT INTO ANSWER(QUESTION_ID,ANSWER_TEXT,IS_CORRECT)"+"VALUES(?,?,?)"
          ) ;
          insertAnswerQuery.setInt(1, question.getQuestionId());
            for (int i = 0; i < answers.length; i++) {
                insertAnswerQuery.setString(2, answers[i]);

                if(i == correctIndex){
                    insertAnswerQuery.setBoolean(3, true);
                }else{
                    insertAnswerQuery.setBoolean(3, false);
                }

                insertAnswerQuery.executeUpdate();
            }
          return true;
   } catch (Exception e) {
        e.printStackTrace();
   }
       return false;
               }
}
