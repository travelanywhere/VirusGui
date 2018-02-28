package virusgui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * @author Nicky van Bergen, made in Austria & the Netherlands
 * @version 2 In dit bestand wordt de GUI gemaakt en de bijbehorende
 * actionPerformed
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
    JRadioButton rbHostamount;
    JRadioButton rbClass;
    JLabel sortLabel;
    static JTextArea virus1Textarea;
    static JTextArea virus2Textarea;
    JLabel virus1Label;
    JLabel virus2Label;
    JScrollPane scrollPane;
    JScrollPane scrollPane2;
    static JTextArea overlapTextarea;
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
     * In deze methoden wordt de GUI gemaakt. De JComboBoxen bevatten nog een
     * string met NULL. Hier wordt na het inlezen van het bestand pas de juiste
     * String met opties gepaaltst.
     */
    public void createGUI() {
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
        rbHostamount = new JRadioButton("Classification");
        rbClass = new JRadioButton("amount Hosts                                                                   ");

        ButtonGroup sortgroup = new ButtonGroup();
        sortgroup.add(rbID);
        sortgroup.add(rbHostamount);
        sortgroup.add(rbClass);
        window.add(rbID);
        window.add(rbHostamount);
        window.add(rbClass);

        virus1Label = new JLabel("                   VirusList 1:");
        virus2Label = new JLabel("VirusList 2:");
        virus1Textarea = new JTextArea(15, 20);
        virus2Textarea = new JTextArea(15, 20);
        scrollPane = new JScrollPane(virus1Textarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        virus1Textarea.setEditable(false);
        scrollPane2 = new JScrollPane(virus2Textarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        virus2Textarea.setEditable(false);
        window.add(virus1Label);
        window.add(scrollPane);
        window.add(virus2Label);
        window.add(scrollPane2);

        overlapLabel = new JLabel("                     overlap viruses from hosts");
        overlapTextarea = new JTextArea(15, 20);
        scrollPane3 = new JScrollPane(overlapTextarea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);;
        window.add(overlapLabel);
        window.add(scrollPane3);

        submitButton = new JButton("Submit");
        window.add(submitButton);
        submitButton.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        /**
         * In de actionPerformed wordt gekeken welke acties er door de gebruiker
         * worden uitgevoerd. Wanneer de gebruiker op de openButton klikt kan er
         * met de filechooser een bestand worden gekozen. Ook wordt de methode
         * readFile, setAmountHost en makeComboList aangeroepen. Wanneer submit
         * wordt aangeklikt methodes getvirusbyhosSets, getaskedvirusSets en
         * compareSets aangeroepen. Vervolgens wordt gecheckt welke sorteering
         * is gekozen en wordt de compare to aangeroepen met de int sort.
         */
        FileChooser = new JFileChooser();
        if (event.getSource() == openButton) {
            virusList = new ArrayList<>();
            int reply = FileChooser.showOpenDialog(this);
            File selectFile = FileChooser.getSelectedFile();
            if (reply == JFileChooser.APPROVE_OPTION) {
                String bestand = (selectFile.getAbsolutePath());
                bestandNaam.setText(selectFile.getName());
                VirusLogica.readFile(bestand);
            }
            VirusLogica.makeComboboxList(virusList);
            VirusLogica.setAmounthost(virusList);
        }
        if (event.getSource() == submitButton) {
            VirusLogica.getvirusbyhostSets(VirusLogica.hostvirusMap);
            VirusLogica.getaskedvirusSets(VirusLogica.virusbyhostSet1, VirusLogica.virusbyhostSet2);
            if (rbID.isSelected()) {
                Virus.sort = 1;
            }
            if (rbHostamount.isSelected()) {
                Virus.sort = 2;
            }
            if (rbClass.isSelected()) {
                Virus.sort = 3;
            }
            virus1Textarea.setText("");
            virus2Textarea.setText("");
            overlapTextarea.setText("");
            VirusLogica.compareSets(VirusLogica.virus1Set, VirusLogica.virus2Set);
        }
    }

}
