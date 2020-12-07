package priler.com.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Item implements Serializable {

    public static final String TYPE_UNKNOWN = "unknown";
    public static final String TYPE_INCOMES = "incomes";
    public static final String TYPE_EXPENSES = "expense";
    public static final String TYPE_BALANCE = "balance";

    public int id;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("comment")
    @Expose
    public String comment;
    @SerializedName("type")
    @Expose
    public String type;

    public Item(String price, String title, String comment, String type) {
        this.price = price;
        this.name = title;
        this.comment = comment;
        this.type = type;
    }

    public Item(String name, String price, String type) {
        this.price = price;
        this.name = name;
        this.type = type;
    }

//    protected Item(Parcel in) {
//        id = in.readInt();
//        price = in.readString();
//        name = in.readString();
//        type = in.readString();
//    }

//    public static final Creator<Item> CREATOR = new Creator<Item>() {
//        @Override
//        public Item createFromParcel(Parcel in) {
//            return new Item(in);
//        }
//
//        @Override
//        public Item[] newArray(int size) {
//            return new Item[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(id);
//        dest.writeString(price);
//        dest.writeString(name);
//        // dest.writeString(comment);
//        dest.writeString(type);
//    }

}
