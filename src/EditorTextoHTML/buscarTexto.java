/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EditorTextoHTML;

/**
 *
 * @author edrei
 */
import javax.swing.*;
import javax.swing.text.*;

public class buscarTexto {

    private JTextPane textPane;

    public buscarTexto(JTextPane textPane) {
        this.textPane = textPane;
    }

    public void buscarEnTexto(String buscarTermino) {
        Document documento = textPane.getDocument();
        int indiceInicial = 0;
        try {
            while (indiceInicial + buscarTermino.length() <= documento.getLength()) {
                String texto = documento.getText(indiceInicial, buscarTermino.length());
                if (texto.equalsIgnoreCase(buscarTermino)) {
                    textPane.setCaretPosition(indiceInicial);
                    textPane.moveCaretPosition(indiceInicial + buscarTermino.length());
                    textPane.requestFocusInWindow();
                    return;
                }
                indiceInicial++;
            }
            JOptionPane.showMessageDialog(textPane, "Texto no encontrado.");
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
