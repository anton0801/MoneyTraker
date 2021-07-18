package priler.com.models;

public class ItemBody {
    int userId;
    String name;
    String price;
    String comment;
    String type;

    public ItemBody(int userId, String name, String price, String comment, String type) {
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.comment = comment;
        this.type = type;
    }
}
