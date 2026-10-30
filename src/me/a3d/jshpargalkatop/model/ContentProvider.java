package me.a3d.jshpargalkatop.model;

public class ContentProvider {
    public static NavigationNode buildNavigationTree() {
        NavigationNode root = new NavigationNode("JShpargalkatop");

        NavigationNode javaNode = new NavigationNode("Java");
        javaNode.addChild(new NavigationNode("Введение"));
        javaNode.addChild(new NavigationNode("Переменные и типы"));
        javaNode.addChild(new NavigationNode("Циклы"));

        root.addChild(javaNode);

        NavigationNode extraNode = new NavigationNode("Дополнительно");
        extraNode.addChild(new NavigationNode("1Введение"));
        extraNode.addChild(new NavigationNode("1Переменные и типы"));
        extraNode.addChild(new NavigationNode("1Циклы"));

        root.addChild(extraNode);

        return root;
    }

    public static String getHtmlContent(String topicName) {
        String content;

        switch (topicName){
            case "Введение":
                content = "<h2>Введение в Java</h2><p>Что-то про Java</p>";
                break;
            case "Переменные и типы":
                content = "<h2>Введение в Java</h2><p>Что-то про Java</p>";
                break;
            default:
                content = "<h2>" + topicName + "</h2><p>Текст не найден</p>";
                break;

        }
        return "<html><body style='padding: 20px font-Family: sans-serif;'>" + content + "</body></html>";
    }
}
