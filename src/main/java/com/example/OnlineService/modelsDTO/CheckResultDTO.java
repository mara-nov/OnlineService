package com.example.OnlineService.modelsDTO;

import java.util.List;

public class CheckResultDTO {

    private Long id;
    private List<Integer> scores;
    private Long workId;
    private String checkerName;

    public CheckResultDTO (CheckResultDTO checkResult) {
        this.id = checkResult.getId();
        this.scores = checkResult.getScores();
        this.workId = checkResult.getWorkId();
        this.checkerName = checkResult.getCheckerName();
    }

    public CheckResultDTO () {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Integer> getScores() {
        return scores;
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }
}
