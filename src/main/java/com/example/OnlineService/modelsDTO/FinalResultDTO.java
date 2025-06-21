package com.example.OnlineService.modelsDTO;

import java.util.List;

public class FinalResultDTO {
    private String workName;
    private List<Integer> scores;

    public FinalResultDTO(String workName, List<Integer> scores) {
        this.workName = workName;
        this.scores = scores;
    }

    public String getWorkName() {
        return workName;
    }

    public List<Integer> getScores() {
        return scores;
    }
}
