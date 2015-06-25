package net.ruggedodyssey.backend.domain;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
@Cache
public class Profile {
    String displayName;
    String mainEmail;
    boolean muted = false;

    @Id
    String userId;
    
    /**
     * Public constructor for Profile.
     * @param userId The user id, obtained from the email
     * @param displayName Any string user wants us to display him/her on this system.
     * @param mainEmail User's main e-mail address.
     * 
     */
    public Profile(String userId, String displayName, String mainEmail) {
        this.userId = userId;
        this.displayName = displayName;
        this.mainEmail = mainEmail;
    }
    
    public String getDisplayName() {
        return displayName;
    }

    public String getMainEmail() {
        return mainEmail;
    }

    public String getUserId() {
        return userId;
    }

    /**
     * Just making the default constructor private.
     */
    private Profile() {}
    
    /**
     * Update the Profile with the given displayName
     *
     * @param displayName
     */
    public void update(String displayName) {
        if (displayName != null) {
            this.displayName = displayName;
        }
    }

    /**
     * Mute all triggers for this profile
     * @param mute
     */
    public void setMute(boolean mute) {
        this.muted = mute;
    }

    public boolean isMuted() {
        return muted;
    }
}
