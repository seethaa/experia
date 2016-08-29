package models;

import com.google.firebase.database.IgnoreExtraProperties;

// [START blog_user_class]
@IgnoreExtraProperties
public class User {


    public String displayName;
    public String username;
    public String email;
    public String profileImgURL;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String displayName, String username, String email, String profileImgURL) {
        this.displayName = displayName;
        this.username = username;
        this.email = email;
        this.profileImgURL = profileImgURL;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getImageURL() {
        return profileImgURL;
    }
}
// [END blog_user_class]
