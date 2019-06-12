package at.ac.tuwien.sepm.groupphase.backend.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Validated
@Configuration
@ConfigurationProperties("storage.location.images")
public class StorageProperties {
    // properties read from application yaml file
    private String base;
    private String profile;
    private String defaultImg;


    /**
     *  Default getter/setter needed for mapping of yaml properties
     */

    public String getBase() {
        return base;
    }


    public void setBase(String base) {
        this.base = base;
    }


    public String getProfile() {
        return profile;
    }


    public void setProfile(String profile) {
        this.profile = profile;
    }


    public String getDefaultImg() {
        return defaultImg;
    }


    public void setDefaultImg(String defaultImg) {
        this.defaultImg = defaultImg;
    }


    /*
     *  Custom getters for concatenated paths
     */


    public String getDefaultProfileImg() {
        return base + profile + defaultImg;
    }


    public String getProfileImgFolder() {
        return base + profile;
    }
}
