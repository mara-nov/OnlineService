package com.example.OnlineService.modelsDTO;

import com.example.OnlineService.models.ResultTable;

public class ResultTableDTO {

    private Long id;
    private Long groupWorkId;

    ResultTableDTO (ResultTable resultTable) {
        this.id = resultTable.getId();
        this.groupWorkId = getGroupWorkId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupWorkId() {
        return groupWorkId;
    }

    public void setGroupWorkId(Long groupWorkId) {
        this.groupWorkId = groupWorkId;
    }
}
