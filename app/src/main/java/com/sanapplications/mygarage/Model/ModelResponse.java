package com.sanapplications.mygarage.Model;

import com.sanapplications.mygarage.Activity.DashboardActivity;

import java.util.List;

public class ModelResponse {
    public int Count;
    public String Message;
    public String SearchCriteria;
    public List<ModelResult> Results;

    public int getCount() {
        return Count;
    }

    public String getMessage() {
        return Message;
    }

    public String getSearchCriteria() {
        return SearchCriteria;
    }

    public List<ModelResult> getResults() {
        return Results;
    }

    public void setCount(int count) {
        this.Count = count;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.SearchCriteria = searchCriteria;
    }

    public void setResults(List<ModelResult> results) {
        this.Results = results;
    }
}
