package me.a3d.jshpargalkatop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс узла дерева. Хранит название и HTML-контент.
 */
public class NavigationNode {
    private String title;
    private List<NavigationNode> children;
    private String content; // Поле для хранения HTML-текста

    public NavigationNode(String title) {
        this.title = title;
        this.children = new ArrayList<>();
        // Текст по умолчанию, если ничего не передали
        this.content = "<h2>" + title + "</h2><p>Текст в разработке.</p>";
    }

    public String getTitle() { return title; }
    public List<NavigationNode> getChildren() { return children; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public void addChild(NavigationNode child) {
        children.add(child);
    }

    public boolean removeChild(String title) {
        return children.removeIf(child -> child.getTitle().equals(title));
    }

    // Важно для отображения в дереве Swing (чтобы вместо адреса объекта показывалось название)
    @Override
    public String toString() {
        return title;
    }
}