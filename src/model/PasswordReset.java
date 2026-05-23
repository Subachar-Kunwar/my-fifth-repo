/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

public class PasswordReset {
    private int resetId;
    private String email;
    private String token;
    private Date requestedAt;
    private Date expiresAt;
    private boolean isCompleted;
    
    public PasswordReset() {
        this.requestedAt = new Date();
        this.isCompleted = false;
    }
    
    public PasswordReset(String email, String token, int expiryMinutes) {
        this();
        this.email = email;
        this.token = token;
        this.expiresAt = new Date(System.currentTimeMillis() + (expiryMinutes * 60 * 1000));
    }
    
    // Getters and Setters
    public int getResetId() { return resetId; }
    public void setResetId(int resetId) { this.resetId = resetId; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public Date getRequestedAt() { return requestedAt; }
    public void setRequestedAt(Date requestedAt) { this.requestedAt = requestedAt; }
    
    public Date getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Date expiresAt) { this.expiresAt = expiresAt; }
    
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
    
    public boolean isExpired() {
        return new Date().after(expiresAt);
    }
}