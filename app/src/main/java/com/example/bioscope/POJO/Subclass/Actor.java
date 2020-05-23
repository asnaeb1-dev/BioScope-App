package com.example.bioscope.POJO.Subclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Actor {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("actor")
    @Expose
    private String actor;
    @SerializedName("actor_poster")
    @Expose
    private String actorPoster;
    @SerializedName("actor_gender")
    @Expose
    private Integer actorGender;
    @SerializedName("actor_character")
    @Expose
    private String actorCharacter;
    @SerializedName("actor_id")
    @Expose
    private Integer actorId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getActorPoster() {
        return actorPoster;
    }

    public void setActorPoster(String actorPoster) {
        this.actorPoster = actorPoster;
    }

    public Integer getActorGender() {
        return actorGender;
    }

    public void setActorGender(Integer actorGender) {
        this.actorGender = actorGender;
    }

    public String getActorCharacter() {
        return actorCharacter;
    }

    public void setActorCharacter(String actorCharacter) {
        this.actorCharacter = actorCharacter;
    }

    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }
}
