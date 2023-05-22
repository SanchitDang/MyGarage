package com.sanapplications.mygarage.Model;

import com.google.gson.annotations.SerializedName;

public class MakeResult {
    //        private int Make_ID;
    public String Make_Name;
//
//        // Getters and setters

    @SerializedName("Make_ID")
    public int makeId;
    @SerializedName("Make_Name")
    //private String makeName;

    public int getMakeId() {
        return makeId;
    }

    public String getMakeName() {
        //return makeName;
        return Make_Name;
    }
}