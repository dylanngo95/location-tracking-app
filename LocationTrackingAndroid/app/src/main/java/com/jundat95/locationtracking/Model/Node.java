package com.jundat95.locationtracking.Model;

/**
 * Created by tinhngo on 3/5/17.
 */

public class Node {

    private String title;
    private int nodeId;

    public Node() {
    }

    public Node(String title, int nodeId) {
        this.title = title;
        this.nodeId = nodeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }
}
