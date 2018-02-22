package virusgui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.swing.DefaultComboBoxModel;
import static virusgui.VirusGui.virusList;

/**
 * @author Nicky van Bergen, made in Austria & the Netherlands
 * @version IDE 8.2 & Java 8
 * Justin heeft geholpen met het sorteren van de data, regex voor het vervangen van een lege HostID en het fixen van de scrollbar.
 */
public class VirusLogica {
 /**
 * declaratie van gebruikte ArrayLists, HashSets & LinkedLists
 * deze zijn static zodat ze in de andere classes gebruikt kunnen worden
 */
    static Set<String> classificationSet = new HashSet<String>();
    static ArrayList<String>classificationList = new ArrayList<>();
    static ArrayList<Integer>hostidList = new ArrayList<>();
    static Set<String>hostidNameSet = new HashSet<>();
    static ArrayList<String>hostidNameList = new ArrayList<>();
    static ArrayList<Virus> selectedvirusList = new ArrayList<>();
    static ArrayList<Virus> selectedvirushost1List = new ArrayList<>();
    static ArrayList<Virus> selectedvirushost2List = new ArrayList<>();
    static Set<Integer> virus1Set = new LinkedHashSet<Integer>();
    static Set<Integer> virus2Set = new LinkedHashSet<Integer>();
    static Set<Integer> overlapSet = new LinkedHashSet<Integer>();
    static ArrayList<Integer>virus1List = new ArrayList<>();
    static ArrayList<Integer>virus2List = new ArrayList<>();
    static ArrayList<Integer>overlapList = new ArrayList<>();
    static String selectedHostidName;
    static String selectedHostidName2;
    static String[] splitselected;
    static String[] splitselected2;
    static Integer selectedHostid;
    static Integer selectedHostid2; 
    
    static void makeObject(String bestand) {
      try {
                    BufferedReader infile = new BufferedReader(new FileReader(bestand));
                    String line;
                    while ((line = infile.readLine()) != null) {
                        if (!line.startsWith("virus tax id")) {
                            String[] splitline = line.split("\t", -1);
                            Virus virusObject = new Virus(Integer.parseInt(splitline[0]), splitline[2].split(";")[1], Integer.parseInt(splitline[7].replaceAll("(^(\\r\\n|\\n|\\r)$)|(^(\\r\\n|\\n|\\r))|^\\s*$", "0")), splitline[8]);
                            virusList.add(virusObject);
                            
                        } 
                    }
                } catch (IOException | NumberFormatException exc) {
                    System.out.println("Er is een fout opgetreden");
                    System.out.println(exc.toString());
                }
    }

   public static void makeComboboxList(ArrayList<Virus> virusList){
     /**
     * Methode die na het inlezen van het bestand alle unieke Classificaties in een lijst zet door gebruik te maken van sets
     * Dit wordt ook gedaan voor alle unieke hostID's. De lijsten worden vervolgens in de combobox gezet.
     */
       for (Virus vi: virusList){
           classificationSet.add(vi.getClassification());
           hostidNameSet.add(vi.getHostID()+ " ("+vi.getHostName()+")");
       }
       classificationList = new ArrayList(classificationSet);
       classificationList.add(" No filter");
       hostidNameList = new ArrayList(hostidNameSet);
       VirusGui.classificationbox.setModel(new DefaultComboBoxModel(classificationList.toArray()));
       VirusGui.hostid1box.setModel(new DefaultComboBoxModel(hostidNameList.toArray()));
       VirusGui.hostid2box.setModel(new DefaultComboBoxModel(hostidNameList.toArray()));
   }
   
   public static ArrayList<Virus> getaskedvirusList(ArrayList<Virus> virusList){
    /**
     * Methode die na het vullen van de comboboxen en het drukken op submit checkt welke classificatie gekozen is 
     * en een nieuwe lijst maakt met alle objecten die voldoen aan de filter keuze. Bij No filter wordt de complete lijst behouden.
     * Maar eerst word de List leeg gemaakt zodat evt elementen van een vorige opdracht worden verwijdert uit de Lijst.
     */
       //selectedvirusList.clear();
       String selectedClassification;
       selectedClassification = (String) VirusGui.classificationbox.getSelectedItem();
       for (Virus vi: virusList){
       if(selectedClassification.equals(vi.getClassification())){
          selectedvirusList.add(vi);
       }
       if(" No filter".equals(selectedClassification)){
           selectedvirusList = virusList;
       }
       }
       return selectedvirusList;
   }
   
   public static void getvirusbyhostLists(ArrayList<Virus> selectedvirusList){
       /**
     * Methode die na het vullen van de comboboxen en het drukken op submit checkt welke hostid gekozen is 
     * en een nieuwe lijst maakt met alle objecten die voldoen aan de filter keuze. Maar eerst worden de Lists leeg gemaakt
     * zodat evt elementen van een vorige opdracht worden verwijdert uit de Lijst.
     */
       selectedvirushost1List.clear();
       selectedvirushost2List.clear();
       selectedHostidName = (String) VirusGui.hostid1box.getSelectedItem();
       selectedHostidName2 = (String) VirusGui.hostid2box.getSelectedItem();
       splitselected = selectedHostidName.split("\\s+");
       splitselected2 = selectedHostidName2.split("\\s+");
       selectedHostid = Integer.parseInt(splitselected[0]);
       selectedHostid2 = Integer.parseInt(splitselected2[0]);
       for (Virus vi: selectedvirusList){
       if((vi.getHostID())==selectedHostid){
          selectedvirushost1List.add(vi);
       }
       if((vi.getHostID())==selectedHostid2){
          selectedvirushost2List.add(vi);
       }
       }}
   
   public static void setAmounthost(ArrayList<Virus> virusList){
       /**
     * Deze methode wordt aangeroepen wanneer er gekozen is om te sorteren op het aantal hosts. Eerst worden de Lijsten leeg gemaakt
     * zodat evt elementen van een vorige opdracht worden verwijdert uit de Lijst. Het aantal keer dat iedere host voorkomt 
     * word geteld en de frequentie wordt steeds met elkaar vergeleken om ze te sorteren. Dit wordt voor beide lijsten gedaan
     */
       HashMap<Integer,Integer> amounthostMap = new HashMap<Integer,Integer>();
        for(int i=0; i<virusList.size(); i++){
            if(amounthostMap.containsKey(virusList.get(i).getHostID())){
                amounthostMap.put(virusList.get(i).getHostID(), +1);
            } else {
                amounthostMap.put(virusList.get(i).getHostID(), 1);
            }}
        for(Virus vi: virusList){
        if(amounthostMap.containsKey(vi.getHostID())){
        vi.setNumberHost(amounthostMap.get(vi.getHostID()));
        }}
       }
   
    public static void createSets(ArrayList<Virus> selectedvirushost1List,ArrayList<Virus> selectedvirushost2List){
        /**
     * Deze methode krijgt vervolgens de lijsten met virussen die voldoen aan de filter eisen. Eerst worden de Sets leeg gemaakt
     * zodat evt elementen van een vorige opdracht worden verwijdert uit de set. Alle bijbehorende virusID's worden 
     * aan de lijsten toegevoegd. Deze worden vervolgens omgezet tot een LinkedHashSet zodat de juiste volgerde behouden word en 
     * de overlap tussen de twee LinkedHashSets makkelijk bepaald kan worden. Vervolgens worden de tekstarea's van de GUI gevuld met de sets
     */
        for(Virus vi: selectedvirushost1List){
        //virus1List.add(vi.getVirusID());
        virus1Set.add(vi.getVirusID());
        }
        for(Virus vi: selectedvirushost2List){
        //virus2List.add(vi.getVirusID());
        virus2Set.add(vi.getVirusID());
        }
    //virus1Set = new LinkedHashSet<>(virus1List);
    //virus2Set = new LinkedHashSet<>(virus2List);
    overlapSet.addAll(virus1Set);
    overlapSet.retainAll(virus2Set);
    virus1List.addAll(virus1Set);
    virus2List.addAll(virus2Set);
    overlapList.addAll(overlapSet);
    Collections.sort(virus1List);
    Collections.sort(virus2List);
    Collections.sort(overlapList);
            for (Integer virusid : virus1List) {
                VirusGui.virus1Textarea.append(virusid.toString()+"\n");
            }
            for (Integer virusid : virus2List) {
                VirusGui.virus2Textarea.append(virusid.toString()+"\n");
            }
            for (Integer virusid : overlapList) {
                VirusGui.overlapTextarea.append(virusid.toString()+"\n");
            }
        virus1List.clear();
        virus2List.clear();
        overlapList.clear();
        virus1Set.clear();
        virus2Set.clear();
        overlapSet.clear();
    }


        
    }
   


    

