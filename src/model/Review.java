package model;

import java.util.Date;

public class Review {

    private int id;
    private int productId;
    private int reviewUserId;
    private String productName;
    private String imagePath;
    private int rating;
    private String reviewText;
    private Date createdAt;
    private String username;

    public Review(int id,
                  int productId,
                  int reviewUserId,
                  String productName,
                  String imagePath,
                  int rating,
                  String reviewText,
                  Date createdAt,
                  String username) {

        this.id = id;
        this.productId = productId;
        this.reviewUserId = reviewUserId;
        this.productName = productName;
        this.imagePath = imagePath;
        this.rating = rating;
        this.reviewText = reviewText;
        this.createdAt = createdAt;
        this.username = username;
    }

    public int getId() { return id; }
    public int getProductId() { return productId; }
    public int getReviewUserId() { return reviewUserId; }
    public String getProductName() { return productName; }
    public String getImagePath() { return imagePath; }
    public int getRating() { return rating; }
    public String getReviewText() { return reviewText; }
    public Date getCreatedAt() { return createdAt; }
    public String getUsername() { return username; }
}