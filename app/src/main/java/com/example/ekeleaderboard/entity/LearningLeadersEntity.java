package com.example.ekeleaderboard.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "learners_table")
public class LearningLeadersEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "learn_Id")
    private long id;

    @ColumnInfo(name = "learn_name")
    private String name;

    @ColumnInfo(name = "learn_hours")
    private Integer hours;

    @ColumnInfo(name = "learn_country")
    private String country;

    @ColumnInfo(name = "learn_badgeUrl")
    private String badgeUrl;

    @Ignore
    public LearningLeadersEntity(String name, Integer hours, String country, String badgeUrl) {
        this.name = name;
        this.hours = hours;
        this.country = country;
        this.badgeUrl = badgeUrl;
    }

    public LearningLeadersEntity(long id, String name, Integer hours, String country, String badgeUrl) {
        this.id = id;
        this.name = name;
        this.hours = hours;
        this.country = country;
        this.badgeUrl = badgeUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }
}
