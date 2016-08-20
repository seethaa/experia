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
    private String uid;
    private String title;
    private String description;
    private String numGuests;
    private String duration;


    public Experience(){
        // Default constructor required for calls to DataSnapshot.getValue(Experience.class)

    }

    public Experience(String uid, String title, String description, String numGuests, String duration) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.numGuests = numGuests;
        this.duration = duration;
    }

    //for testing
    public Experience(String s, String s1) {
        this.title = s;
        this.description = s1;
    }

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
        result.put("description", description);
        result.put("numGuests", numGuests);
        result.put("duration", duration);

        return result;
    }
}
