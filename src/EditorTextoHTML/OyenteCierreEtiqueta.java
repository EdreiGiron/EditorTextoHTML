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
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class OyenteCierreEtiqueta extends KeyAdapter {

    private JTextPane textPane;
    private JTree arbolDOM;
    private static final String[] ETIQUETAS_SIN_CIERRE = {"br", "hr", "img", "input", "link", "meta"};

    public OyenteCierreEtiqueta(JTextPane textPane, JTree domTree) {
        this.textPane = textPane;
        this.arbolDOM = domTree;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == '>') {
            insertarEtiquetaCierre();
            new ActualizadorDOM(arbolDOM).ActualizarArbol(textPane.getText());
        }
    }

    private void insertarEtiquetaCierre() {
        int posicionIntercalada = textPane.getCaretPosition();
        Document doc = textPane.getDocument();
        StringBuilder stringBuilder = new StringBuilder();
        try {
            stringBuilder.append(doc.getText(0, posicionIntercalada));
            String ultimaEtiquetaAbierta = UltimaEtiquetaAbierta(stringBuilder.toString());
            if (etiquetasSinCierre(ultimaEtiquetaAbierta)) {
                return;
            }
            String nombreEtiqueta = NombreEtiqueta(ultimaEtiquetaAbierta);
            String etiquetaCierre = "</" + nombreEtiqueta;
            stringBuilder.append(">").append(etiquetaCierre);
            stringBuilder.append(doc.getText(posicionIntercalada, doc.getLength() - posicionIntercalada));
            doc.remove(0, doc.getLength());
            doc.insertString(0, stringBuilder.toString(), null);

            int nuevaPosicionIntercalada = posicionIntercalada + etiquetaCierre.length() + 1;
            if (nuevaPosicionIntercalada > doc.getLength()) {
                nuevaPosicionIntercalada = doc.getLength();
            }
            textPane.setCaretPosition(nuevaPosicionIntercalada);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private String UltimaEtiquetaAbierta(String texto) {
        int ultimoIndice = texto.lastIndexOf('<');
        int nuevoEspacioIndice = texto.indexOf(' ', ultimoIndice);
        if (nuevoEspacioIndice != -1) {
            return texto.substring(ultimoIndice + 1, nuevoEspacioIndice);
        } else {
            return texto.substring(ultimoIndice + 1);
        }
    }

    private String NombreEtiqueta(String etiqueta) {
        int indice = etiqueta.indexOf('>');
        if (indice != -1) {
            return etiqueta.substring(0, indice);
        } else {
            return etiqueta;
        }
    }

    private boolean etiquetasSinCierre(String etiqueta) {
        for (String etiquetaSinCierre : ETIQUETAS_SIN_CIERRE) {
            if (etiquetaSinCierre.equalsIgnoreCase(etiqueta)) {
                return true;
            }
        }
        return false;
    }
}
