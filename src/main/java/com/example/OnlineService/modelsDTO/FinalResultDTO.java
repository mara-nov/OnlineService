package com.example.OnlineService.modelsDTO;

import java.util.List;

public class FinalResultDTO {
    private String workName;
    private List<Integer> scores;
    private int total;

    public FinalResultDTO(String workName, List<Integer> scores) {
        this.workName = workName;
        this.scores = scores;
        this.total = 0;
        for (Integer score : scores) {
            this.total += score;
        }
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
