package quizapplication.src.database;
//data model to represent  dat from category tabel
public class category {
    private int categoryId;
    private String categoryName;


    
    public int getCategoryId() {
        return categoryId;
    }



    public String getCategoryName() {
        return categoryName;
    }



    public category(int categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    

}
