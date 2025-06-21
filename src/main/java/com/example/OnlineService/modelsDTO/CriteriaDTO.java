package com.example.OnlineService.modelsDTO;

import com.example.OnlineService.models.Criteria;

public class CriteriaDTO {

    private Long id;
    private String pdfPath;

    CriteriaDTO (Criteria criteria) {
        this.id = criteria.getId();
        this.pdfPath = criteria.getPdfPath();
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
