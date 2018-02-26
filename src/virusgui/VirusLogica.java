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
 * declaratie van gebruikte ArrayLists, HashSets & LinkedLists
 * deze zijn static zodat ze in de andere classes gebruikt kunnen worden
 */
    static Set<String> classificationSet = new HashSet<String>();
    static ArrayList<String>classificationList = new ArrayList<>();
    static Set<String>hostidNameSet = new HashSet<>();
    static ArrayList<String>hostidNameList = new ArrayList<>();
    static ArrayList<Virus> selectedvirusList = new ArrayList<>();
    static HashSet<Virus> selectedvirushost1Set = new HashSet<>();
    static HashSet<Virus> selectedvirushost2Set = new HashSet<>();
    static ArrayList<Virus> selectedvirushost1List = new ArrayList<>();
    static ArrayList<Virus> selectedvirushost2List = new ArrayList<>();
    static Set<Integer> virus1Set = new LinkedHashSet<Integer>();
    static Set<Integer> virus2Set = new LinkedHashSet<Integer>();
    static Set<Integer> overlapSet = new LinkedHashSet<Integer>();
    static ArrayList<Integer>virus1List = new ArrayList<>();
    static ArrayList<Integer>virus2List = new ArrayList<>();
    static ArrayList<Integer>overlapList = new ArrayList<>();
    static HashMap<Integer,HashSet<Virus>> hostvirusMap = new HashMap<>();
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
     * wanneer de key al in de hashmap staat wordt er +1 gedaan bij de value.
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
       selectedvirushost1Set.clear();
       selectedvirushost2Set.clear();
       selectedHostidName = (String) VirusGui.hostid1box.getSelectedItem();
       selectedHostidName2 = (String) VirusGui.hostid2box.getSelectedItem();
       splitselected = selectedHostidName.split("\\s+");
       splitselected2 = selectedHostidName2.split("\\s+");
       selectedHostid = Integer.parseInt(splitselected[0]);
       selectedHostid2 = Integer.parseInt(splitselected2[0]);
       selectedvirushost1Set = hostvirusMap.get(selectedHostid);
       selectedvirushost1List = new ArrayList<>(selectedvirushost1Set);
       selectedvirushost2Set = hostvirusMap.get(selectedHostid2);
       selectedvirushost2List = new ArrayList<>(selectedvirushost2Set);
       //for (Virus vi: selectedvirusList){
//       if((vi.getHostID())==selectedHostid){
//          selectedvirushost1Set.add(vi);
//       }
//       if((vi.getHostID())==selectedHostid2){
//          selectedvirushost2Set.add(vi);
       
       }
   
    public static void createSets(ArrayList<Virus> selectedvirushost1List,ArrayList<Virus> selectedvirushost2List){
        /**
     * Deze methode krijgt vervolgens de lijsten met virussen die voldoen aan de filter eisen. Eerst worden de Sets leeg gemaakt
     * zodat evt elementen van een vorige opdracht worden verwijdert uit de set. Alle bijbehorende virusID's worden 
     * aan de lijsten toegevoegd. Deze worden vervolgens omgezet tot een LinkedHashSet zodat de juiste volgerde behouden word en 
     * de overlap tussen de twee LinkedHashSets makkelijk bepaald kan worden. Vervolgens worden de tekstarea's van de GUI gevuld met de sets
     */
        for(Virus vi: selectedvirushost1List){
        virus1Set.add(vi.getVirusID());
        }
        for(Virus vi: selectedvirushost2List){
        virus2Set.add(vi.getVirusID());
        }
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
   


    

