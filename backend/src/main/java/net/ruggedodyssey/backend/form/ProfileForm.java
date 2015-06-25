package net.ruggedodyssey.backend.form;

/**
 * Pojo representing a profile form on the client side.
 */
public class ProfileForm {
    /**
     * Any string user wants us to display him/her on this system.
     */
    private String displayName;

    /**
     * Are notifications muted
     */
    private boolean muted;

    private ProfileForm() {}

    /**
     * Constructor for ProfileForm, solely for unit test.
     * @param displayName A String for displaying the user on this system.
     */
    public ProfileForm(String displayName, boolean muted) {
        this.displayName = displayName;
        this.muted = muted;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isMuted() {
        return muted;
    }
    

}
