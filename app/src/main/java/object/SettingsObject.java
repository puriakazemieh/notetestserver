package object;

import android.graphics.Color;

public class SettingsObject {


    private int SETTINGS_NIGHT_MODE;
    private String SETTINGS_FONT;
    private String SETTINGS_BG_COLOR;
    private int SETTINGS_FONT_SIZE;
    private String SETTINGS_TEXT_COLOR;
    private AvatarObject AvatarObject;

    public int getNightMode() {
        return SETTINGS_NIGHT_MODE;
    }

    public String getSETTINGS_FONT() {
        return SETTINGS_FONT;
    }


    public int getBgColor() {
        try {
            if (SETTINGS_BG_COLOR.contains("#")) {
                SETTINGS_BG_COLOR = "#" + SETTINGS_BG_COLOR;
            }
            return Color.parseColor(SETTINGS_BG_COLOR);
        } catch (Exception e) {

            return Color.WHITE;
        }
    }
    public String getBgColor(int t){
        return SETTINGS_BG_COLOR;
    }

    public String getTextColor(int t){
        return SETTINGS_TEXT_COLOR;
    }
    public int getTextColor() {
        try {
            if (SETTINGS_TEXT_COLOR.contains("#")) {
                SETTINGS_TEXT_COLOR = "#" + SETTINGS_TEXT_COLOR;
            }
            return Color.parseColor(SETTINGS_TEXT_COLOR);
        } catch (Exception e) {

            return Color.BLACK;
        }
    }

    public int getFontSize() {
        return SETTINGS_FONT_SIZE;
    }


    public AvatarObject getAvatarObject() {
        return AvatarObject;
    }
}
