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
import java.util.Arrays;
import java.util.List;

public class OyenteCierreEtiqueta extends KeyAdapter {

    private JTextPane textPane;
    private JTree arbolDOM;
    private static final List<String> ETIQUETAS_SIN_CIERRE = Arrays.asList(
            "br", "hr", "img", "input", "link", "meta"
    );
    private static final List<String> ETIQUETAS_RESERVADAS = Arrays.asList(
            "html", "head", "body", "title", "div", "p", "span", "a", "img", "table",
            "tr", "td", "ul", "ol", "li", "form", "input", "button", "h1", "h2", "h3",
            "h4", "h5", "h6", "br", "hr", "img", "input", "link", "meta", "tbody"
           
    );

    public OyenteCierreEtiqueta(JTextPane textPane, JTree arbolDOM) {
        this.textPane = textPane;
        this.arbolDOM = arbolDOM;
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
            String ultimaEtiquetaAbierta = obtenerUltimaEtiquetaAbierta(stringBuilder.toString());
            if (ultimaEtiquetaAbierta.isEmpty() || esEtiquetaSinCierre(ultimaEtiquetaAbierta) || !ETIQUETAS_RESERVADAS.contains(ultimaEtiquetaAbierta)) {
                return;
            }
            String nombreEtiqueta = obtenerNombreEtiqueta(ultimaEtiquetaAbierta);
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

    private String obtenerUltimaEtiquetaAbierta(String texto) {
        int ultimoIndice = texto.lastIndexOf('<');
        if (ultimoIndice == -1) {
            return "";
        }
        int nuevoEspacioIndice = texto.indexOf(' ', ultimoIndice);
        int nuevoCierreIndice = texto.indexOf('>', ultimoIndice);

        if (nuevoEspacioIndice == -1 || (nuevoCierreIndice != -1 && nuevoCierreIndice < nuevoEspacioIndice)) {
            nuevoEspacioIndice = nuevoCierreIndice;
        }

        if (nuevoEspacioIndice == -1) {
            return texto.substring(ultimoIndice + 1).trim();
        }

        return texto.substring(ultimoIndice + 1, nuevoEspacioIndice).trim();
    }

    private String obtenerNombreEtiqueta(String etiqueta) {
        int indice = etiqueta.indexOf('>');
        if (indice != -1) {
            return etiqueta.substring(0, indice);
        } else {
            return etiqueta;
        }
    }

    private boolean esEtiquetaSinCierre(String etiqueta) {
        return ETIQUETAS_SIN_CIERRE.contains(etiqueta.toLowerCase());
    }
}
