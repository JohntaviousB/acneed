package com.abusement.park.acneed.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Suggestion {

    private String user;
    private String title;
    private String details;
    private Date creationDate;
    private int downVotes;
    private int upVotes;
    private Map<String, Boolean> upVoters; // these maps are workarounds to firebase not serializing Sets
    private Map<String, Boolean> downVoters;
    private String id;

    public Suggestion() {
        downVotes = upVotes = 0; // Java initializes these to 0 by default, but this is for clarity
        creationDate = new Date();
        upVoters = new HashMap<>();
        downVoters = new HashMap<>();
    }

    public Suggestion(String user, String title, String details) {
        downVotes = upVotes = 0;
        this.user = user;
        this.title = title;
        this.details = details;
        creationDate = new Date();
        upVoters = new HashMap<>();
        downVoters = new HashMap<>();
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void incrementUpVotes() {
        upVotes++;
    }

    public void decrementUpVotes() {
        upVotes--;
    }

    public void incrementDownVotes() {
        downVotes++;
    }

    public void decrementDownVotes() {
        downVotes--;
    }

    public int calculateUpVotePercent() {
        return (int) ( (1.0 * upVotes / (upVotes + downVotes)) * 100);
    }

    public int totalVotes() {
        return upVotes + downVotes;
    }

    public Map<String,Boolean> getUpVoters() {
        return upVoters;
    }

    public void setUpVoters(Map<String, Boolean> upVoters) {
        this.upVoters = upVoters;
    }

    public Map<String, Boolean> getDownVoters() {
        return downVoters;
    }

    public void setDownVoters(Map<String, Boolean> downVoters) {
        this.downVoters = downVoters;
    }

    public boolean addUpVoter(String user) {
        if (upVoters == null) {
            upVoters = new HashMap<>();
        }
        Boolean result = upVoters.put(user, true);
        if (downVoters != null) {
            removeDownVoter(user);
        }
        if (result == null) {
            // b/c result is the PREVIOUS mapping, null indicates that we should increment
            incrementUpVotes();
        }
        return result == null;
    }

    public boolean addDownVoter(String user) {
        if (downVoters == null) {
            downVoters = new HashMap<>();
        }
        Boolean result = downVoters.put(user, true);
        if (upVoters != null) {
            removeUpVoter(user);
        }
        if (result == null) {
            incrementDownVotes();
        }
        return result == null;
    }

    public boolean removeUpVoter(String user) {
        Boolean result = upVoters.remove(user);
        if (result != null) {
            decrementUpVotes();
        }
        return result != null;
    }

    public boolean removeDownVoter(String user) {
        Boolean result = downVoters.remove(user);
        if (result != null) {
            decrementDownVotes();
        }
        return result != null;
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
        return Objects.equals(user, that.user)
                && Objects.equals(title, that.title)
                && Objects.equals(creationDate, that.creationDate);

    }

    @Override
    public int hashCode() {
        return Objects.hash(user, title, creationDate);
    }
}
