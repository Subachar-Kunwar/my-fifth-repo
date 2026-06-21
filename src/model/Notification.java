package model;

import java.util.Date;

public class Notification {

    private int id;
    private int userId;
    private String message;
    private Date createdAt;
    private boolean isRead;

    // ─── Constructor ──────────────────────────────────────────
    public Notification(int id, int userId, String message,
                        Date createdAt, boolean isRead) {
        this.id        = id;
        this.userId    = userId;
        this.message   = message;
        this.createdAt = createdAt;
        this.isRead    = isRead;
    }

    // ─── Getters ──────────────────────────────────────────────
    public int getId()         { return id; }
    public int getUserId()     { return userId; }
    public String getMessage() { return message; }
    public Date getCreatedAt() { return createdAt; }
    public boolean isRead()    { return isRead; }

    // ─── Setters ──────────────────────────────────────────────
    public void setId(int id)              { this.id = id; }
    public void setUserId(int userId)      { this.userId = userId; }
    public void setMessage(String message) { this.message = message; }
    public void setCreatedAt(Date date)    { this.createdAt = date; }
    public void setRead(boolean read)      { this.isRead = read; }
}