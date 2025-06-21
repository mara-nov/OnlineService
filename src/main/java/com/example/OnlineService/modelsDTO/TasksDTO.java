package com.example.OnlineService.modelsDTO;

import com.example.OnlineService.models.Tasks;

public class TasksDTO {

    private Long id;
    private String pdfPath;

    public TasksDTO(Tasks tasks) {
        this.id = tasks.getId();
        this.pdfPath = tasks.getPdfPath();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
}
