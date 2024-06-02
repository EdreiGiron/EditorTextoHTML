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
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class ActualizadorDOM {

    private JTree arbolDOM;

    public ActualizadorDOM(JTree arbolDOM) {
        this.arbolDOM = arbolDOM;
    }

    public void ActualizarArbol(String html) {
        DefaultMutableTreeNode rama = new DefaultMutableTreeNode("!DOCTYPE");
        DefaultMutableTreeNode padreActual = rama;
        DefaultTreeModel modeloArbol = new DefaultTreeModel(rama);
        arbolDOM.setModel(modeloArbol);

        int indiceInicial = html.indexOf('<');
        while (indiceInicial >= 0) {
            int indiceFinal = html.indexOf('>', indiceInicial);
            if (indiceFinal >= 0) {
                String etiqueta = html.substring(indiceInicial + 1, indiceFinal);
                if (etiqueta.startsWith("/")) {
                    padreActual = (DefaultMutableTreeNode) padreActual.getParent();
                } else {
                    DefaultMutableTreeNode nodo = new DefaultMutableTreeNode(etiqueta);
                    modeloArbol.insertNodeInto(nodo, padreActual, padreActual.getChildCount());
                    padreActual = nodo;
                }
                indiceInicial = html.indexOf('<', indiceFinal);

                if (indiceInicial > indiceFinal + 1) {
                    String texto = html.substring(indiceFinal + 1, indiceInicial).trim();
                    if (!texto.isEmpty()) {
                        DefaultMutableTreeNode textNode = new DefaultMutableTreeNode(texto);
                        modeloArbol.insertNodeInto(textNode, padreActual, padreActual.getChildCount());
                    }
                }
            } else {
                break;
            }
        }

        ExpandirTodosLosNodos(arbolDOM, 0, arbolDOM.getRowCount());
    }

    private void ExpandirTodosLosNodos(JTree arbol, int indiceInicial, int cuentaRamas) {
        for (int i = indiceInicial; i < cuentaRamas; ++i) {
            arbol.expandRow(i);
        }

        if (arbol.getRowCount() != cuentaRamas) {
            ExpandirTodosLosNodos(arbol, cuentaRamas, arbol.getRowCount());
        }
    }
}
