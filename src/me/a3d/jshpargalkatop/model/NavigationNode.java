package me.a3d.jshpargalkatop.model;

import java.util.ArrayList;
import java.util.List;

public class NavigationNode {
    private String title;
    private List<NavigationNode> children;

    //@Contract(pure=true)
    public NavigationNode(String title) {
        this.title = title;
        this.children = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public List<NavigationNode> getChildren() {
        return children;
    }

    public void addChild(NavigationNode child) {
        children.add(child);
    }

}
