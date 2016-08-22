package models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Model for Experiences
 */
@IgnoreExtraProperties
public class Experience {
    public String uid;
    public String title;
    public String author;
    public String description;
    public String numGuests;
    public String date;
    public String duration;
    public String tags;
    public String imgURL;
    public String address;
    public String latitude;
    public String longitude;


    public Experience(){
        // Default constructor required for calls to DataSnapshot.getValue(Experience.class)

    }

    public Experience(String uid, String title, String author, String description, String numGuests, String date, String duration, String tags, String imgURL, String address, String latitude, String longitude) {
        this.uid = uid;
        this.title = title;
        this.author = author;
        this.description = description;
        this.numGuests = numGuests;
        this.date = date;
        this.duration = duration;
        this.tags = tags;
        this.imgURL = imgURL;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //for testing
//    public Experience(String s, String s1) {
//        this.title = s;
//        this.description = s1;
//    }
//
//    public Experience(String uid, String title, String author, String description) {
//        this.uid = uid;
//        this.title = title;
//        this.author = author;
//        this.description = description;
//    }

//    private void writeNewUser(String userId, String name, String email) {
//        Experience user = new User(name, email);
//
//        mDatabase.child("users").child(userId).setValue(user);
//    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("title", title);
        result.put("author", author);
        result.put("description", description);
        result.put("numGuests", numGuests);
        result.put("date", date);
        result.put("duration", duration);
        result.put("tags", tags);
        result.put("imgURL", imgURL);
        result.put("address", address);
        result.put("latitude", latitude);
        result.put("longitude", longitude);

        return result;
    }
}
