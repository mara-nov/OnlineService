package com.example.OnlineService.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;
    long identifier;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "project_id")
    private List<GroupWork> groupWorks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public List<GroupWork> getGroupWorks() {
        return groupWorks;
    }

    public void setGroupWorks(List<GroupWork> groupWorks) {
        this.groupWorks = groupWorks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addGroupWork(GroupWork groupWork) {
        if (this.groupWorks == null) {
            this.groupWorks = new ArrayList<>();
        }
        this.groupWorks.add(groupWork);
    }
}
