package model;

public class ProductReview {

    private int productId;
    private int userId;
    private int rating;
    private String reviewText;

    public ProductReview(int productId, int userId, int rating, String reviewText) {
        this.productId = productId;
        this.userId = userId;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public int getProductId() { return productId; }
    public int getUserId() { return userId; }
    public int getRating() { return rating; }
    public String getReviewText() { return reviewText; }
}