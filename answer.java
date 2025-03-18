package quizapplication.src.database;
//data model to represent  dat from answer tabel



public class answer {
private int answerId;
private int questionId;
private String answerText;
private boolean iscorrect;
public int getAnswerId() {
    return answerId;
}
public int getQuestionId() {
    return questionId;
}
public String getAnswerText() {
    return answerText;
}
public boolean iscorrect() {
    return iscorrect;
}
public answer(int answerId, int questionId, String answerText, boolean iscorrect) {
    this.answerId = answerId;
    this.questionId = questionId;
    this.answerText = answerText;
    this.iscorrect = iscorrect;
}


}
