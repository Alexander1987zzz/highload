package com.highload.socialnetwork.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserPost implements Serializable {
    private String id;
    private String text;
    private String authorUserId;
}
