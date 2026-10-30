package me.a3d.jshpargalkatop.ui;

import me.a3d.jshpargalkatop.model.NavigationNode;
import me.a3d.jshpargalkatop.model.ContentProvider;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class MainFrame extends JFrame {

    private JTree tree;
    private JEditorPane contentPane;

    public MainFrame(){
        setTitle("Java шпаргалка");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createTree();
        createContentPane();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tree), new JScrollPane(contentPane));
        splitPane.setDividerLocation(250);
        add(splitPane);

        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) return;
            String selectedNode = node.toString();
            contentPane.setText(ContentProvider.getHtmlContent(selectedNode));
            contentPane.setCaretPosition(0);
        });
    }


    private void createTree(){
        NavigationNode rootModel = ContentProvider.buildNavigationTree();

        DefaultMutableTreeNode rootSwingNode = convertToSwingNode(rootModel);

        tree = new JTree(rootSwingNode);

        tree.setRowHeight(25);

        tree.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

    }

    private  DefaultMutableTreeNode convertToSwingNode(NavigationNode modelNode) {
        DefaultMutableTreeNode swingNode = new DefaultMutableTreeNode(modelNode.getTitle());
        for (NavigationNode child : modelNode.getChildren()) {
            swingNode.add(convertToSwingNode(child));
        }

        return swingNode;
    }

    private void createContentPane(){
        contentPane = new JEditorPane();

        contentPane.setContentType("text/html");

        contentPane.setEditable(false);

    }
}
