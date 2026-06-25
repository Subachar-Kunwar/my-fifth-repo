package controller;

import dao.UserDAO;
import net.coobird.thumbnailator.Thumbnails;
import java.io.File;

public class ProfileController {

    private final UserDAO userDAO = new UserDAO();
    private final int userId;

    public ProfileController(int userId) {
        this.userId = userId;
    }

    // ─── Get Current Profile Pic Path ─────────────────────────
    public String getProfilePic() {
        return userDAO.getProfilePic(userId);
    }

    // ─── Update Profile Pic ───────────────────────────────────
    public String updateProfilePic(String sourceImagePath) {

        if (sourceImagePath == null || sourceImagePath.trim().isEmpty()) {
            return "Please select an image!";
        }

        File sourceFile = new File(sourceImagePath);
        if (!sourceFile.exists()) {
            return "Selected file does not exist!";
        }

        String lower = sourceImagePath.toLowerCase();
        if (!lower.endsWith(".jpg") && !lower.endsWith(".jpeg") 
                && !lower.endsWith(".png")) {
            return "Only JPG, JPEG, or PNG images allowed!";
        }

        File profilesDir = new File("src/images/profiles");
        if (!profilesDir.exists()) {
            profilesDir.mkdirs();
        }

        String fileName = "user_" + userId + ".jpg";
        File destFile = new File(profilesDir, fileName);

        try {
            Thumbnails.of(sourceFile)
                .size(200, 200)
                .outputFormat("jpg")
                .outputQuality(0.9)
                .toFile(destFile);
        } catch (Exception e) {
            return "Failed to process image: " + e.getMessage();
        }

        String dbPath = "images/profiles/" + fileName;
        boolean success = userDAO.updateProfilePic(userId, dbPath);

        return success ? null : "Failed to update database. Please try again.";
    }

    // ─── Load Profile Pic into JLabel ─────────────────────────
    public void loadProfilePic(javax.swing.JLabel imageLabel, String defaultImagePath) {
        String dbPath = getProfilePic();
        String pathToLoad;

        if (dbPath != null && !dbPath.isEmpty()) {
            File f = new File("src/" + dbPath);
            if (f.exists()) {
                pathToLoad = f.getAbsolutePath();
            } else {
                pathToLoad = defaultImagePath;
            }
        } else {
            pathToLoad = defaultImagePath;
        }

        try {
            javax.swing.ImageIcon icon = new javax.swing.ImageIcon(pathToLoad);
            java.awt.Image scaled = icon.getImage()
                .getScaledInstance(105, 105, java.awt.Image.SCALE_SMOOTH);
            imageLabel.setIcon(new javax.swing.ImageIcon(scaled));
        } catch (Exception e) {
            System.out.println("Load image error: " + e.getMessage());
        }
    }

    // ─── Auto-Load Profile Pic ────────────────────────────────
    public void initProfilePic(javax.swing.JLabel imageLabel) {
        String defaultPath = "src/group7/rewear/cattpic.png";
        loadProfilePic(imageLabel, defaultPath);
    }
}