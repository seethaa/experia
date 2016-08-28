package models;

public class Creation {

    public Long _id; // for cupboard
    public String title;
    public String author;
    public String description;
    public String category;
    public String numGuests;
    public String date;
    public String duration;
    public String tags;
    public String imgURL;
    public String address;
    public int type;


    public Creation() {
        this.title = "nothing";
        this.author = "nothing";
        this.description = "nothing";
        this.category = "nothing";
        this.numGuests = "nothing";
        this.date = "nothing";
        this.duration = "nothing";
        this.tags = "nothing";
        this.imgURL = "nothing";
        this.address = "nothing";
        this.type = 0;
    }

    public Creation(String title, String author, String description, String category, String date,
                    String numGuests, String duration, String tags, String imgURL, String address, int type)
    {
        this.title = title;
        this.author = author;
        this.description = description;
        this.category = category;
        this.numGuests = numGuests;
        this.date = date;
        this.duration = duration;
        this.tags = tags;
        this.imgURL = imgURL;
        this.address = address;
        this.type = 0;
    }
}

