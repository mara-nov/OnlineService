package com.example.OnlineService.modelsDTO;

import com.example.OnlineService.models.Work;

import java.util.List;

public class WorkDTO {

    private Long id;
    private String name;
    private String pdfPath;
    private List<CheckResultDTO> checkResults;

    WorkDTO (Work work) {
        this.id = work.getId();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public List<CheckResultDTO> getCheckResults() {
        return checkResults;
    }

    public void setCheckResults(List<CheckResultDTO> checkResults) {
        this.checkResults = checkResults;
    }
}
