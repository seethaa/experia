package firebase;

import com.google.firebase.database.DatabaseReference;

public class FirebaseHelper {


    private static FirebaseHelper sInstance = null;

    private DatabaseReference mDatabase;

    public String beginDate;
    public String sortBy;
    public boolean newsdeskArts;
    public boolean newsdeskFashionStyle;
    public boolean newsdeskSports;

    public static synchronized FirebaseHelper getInstance() {
        if (sInstance == null ) {
            sInstance = new FirebaseHelper();

        }
        return sInstance;
    }






}
