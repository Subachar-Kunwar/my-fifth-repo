package model;

import java.util.Date;

public class Review {

    private int id;
    private int productId;
    private String productName;
    private String imagePath;
    private int rating;
    private String reviewText;
    private Date createdAt;

    public Review(int id, int productId, String productName,
                  String imagePath, int rating,
                  String reviewText, Date createdAt) {

        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.imagePath = imagePath;
        this.rating = rating;
        this.reviewText = reviewText;
        this.createdAt = createdAt;
    }

    public int getId() { return id; }
    public String getProductName() { return productName; }
    public String getImagePath() { return imagePath; }
    public int getRating() { return rating; }
    public String getReviewText() { return reviewText; }
    public Date getCreatedAt() { return createdAt; }
}