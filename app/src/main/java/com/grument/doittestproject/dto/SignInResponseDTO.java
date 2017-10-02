package com.grument.doittestproject.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;




public class SignInResponseDTO {

    public SignInResponseDTO() {
    }

    public SignInResponseDTO(String creationTime, String avatarImageLink, String token) {
        this.creationTime = creationTime;
        this.avatarImageLink = avatarImageLink;
        this.token = token;
    }

    @Expose
    @SerializedName("creation_time")
    private String creationTime;

    @Expose
    @SerializedName("avatar")
    private String avatarImageLink;


    @Expose
    @SerializedName("token")
    private String token;


    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getAvatarImageLink() {
        return avatarImageLink;
    }

    public void setAvatarImageLink(String avatarImageLink) {
        this.avatarImageLink = avatarImageLink;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "SignInResponseDTO{" +
                "creationTime='" + creationTime + '\'' +
                ", avatarImageLink='" + avatarImageLink + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
