package me.a3d.jshpargalkatop.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentProvider {

    public static NavigationNode buildNavigationTree() {
        NavigationNode root = new NavigationNode("JShpargalkaTOP");

        NavigationNode javaNode = new NavigationNode("Java");
        javaNode.addChild(new NavigationNode("Введение"));
        javaNode.addChild(new NavigationNode("Переменные и типы"));
        javaNode.addChild(new NavigationNode("Циклы"));
        root.addChild(javaNode);

        NavigationNode extraNode = new NavigationNode("Дополнительно");
        extraNode.addChild(new NavigationNode("Установка Java и IDEA"));
        extraNode.addChild(new NavigationNode("Горячие клавиши"));
        extraNode.addChild(new NavigationNode("Команды Git"));
        root.addChild(extraNode);

        return root;
    }

    public static NavigationNode buildTreeFromFile(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        NavigationNode root = new NavigationNode("Импортированный справочник");
        Map<String, NavigationNode> nodesMap = new HashMap<>();
        nodesMap.put("Корень", root);

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            // Разбиваем строку на 4 части (уровень, имя, родитель, контент)
            String[] parts = line.split(";", -1);
            if (parts.length < 3) continue;

            String nodeName = parts[1].trim();
            String parentName = parts[2].trim();

            NavigationNode newNode = new NavigationNode(nodeName);


            if (parts.length >= 4) {
                String htmlContent = parts[3].trim();
                if (!htmlContent.isEmpty()) {
                    newNode.setContent(htmlContent);
                }
            }

            NavigationNode parent = nodesMap.get(parentName);
            if (parent != null) {
                parent.addChild(newNode);
                nodesMap.put(nodeName, newNode);
            }
        }
        return root;
    }

    // Метод оставили для совместимости, но теперь тексты берутся из модели
    public static String getHtmlContent(String topicName) {
        String content = "<h2>" + topicName + "</h2><p>Текст в разработке.</p>";
        return "<html><body style='padding: 20px; font-family: sans-serif;'>" + content + "</body></html>";
    }
}