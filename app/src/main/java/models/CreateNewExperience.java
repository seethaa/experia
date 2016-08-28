package models;

import com.orm.SugarRecord;

/**
 * Created by doc_dungeon on 8/27/16.
 */
public class CreateNewExperience extends SugarRecord {
    public String experience_name;
    public String experience_description;
    public String experience_time_date;
    public String experience_street_addr;
    public String experience_city;
    public String experience_state;

    public CreateNewExperience() {
    }

    public CreateNewExperience(String experience_name, String experience_description, String experience_time_date,
                               String experience_street_addr, String experience_city, String experience_state) {
        this.experience_name = experience_name;
        this.experience_description = experience_description;
        this.experience_time_date = experience_time_date;
        this.experience_street_addr = experience_street_addr;
        this.experience_city = experience_city;
        this.experience_state = experience_state;
    }
}
