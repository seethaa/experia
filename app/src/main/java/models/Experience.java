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
    public String duration;
    public String tags;


    public Experience(){
        // Default constructor required for calls to DataSnapshot.getValue(Experience.class)

    }

    public Experience(String uid, String title, String author, String description, String numGuests, String duration, String tags) {
        this.uid = uid;
        this.title = title;
        this.author = author;
        this.description = description;
        this.numGuests = numGuests;
        this.duration = duration;
        this.tags = tags;
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
        result.put("duration", duration);
        result.put("tags", tags);
        return result;
    }
}
