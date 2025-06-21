package com.example.OnlineService.models;

import jakarta.persistence.*;

@Entity
public class ResultTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupWorkId;

    public Long getGroupWorkId() {
        return groupWorkId;
    }

    public void setGroupWorkId(Long groupWorkId) {
        this.groupWorkId = groupWorkId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
