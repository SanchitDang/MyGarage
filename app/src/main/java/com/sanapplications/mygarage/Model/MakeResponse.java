package com.sanapplications.mygarage.Model;

import com.sanapplications.mygarage.Activity.DashboardActivity;

import java.util.List;

public class MakeResponse {
    public int Count;
    public String Message;
    public List<MakeResult> Results;

    // Getters and setters
    public int count;
    public String message;
    public Object searchCriteria;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(Object searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

}
