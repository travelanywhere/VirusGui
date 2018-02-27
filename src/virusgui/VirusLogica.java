package virusgui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.swing.DefaultComboBoxModel;

/**
 * @author Nicky van Bergen, made in Austria & the Netherlands
 * @version IDE 8.2 & Java 8
 * In dit bestand staan alle methodes die worden aangeroepen in de action Performed.
 * Justin heeft geholpen bij de regex voor het vervangen van een lege HostID en het fixen van de scrollbar.
 */
public class VirusLogica {
 /**
 * declaratie & initalisatie van gebruikte ArrayLists, HashSets & Hashmaps
 * deze zijn static zodat ze in de andere classes gebruikt kunnen worden
 */
    static HashMap<Integer,HashSet<Virus>> hostvirusMap = new HashMap<>();
    static Set<String> classificationSet = new HashSet<String>();
    static ArrayList<String>classificationList = new ArrayList<>();
    static Set<String>hostidNameSet = new HashSet<>();
    static ArrayList<String>hostidNameList = new ArrayList<>();
    static HashSet<Virus> virusbyhostSet1 = new HashSet<>();
    static HashSet<Virus> virusbyhostSet2 = new HashSet<>();
    static ArrayList<Virus> selectedvirushost1List = new ArrayList<>();
    static ArrayList<Virus> selectedvirushost2List = new ArrayList<>();
    static Set<Integer> virus1Set = new HashSet<Integer>();
    static Set<Integer> virus2Set = new HashSet<Integer>();
    static Set<Integer> overlapSet = new HashSet<Integer>();
    static ArrayList<Integer>virus1List = new ArrayList<>();
    static ArrayList<Integer>virus2List = new ArrayList<>();
    static ArrayList<Integer>overlapList = new ArrayList<>();
    
    static void readFile(String bestand) {
        /**
     * In deze methode wordt het gekozen bestand ingelezen en wordt voor iedere regel van het bestand een object aangemaakt
     * via de constructor van de Virus Class. Hiernaast wordt er meteen een hasmap gemaakt met de HostID als Key en bijbehorende
     * virus objecten als value.
     */
      try {
                    BufferedReader infile = new BufferedReader(new FileReader(bestand));
                    String line;
                    while ((line = infile.readLine()) != null) {
                        if (!line.startsWith("virus tax id")) {
                            String[] splitline = line.split("\t", -1);
                            Virus virusObject = new Virus(Integer.parseInt(splitline[0]), splitline[2].split(";")[1], Integer.parseInt(splitline[7].replaceAll("(^(\\r\\n|\\n|\\r)$)|(^(\\r\\n|\\n|\\r))|^\\s*$", "0")), splitline[8]);
                            VirusGui.virusList.add(virusObject);
                            if(hostvirusMap.containsKey(virusObject.getHostID())){
                                hostvirusMap.get(virusObject.getHostID()).add(virusObject);}               
                            else {
                                HashSet<Virus> tempHashSet = new HashSet<>();
                                tempHashSet.add(virusObject);
                                hostvirusMap.put(virusObject.getHostID(), tempHashSet);} 
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
   
   public static void setAmounthost(ArrayList<Virus> virusList){
       /**
     * Deze methode wordt aangeroepen om in de virusclass het amount of host te vullen. Met behulp van een hashmap wordt bepaald
     * hoe vaak elke host voorkomt in de list met virus objecten. De frequentie wordt als value toegevoegd aan de hasmap.
     * wanneer de key al in de hashmap staat wordt er +1 gedaan bij de value. Vervolgens worden de frequenties toegevoegd bij de objecten.
     */
       HashMap<Integer,Integer> amounthostMap = new HashMap<>();
        for(int i=0; i<virusList.size(); i++){
            if(amounthostMap.containsKey(virusList.get(i).getHostID())){
                amounthostMap.put(virusList.get(i).getHostID(), amounthostMap.get(virusList.get(i).getHostID())+1);
            } else {
                amounthostMap.put(virusList.get(i).getHostID(), 1);
            }}
        for(Virus vi: virusList){
            if(amounthostMap.containsKey(vi.getHostID())){
                vi.setAmountHost(amounthostMap.get(vi.getHostID()));
            }}
        }
   
   public static void getvirusbyhostSets(HashMap<Integer,HashSet<Virus>> hostvirusMap){
       /**
     * Methode die na het vullen van de comboboxen en het drukken op submit checkt welke hostid gekozen is. 
     * Via de HashMap wordt de gekozen hostid ophaalt en de bijbehorende virus objecten in een HashSet zet.
     */
       String selectedHostidName = (String) VirusGui.hostid1box.getSelectedItem();
       String selectedHostidName2 = (String) VirusGui.hostid2box.getSelectedItem();
       String[] splitselected = selectedHostidName.split("\\s+");
       String[] splitselected2 = selectedHostidName2.split("\\s+");
       Integer selectedHostid = Integer.parseInt(splitselected[0]);
       Integer selectedHostid2 = Integer.parseInt(splitselected2[0]);
       virusbyhostSet1 = hostvirusMap.get(selectedHostid);
       virusbyhostSet2 = hostvirusMap.get(selectedHostid2);
       }
   
   public static void getaskedvirusSets(HashSet<Virus> selectedvirushost1Set,HashSet<Virus> selectedvirushost2Set ){
    /**
     * Methode die na het vullen van de comboboxen en het drukken op submit checkt welke classificatie gekozen is. 
     * Deze methode krijgt de set van getvirusbyhostSets en bekijkt welke virus objecten voldoen aan 
     * de classificatie en zet deze in een nieuwe set.
     */
       String selectedClassification;
       selectedClassification = (String) VirusGui.classificationbox.getSelectedItem();
       for (Virus vi: selectedvirushost1Set){
        if(selectedClassification.equals(vi.getClassification())){
            virus1Set.add(vi.getVirusID());}
        if(" No filter".equals(selectedClassification)){
           virus1Set.add(vi.getVirusID());
       }}
       for (Virus vi: selectedvirushost2Set){
        if(selectedClassification.equals(vi.getClassification())){
          virus2Set.add(vi.getVirusID());}
        if(" No filter".equals(selectedClassification)){
           virus2Set.add(vi.getVirusID());
       }}
   }
   
    public static void compareSets(Set<Integer> virus1Set, Set<Integer> virus2Set){
        /**
     * Deze methode krijgt vervolgens de Sets met virusID's die voldoen aan de filter eisen. vergelijkt de sets,
     * zet deze in een List en sorteert ze. Vervolgens worden de HostID's één voor één in de textarea's gezet.
     */
    overlapSet.addAll(virus1Set);
    overlapSet.retainAll(virus2Set);
    virus1List.addAll(virus1Set);
    virus2List.addAll(virus2Set);
    overlapList.addAll(overlapSet);
    Collections.sort(virus1List);
    Collections.sort(virus2List);
    Collections.sort(overlapList);
            for (Integer vi : virus1List) {
                VirusGui.virus1Textarea.append(vi.toString()+"\n");}
            for (Integer vi : virus2List) {
                VirusGui.virus2Textarea.append(vi.toString()+"\n");}
            for (Integer vi : overlapList) {
                VirusGui.overlapTextarea.append(vi.toString()+"\n");}
        virus1List.clear();
        virus2List.clear();
        overlapList.clear();
        virus1Set.clear();
        virus2Set.clear();
        overlapSet.clear();
    }  
    }
   


    

