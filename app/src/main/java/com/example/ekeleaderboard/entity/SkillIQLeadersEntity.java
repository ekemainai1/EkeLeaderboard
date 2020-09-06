package com.example.ekeleaderboard.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "skilliq_table")
public class SkillIQLeadersEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "skilliq_Id")
    private long id;

    @ColumnInfo(name = "skilliq_name")
    private String name;

    @ColumnInfo(name = "skilliq_score")
    private Integer score;

    @ColumnInfo(name = "skilliq_country")
    private String country;

    @ColumnInfo(name = "skilliq_badgeUrl")
    private String badgeUrl;

    @Ignore
    public SkillIQLeadersEntity(String name, Integer score, String country, String badgeUrl) {
        this.name = name;
        this.score = score;
        this.country = country;
        this.badgeUrl = badgeUrl;
    }

    public SkillIQLeadersEntity(long id, String name, Integer score, String country, String badgeUrl) {
        this.id = id;
        this.name = name;
        this.score = score;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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
