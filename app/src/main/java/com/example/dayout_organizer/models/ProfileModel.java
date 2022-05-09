package com.example.dayout_organizer.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ProfileModel implements Serializable {

    public boolean success;
    public String message;
    public Data data;

    public class Data{
        public int id;
        public int user_id;
        public String created_at;
        public String updated_at;
        public String bio;
        public int followers_count;
        public int trips_count;
        public User user;
    }

    public class User{
        public int id;
        public String first_name;
        public String last_name;
        public String photo;
        public String gender;
        public String phone_number;
        public String email;
        public String confirm_at;
        public String created_at;
        public String updated_at;
    }


}
