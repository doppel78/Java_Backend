package HomeWork_4.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Data {

    @JsonProperty("id")
    private String id;
    @JsonProperty("deletehash")
    private String deletehash;
    @JsonProperty("account_id")
    private Integer accountId;
    @JsonProperty("account_url")
    private String accountUrl;
    @JsonProperty("ad_type")
    private Object adType;
    @JsonProperty("ad_url")
    private Object adUrl;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private Object description;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("width")
    private Integer width;
    @JsonProperty("height")
    private Integer height;
    @JsonProperty("size")
    private Integer size;
    @JsonProperty("views")
    private Integer views;
    @JsonProperty("section")
    private Object section;
    @JsonProperty("vote")
    private Object vote;
    @JsonProperty("bandwidth")
    private Integer bandwidth;
    @JsonProperty("animated")
    private Boolean animated;
    @JsonProperty("favorite")
    private Boolean favorite;
    @JsonProperty("in_gallery")
    private Boolean inGallery;
    @JsonProperty("in_most_viral")
    private Boolean inMostViral;
    @JsonProperty("has_sound")
    private Boolean hasSound;
    @JsonProperty("is_ad")
    private Boolean isAd;
    @JsonProperty("nsfw")
    private Object nsfw;
    @JsonProperty("link")
    private String link;
    @JsonProperty("tags")
    public List<Object> tags = new ArrayList<Object>();
    @JsonProperty("datetime")
    private Integer datetime;
    @JsonProperty("mp4")
    private String mp4;
    @JsonProperty("hls")
    private String hls;
    @JsonProperty("edited")
    private String edited;
    @JsonProperty("mp4_size")
    public Integer mp4Size;
    @JsonProperty("processing")
    public Processing processing;
    @JsonProperty("url")
    public String url;
    @JsonProperty("bio")
    public Object bio;
    @JsonProperty("avatar")
    public String avatar;
    @JsonProperty("avatar_name")
    public String avatarName;
    @JsonProperty("cover")
    public String cover;
    @JsonProperty("cover_name")
    public String coverName;
    @JsonProperty("reputation")
    public Integer reputation;
    @JsonProperty("reputation_name")
    public String reputationName;
    @JsonProperty("created")
    public Integer created;
    @JsonProperty("pro_expiration")
    public Boolean proExpiration;
    @JsonProperty("user_follow")
    public UserFollow userFollow;
    @JsonProperty("is_blocked")
    public Boolean isBlocked;
    @JsonProperty("error")
    public String error;
    @JsonProperty("request")
    public String request;
    @JsonProperty("method")
    public String method;
}
