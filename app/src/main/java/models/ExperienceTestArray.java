package models;

import java.util.ArrayList;

/**
 * Created by doc_dungeon on 8/20/16.
 */
public class ExperienceTestArray {
    //define user
    public String name;
    public String hometown;

    //create a constructor
    public ExperienceTestArray(String name, String hometown){
        this.name = name;
        this.hometown = hometown;
    }

    //returns list of users
    public static ArrayList<ExperienceTestArray> getUserList(){
        ArrayList<ExperienceTestArray> users = new ArrayList<>();
        users.add(new ExperienceTestArray("Chef Steven","New York"));
        users.add(new ExperienceTestArray("Dancer Jane","Oakland"));
        users.add(new ExperienceTestArray("Singer Susan","LA"));
        users.add(new ExperienceTestArray("Painter John","Paris"));
        users.add(new ExperienceTestArray("Astrologer Rhaian","Stockholm"));
        users.add(new ExperienceTestArray("Trainer Jamal","Boston"));
        users.add(new ExperienceTestArray("Photographer Tim","Chicago"));
        users.add(new ExperienceTestArray("Writer Arianna","Miami"));
        users.add(new ExperienceTestArray("Actress Demi","London"));

        return users;

    }
}
