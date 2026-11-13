package me.a3d.jshpargalkatop.ui;

import me.a3d.jshpargalkatop.model.NavigationNode;
import me.a3d.jshpargalkatop.model.ContentProvider;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainFrame extends JFrame {

    private JTree tree;
    private JEditorPane contentPane;

    public MainFrame() {
        setTitle("Java шпаргалка");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createMenuBar();
        createTree();
        createContentPane();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tree), new JScrollPane(contentPane));
        splitPane.setDividerLocation(250);
        add(splitPane);

        // Обработчик кликов по дереву
        tree.addTreeSelectionListener(_ -> {
            DefaultMutableTreeNode selectedSwingNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedSwingNode == null) return;

            Object userObject = selectedSwingNode.getUserObject();
            String htmlToDisplay;

            if (userObject instanceof NavigationNode node) {
                htmlToDisplay = node.getContent();
            } else {
                htmlToDisplay = "<h2>" + selectedSwingNode + "</h2>";
            }

            contentPane.setText("<html><body style='padding: 20px; font-family: sans-serif;'>" + htmlToDisplay + "</body></html>");
            contentPane.setCaretPosition(0);
        });
    }

    private void createTree() {
        NavigationNode rootModel = ContentProvider.buildNavigationTree();
        DefaultMutableTreeNode rootSwingNode = convertToSwingNode(rootModel);

        tree = new JTree(rootSwingNode);
        tree.setRowHeight(25);
        tree.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    private DefaultMutableTreeNode convertToSwingNode(NavigationNode modelNode) {
        DefaultMutableTreeNode swingNode = new DefaultMutableTreeNode(modelNode);
        for (NavigationNode child : modelNode.getChildren()) {
            swingNode.add(convertToSwingNode(child));
        }
        return swingNode;
    }

    private void rebuildNode(DefaultMutableTreeNode parentNode, List<DefaultMutableTreeNode> sortedChildren) {
        parentNode.removeAllChildren();
        for (DefaultMutableTreeNode child : sortedChildren) {
            parentNode.add(child);
        }
        ((DefaultTreeModel) tree.getModel()).reload(parentNode);
    }

    private void createContentPane() {
        contentPane = new JEditorPane();
        contentPane.setContentType("text/html");
        contentPane.setEditable(false);
        contentPane.setText("<html><body style='padding:20px;'><h2>Добро пожаловать!</h2><p>Выберите тему слева.</p></body></html>");
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createEditMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createHelpMenu());
        setJMenuBar(menuBar);
    }

    // ==========================================
    // МЕНЮ "ФАЙЛ"
    // ==========================================
    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("Файл");

        JMenuItem importItem = new JMenuItem("Импорт из файла...");
        importItem.addActionListener(_ -> {
            JFileChooser fileChooser = new JFileChooser();
            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToOpen = fileChooser.getSelectedFile();
                try {
                    NavigationNode newRootModel = ContentProvider.buildTreeFromFile(fileToOpen.getAbsolutePath());
                    DefaultMutableTreeNode newRootSwing = convertToSwingNode(newRootModel);
                    tree.setModel(new DefaultTreeModel(newRootSwing));

                    for (int i = 0; i < tree.getRowCount(); i++) {
                        tree.expandRow(i);
                    }
                    JOptionPane.showMessageDialog(this, "Импорт прошел успешно!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        JMenuItem exitItem = new JMenuItem("Выход");
        exitItem.addActionListener(_ -> System.exit(0));

        fileMenu.add(importItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        return fileMenu;
    }

    // ==========================================
    // МЕНЮ "ПРАВКА"
    // ==========================================
    private JMenu createEditMenu() {
        JMenu editMenu = new JMenu("Правка");

        JMenuItem deleteItem = new JMenuItem("Удалить выбранный узел");
        deleteItem.addActionListener(_ -> {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (selectedNode == null) {
                JOptionPane.showMessageDialog(this, "Выберите узел!", "Внимание", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (selectedNode.isRoot()) {
                JOptionPane.showMessageDialog(this, "Нельзя удалить корень!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Удалить узел '" + selectedNode + "'?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                model.removeNodeFromParent(selectedNode);
                contentPane.setText("<html><body style='padding:20px;'><h2>Узел удален</h2></body></html>");
            }
        });

        editMenu.add(deleteItem);
        return editMenu;
    }

    // ==========================================
    // МЕНЮ "ВИД" (СОРТИРОВКА)
    // ==========================================
    private JMenu createViewMenu() {
        JMenu viewMenu = new JMenu("Вид");

        JMenuItem sortAnonAscItem = new JMenuItem("По возрастанию (Анонимный класс)");
        sortAnonAscItem.addActionListener(_ -> sortSelectedNode(false, true));

        JMenuItem sortLambdaAscItem = new JMenuItem("По возрастанию (Лямбда)");
        sortLambdaAscItem.addActionListener(_ -> sortSelectedNode(false, false));

        JMenuItem sortAnonDescItem = new JMenuItem("По убыванию (Анонимный класс)");
        sortAnonDescItem.addActionListener(_ -> sortSelectedNode(true, true));

        JMenuItem sortLambdaDescItem = new JMenuItem("По убыванию (Лямбда)");
        sortLambdaDescItem.addActionListener(_ -> sortSelectedNode(true, false));

        viewMenu.add(sortAnonAscItem);
        viewMenu.add(sortLambdaAscItem);
        viewMenu.addSeparator();
        viewMenu.add(sortAnonDescItem);
        viewMenu.add(sortLambdaDescItem);

        return viewMenu;
    }

    // Вспомогательный метод для сортировки (чтобы не дублировать код)
    private void sortSelectedNode(boolean descending, boolean useAnonymousClass) {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (selectedNode == null || selectedNode.getChildCount() == 0) return;

        List<DefaultMutableTreeNode> children = new ArrayList<>();
        for (int i = 0; i < selectedNode.getChildCount(); i++) {
            children.add((DefaultMutableTreeNode) selectedNode.getChildAt(i));
        }

        if (useAnonymousClass) {
            // Демонстрация анонимного класса (оставляем для урока)
            Comparator<DefaultMutableTreeNode> comparator = new Comparator<>() {
                @Override
                public int compare(DefaultMutableTreeNode n1, DefaultMutableTreeNode n2) {
                    return descending ? n2.toString().compareTo(n1.toString()) : n1.toString().compareTo(n2.toString());
                }
            };
            children.sort(comparator);
        } else {
            // Современная лямбда + Comparator.comparing
            Comparator<DefaultMutableTreeNode> comparator = Comparator.comparing(Object::toString);
            if (descending) {
                comparator = comparator.reversed();
            }
            children.sort(comparator);
        }

        rebuildNode(selectedNode, children);
    }

    // ==========================================
    // МЕНЮ "СПРАВКА"
    // ==========================================
    private JMenu createHelpMenu() {
        JMenu helpMenu = new JMenu("Справка");

        JMenuItem aboutItem = new JMenuItem("О программе");
        aboutItem.addActionListener(_ -> JOptionPane.showMessageDialog(this, "JShpargalkaTOP v0.3.0\nУчебный проект A3D.me", "О программе", JOptionPane.INFORMATION_MESSAGE));

        helpMenu.add(aboutItem);
        return helpMenu;
    }
}