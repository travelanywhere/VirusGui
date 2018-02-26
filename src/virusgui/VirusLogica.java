package virusgui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.swing.DefaultComboBoxModel;

/**
 * @author Nicky van Bergen, made in Austria & the Netherlands
 * @version IDE 8.2 & Java 8
 * Justin heeft geholpen met het sorteren van de data, regex voor het vervangen van een lege HostID en het fixen van de scrollbar.
 */
public class VirusLogica {
 /**
 * declaratie van gebruikte ArrayLists, HashSets & Hashmaps
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
    static Set<Virus> virus1Set = new HashSet<Virus>();
    static Set<Virus> virus2Set = new HashSet<Virus>();
    static Set<Virus> overlapSet = new HashSet<Virus>();
    static ArrayList<Virus>virus1List = new ArrayList<>();
    static ArrayList<Virus>virus2List = new ArrayList<>();
    static ArrayList<Virus>overlapList = new ArrayList<>();
    static String selectedHostidName;
    static String selectedHostidName2;
    static String[] splitselected;
    static String[] splitselected2;
    static Integer selectedHostid;
    static Integer selectedHostid2; 
    
    static void makeObject(String bestand) {
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
                                hostvirusMap.get(virusObject.getHostID()).add(virusObject);
                            }               
                            else {
                                HashSet<Virus> tempHashSet = new HashSet<>();
                                tempHashSet.add(virusObject);
                                hostvirusMap.put(virusObject.getHostID(), tempHashSet);
                            } 
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
        vi.setNumberHost(amounthostMap.get(vi.getHostID()));
        }}
       }
   
   public static void getvirusbyhostSets(HashMap<Integer,HashSet<Virus>> hostvirusMap){
       /**
     * Methode die na het vullen van de comboboxen en het drukken op submit checkt welke hostid gekozen is 
     * en de via een HashMap de gekozen hostid ophaalt en de bijbehorende virus objecten in een HashSet zet.
     */
       virusbyhostSet1.clear();
       virusbyhostSet2.clear();
       selectedHostidName = (String) VirusGui.hostid1box.getSelectedItem();
       selectedHostidName2 = (String) VirusGui.hostid2box.getSelectedItem();
       splitselected = selectedHostidName.split("\\s+");
       splitselected2 = selectedHostidName2.split("\\s+");
       selectedHostid = Integer.parseInt(splitselected[0]);
       selectedHostid2 = Integer.parseInt(splitselected2[0]);
       virusbyhostSet1 = hostvirusMap.get(selectedHostid);
       virusbyhostSet2 = hostvirusMap.get(selectedHostid2);    
       }
   
   public static void getaskedvirusLists(HashSet<Virus> selectedvirushost1Set,HashSet<Virus> selectedvirushost2Set ){
    /**
     * Methode die na het vullen van de comboboxen en het drukken op submit checkt welke classificatie gekozen is 
     * en de set van getvirusbyhostList krijgt en deze virus objecten bekijkt of ze voldoen aan de classificatie.
     */
       String selectedClassification;
       selectedClassification = (String) VirusGui.classificationbox.getSelectedItem();
       
       for (Virus vi: selectedvirushost1Set){
        if(selectedClassification.equals(vi.getClassification())){
            virus1Set.add(vi);}
        if(" No filter".equals(selectedClassification)){
           virus1Set.addAll(selectedvirushost1Set);
       }}
       for (Virus vi: selectedvirushost2Set){
        if(selectedClassification.equals(vi.getClassification())){
          virus2Set.add(vi);}
        if(" No filter".equals(selectedClassification)){
           virus2Set.addAll(selectedvirushost2Set);
       }}
   }
   
    public static void createSets(Set<Virus> virus1Set, Set<Virus> virus2Set){
        /**
     * Deze methode krijgt vervolgens de Sets met virussen objecten die voldoen aan de filter eisen. vergelijkt de sets
     * zet deze in een lijst en sorteert ze. Vervolgens worden deze lijsten in de textarea's gezet.
     */
    overlapSet.addAll(virus1Set);
    overlapSet.retainAll(virus2Set);
    virus1List.addAll(virus1Set);
    virus2List.addAll(virus2Set);
    overlapList.addAll(overlapSet);
    Collections.sort(virus1List);
    Collections.sort(virus2List);
    Collections.sort(overlapList);
            for (Virus vi : virus1List) {
                Integer virusid = vi.getVirusID();
                VirusGui.virus1Textarea.append(virusid.toString()+"\n");
            }
            for (Virus vi : virus2List) {
                Integer virusid = vi.getVirusID();
                VirusGui.virus2Textarea.append(virusid.toString()+"\n");
            }
            for (Virus vi : overlapList) {
                Integer virusid = vi.getVirusID();
                VirusGui.virus2Textarea.append(virusid.toString()+"\n");
            }
        virus1List.clear();
        virus2List.clear();
        overlapList.clear();
        virus1Set.clear();
        virus2Set.clear();
        overlapSet.clear();
    }


        
    }
   


    

