package com.abusement.park.acneed.model;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Suggestion {

    private String user;
    private String title;
    private String details;
    private Date creationDate;
    private int downVotes;
    private int upVotes;
    private UUID uuid;

    public Suggestion() {
        downVotes = upVotes = 0; // Java initializes these to 0 by default, but this is for clarity
        uuid = UUID.randomUUID();
    }

    public Suggestion(String user, String title, String details) {
        downVotes = upVotes = 0;
        this.user = user;
        this.title = title;
        this.details = details;
        this.uuid = UUID.randomUUID();
        creationDate = new Date();
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getDownVotes() {
        return downVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public Date getCreationDate() {
        return new Date(creationDate.getTime());
    }

    public void setCreationDate(Date date) {
        this.creationDate = new Date(date.getTime());
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public int calculateUpVotePercent() {
        return (int) ( (1.0 * upVotes / (upVotes + downVotes)) * 100);
    }

    public int getTotalVotes() {
        return upVotes + downVotes;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }
        Suggestion that = (Suggestion) obj;
        return Objects.equals(uuid, that.uuid);

    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
