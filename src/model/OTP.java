/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package model;

import java.util.Date;

public class OTP {
    private String email;
    private String otpCode;
    private Date createdAt;
    private Date expiresAt;
    private boolean isUsed;
    
    public OTP() {
        this.createdAt = new Date();
        this.isUsed = false;
    }
    
    public OTP(String email, String otpCode, int expiryMinutes) {
        this();
        this.email = email;
        this.otpCode = otpCode;
        this.expiresAt = new Date(System.currentTimeMillis() + (expiryMinutes * 60 * 1000));
    }
    
    // Getters and Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    
    public Date getExpiresAt() { return expiresAt; }
    public void setExpiresAt(Date expiresAt) { this.expiresAt = expiresAt; }
    
    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }
    
    public boolean isExpired() {
        return new Date().after(expiresAt);
    }
}