package com.jundat95.locationtracking.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tinhngo on 3/3/17.
 */

public class DataModel {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("_rev")
    @Expose
    private String rev;
    @SerializedName("node")
    @Expose
    private String node;
    @SerializedName("location")
    @Expose
    private List<Double> location = null;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public DataModel() {
    }

    public DataModel(String id, String rev, String node, List<Double> location, String timestamp) {
        this.id = id;
        this.rev = rev;
        this.node = node;
        this.location = location;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
