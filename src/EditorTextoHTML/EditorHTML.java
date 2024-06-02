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
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class EditorHTML extends JFrame implements ActionListener {

    private JTextPane areaCodigoHTML;
    private JTextArea areaNumeroLinea;
    private JFileChooser buscarArchivo;
    private JTree arbolDOM;
    private List<String> palabrasReservadas = Arrays.asList(
            "html", "head", "body", "title", "div", "p", "span", "a", "img", "table",
            "tr", "td", "ul", "ol", "li", "form", "input", "button", "h1", "h2", "h3",
            "h4", "h5", "h6", "br", "hr", "img", "input", "link", "meta"
    );

    public EditorHTML() {
        setTitle("EDITOR HTML");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        areaCodigoHTML = new JTextPane();
        areaCodigoHTML.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaCodigoHTML.setBackground(Color.BLACK);
        areaCodigoHTML.getDocument().addDocumentListener(new ResaltarTexto(areaCodigoHTML, palabrasReservadas));

        arbolDOM = new JTree();
        arbolDOM.setFont(new Font("Monospaced", Font.PLAIN, 13));
        arbolDOM.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        arbolDOM.setBackground(Color.DARK_GRAY);

        areaCodigoHTML.addKeyListener(new OyenteCierreEtiqueta(areaCodigoHTML, arbolDOM));

        areaNumeroLinea = new JTextArea("1");
        areaNumeroLinea.setBackground(Color.DARK_GRAY);
        areaNumeroLinea.setForeground(Color.LIGHT_GRAY);
        areaNumeroLinea.setEditable(false);
        areaNumeroLinea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(areaCodigoHTML);
        scrollPane.setRowHeaderView(areaNumeroLinea);

        JScrollPane treeScrollPane = new JScrollPane(arbolDOM);
        treeScrollPane.setPreferredSize(new Dimension(200, 0));

        JMenuBar menuEditorHTML = new JMenuBar();
        JMenu elementosMenu = new JMenu("Archivo");
        String[] menuItems = {"Nuevo", "Abrir", "Guardar", "Guardar Como", "Buscar", "Imprimir", "Salir"};
        for (String item : menuItems) {
            JMenuItem itemMenu = new JMenuItem(item);
            itemMenu.addActionListener(this);
            elementosMenu.add(itemMenu);
            if (item.equals("Salir")) {
                elementosMenu.addSeparator();
            }
        }
        menuEditorHTML.add(elementosMenu);

        Container contenedorPrincipal = getContentPane();
        contenedorPrincipal.setLayout(new BorderLayout());
        contenedorPrincipal.add(scrollPane, BorderLayout.CENTER);
        contenedorPrincipal.add(treeScrollPane, BorderLayout.WEST);
        setJMenuBar(menuEditorHTML);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EditorHTML::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "Nuevo":
                nuevoDocumento();
                break;
            case "Abrir":
                abrirDocumento();
                break;
            case "Guardar":
                guardarDocumento();
                break;
            case "Guardar Como":
                guardarDocumentoComo();
                break;
            case "Buscar":
                buscarTexto();
                break;
            case "Imprimir":
                imprimirDocumento();
                break;
            case "Salir":
                salir();
                break;
        }
    }

    private void nuevoDocumento() {
        areaCodigoHTML.setText("");
        ActualizarArbolDOM();
    }

    private void abrirDocumento() {
        if (buscarArchivo == null) {
            buscarArchivo = new JFileChooser();
        }
        int result = buscarArchivo.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = buscarArchivo.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                areaCodigoHTML.setText(sb.toString());
                ActualizarArbolDOM();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarDocumento() {
        guardarDocumentoComo();
    }

    private void guardarDocumentoComo() {
        if (buscarArchivo == null) {
            buscarArchivo = new JFileChooser();
        }
        int resultado = buscarArchivo.showSaveDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = buscarArchivo.getSelectedFile();
            try (BufferedWriter escribir = new BufferedWriter(new FileWriter(archivo))) {
                escribir.write(areaCodigoHTML.getText());
                ActualizarArbolDOM();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void imprimirDocumento() {
        try {
            areaCodigoHTML.print();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    private void salir() {
        System.exit(0);
    }

    private void ActualizarArbolDOM() {
        String html = areaCodigoHTML.getText();
        new ActualizadorDOM(arbolDOM).ActualizarArbol(html);
    }

    private void buscarTexto() {
        String buscarTexto = JOptionPane.showInputDialog(this, "Ingrese el texto a buscar:");
        if (buscarTexto != null && !buscarTexto.isEmpty()) {
            new buscarTexto(areaCodigoHTML).buscarEnTexto(buscarTexto);
        }
    }
}
