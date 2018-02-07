package virusgui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * @author Nicky van Bergen, made in Austria & the Netherlands
 * @version IDE 8.2 & Java 8
 */
public class VirusGui extends JFrame implements ActionListener {

    /**
     * Declaratie van gebruikte elementen voor de GUI
     */
    JButton openButton;
    JFileChooser FileChooser;
    JLabel bestandLabel;
    JTextField bestandNaam;
    JLabel classificationLabel;
    JLabel hostid1Label;
    JLabel hostid2Label;
    static JComboBox classificationbox;
    static JComboBox<Integer> hostid1box;
    static JComboBox hostid2box;
    JRadioButton rbID;
    JRadioButton rbHost;
    JRadioButton rbClass;
    JLabel sortLabel;
    static JTextArea virus1;
    static JTextArea virus2;
    JLabel virus1Label;
    JLabel virus2Label;
    JScrollPane scrollPane;
    JScrollPane scrollPane2;
    static JTextArea overlap;
    JLabel overlapLabel;
    JScrollPane scrollPane3;
    JButton submitButton;

    static ArrayList<Virus> virusList;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        VirusGui frame = new VirusGui();
        frame.setSize(870, 700);
        frame.setTitle("VirusGui");
        frame.createGUI();
        frame.show();
    }

    /**
     *
     */
    public void createGUI() {
        /**
         * In deze methoden wordt de Gui gemaakt. De JComboBoxen bevatten nog
         * een string met NULL. Hier wordt na het inlezen pas de juiste String
         * met opties gepaaltst.
         */
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container window = getContentPane();
        window.setLayout(new FlowLayout());

        bestandLabel = new JLabel();
        bestandLabel.setText("File name:");
        bestandNaam = new JTextField("", 20);
        window.add(bestandLabel);
        window.add(bestandNaam);
        openButton = new JButton("Select File");
        window.add(openButton);
        openButton.addActionListener(this);

        classificationLabel = new JLabel("Classification:");
        String[] classificationoption = {"NULL"};
        classificationbox = new JComboBox(classificationoption);
        classificationbox.setPreferredSize(new Dimension(800, 25));
        window.add(classificationLabel);
        window.add(classificationbox);

        hostid1Label = new JLabel("Host ID 1:");
        hostid1box = new JComboBox();
        hostid1box.setPreferredSize(new Dimension(340, 25));
        window.add(hostid1Label);
        window.add(hostid1box);

        hostid2Label = new JLabel("Host ID 2:");
        hostid2box = new JComboBox();
        hostid2box.setPreferredSize(new Dimension(340, 25));
        window.add(hostid2Label);
        window.add(hostid2box);

        sortLabel = new JLabel("                                                                                  Sort by:");
        window.add(sortLabel);
        rbID = new JRadioButton("ID");
        rbID.setSelected(true);
        rbHost = new JRadioButton("Classification");
        rbClass = new JRadioButton("amount Hosts                                                                   ");

        ButtonGroup sortgroup = new ButtonGroup();
        sortgroup.add(rbID);
        sortgroup.add(rbHost);
        sortgroup.add(rbClass);
        window.add(rbID);
        window.add(rbHost);
        window.add(rbClass);

        virus1Label = new JLabel("                   VirusList 1:");
        virus2Label = new JLabel("VirusList 2:");
        virus1 = new JTextArea(15, 20);
        virus2 = new JTextArea(15, 20);
        scrollPane = new JScrollPane(virus1,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        virus1.setEditable(false);
        scrollPane2 = new JScrollPane(virus2,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        virus2.setEditable(false);
        window.add(virus1Label);
        window.add(scrollPane);
        window.add(virus2Label);
        window.add(scrollPane2);

        overlapLabel = new JLabel("                     overlap viruses from hosts");
        overlap = new JTextArea(15, 20);
        scrollPane3 = new JScrollPane(overlap,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);;
        window.add(overlapLabel);
        window.add(scrollPane3);

        submitButton = new JButton("Submit");
        window.add(submitButton);
        submitButton.addActionListener(this);

    }

    private BufferedReader infile;

    @Override
    public void actionPerformed(ActionEvent event) {
        /**
         * In de actionPerformed wordt gekeken welke acties er door de gebruiker
         * worden uitgevoerd Wanneer de gebruiker op de openButton klikt kan er
         * met de filechooser een bestand worden geopend en ingelezen worden
         * Vervolgens wordt alles wat ingelezen is meteen in objecten gezet met
         * behulp van de constructor. Ook wordt de methode makeComboList
         * aangeroepen om de juiste String met opties in de comboboxen te
         * zetten. Wanneer submit wordt aangeklikt methodes getaskedvirusList,
         * getvirusbyhostList1 & getvirusbyhostList2 aangeroepen. Vervolgens
         * wordt gecheckt welke sorteering is gekozen en wordt de bijbehorende
         * methode aangeroepen. De sets die hierbij gemaakt worden worden
         * vervolgens in de textArea's weergegeven
         */
        String bestand;
        FileChooser = new JFileChooser();
        if (event.getSource() == openButton) {
            String line;
            File selectFile;
            int reply;
            int i = 0;
            virusList = new ArrayList<>();
            reply = FileChooser.showOpenDialog(this);
            selectFile = FileChooser.getSelectedFile();
            if (reply == JFileChooser.APPROVE_OPTION) {
                bestand = (selectFile.getAbsolutePath());
                bestandNaam.setText(selectFile.getName());
                try {
                    infile = new BufferedReader(new FileReader(bestand));
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(VirusGui.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    while ((line = infile.readLine()) != null) {
                        if (!line.startsWith("virus tax id")) {
                            String[] splitline = line.split("\t", -1);
                            Virus virusObject = new Virus(Integer.parseInt(splitline[0]), splitline[2].split(";")[1], Integer.parseInt(splitline[7].replaceAll("(^(\\r\\n|\\n|\\r)$)|(^(\\r\\n|\\n|\\r))|^\\s*$", "0")), splitline[8]);
                            virusList.add(virusObject);
                        }
                    }
                } catch (Exception exc) {
                    System.out.println("Er is een fout opgetreden");
                    System.out.println(exc.toString());
                }
            }
            VirusLogica.makeComboboxList(virusList);

        }
        if (event.getSource() == submitButton) {
            VirusLogica.getaskedvirusList(virusList);
            VirusLogica.getvirusbyhostList1(VirusLogica.selectedvirusList);
            VirusLogica.getvirusbyhostList2(VirusLogica.selectedvirusList);
            virus1.setText(null);
            virus2.setText(null);
            overlap.setText(null);
            if (rbID.isSelected()) {
                VirusLogica.sortHostid(VirusLogica.selectedvirushost1List, VirusLogica.selectedvirushost2List);
            }
            if (rbHost.isSelected()) {
                VirusLogica.sortAmounthost(VirusLogica.selectedvirushost1List, VirusLogica.selectedvirushost2List);
            }
            if (rbClass.isSelected()) {
                VirusLogica.sortClassification(VirusLogica.selectedvirushost1List, VirusLogica.selectedvirushost2List);
            }
            VirusLogica.createSets(VirusLogica.selectedvirushost1List, VirusLogica.selectedvirushost2List);

        }
    }

}