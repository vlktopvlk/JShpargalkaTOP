import me.a3d.jshpargalkatop.ui.MainFrame;

import javax.swing.SwingUtilities;

void main() {
    IO.println("Запуск программы Шпаргалка v0.1.0...");
    SwingUtilities.invokeLater(()->{
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    });
}