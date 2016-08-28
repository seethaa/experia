package models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

/**
 * Model for Experiences
 */
@IgnoreExtraProperties
@Parcel
public class Experience {
    public String uid;
    public String title;
    public String author;
    public String description;
    public String category;
    public int totalSpots;
    public String date;
    public String duration;
    public String tags;
    public String imgURL;
    public String address;
    public int spotsLeft;
    public int type;
    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    public Experience(){
        // Default constructor required for calls to DataSnapshot.getValue(Experience.class)
    }

    public Experience(String uid, String title, String author, String description, int numGuests, String date, String duration, String tags, String imgURL, String address, int type) {
        this.uid = uid;
        this.title = title;
        this.author = author;
        this.description = description;
        this.totalSpots = numGuests;
        this.date = date;
        this.duration = duration;
        this.tags = tags;
        this.imgURL = imgURL;
        this.address = address;
        this.type = type;
        this.spotsLeft = totalSpots;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("title", title);
        result.put("author", author);
        result.put("description", description);
        result.put("totalSpots", totalSpots);
        result.put("date", date);
        result.put("duration", duration);
        result.put("tags", tags);
        result.put("imgURL", imgURL);
        result.put("address", address);
        result.put("type", type);
        result.put("starCount", starCount);
        result.put("stars", stars);
        return result;
    }
}
