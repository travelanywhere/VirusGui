package virusgui;

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
    static Set<Integer> hostidSet = new HashSet<Integer>();
    static ArrayList<Integer>hostidList = new ArrayList<>();
    static Set<String>hostidNameSet = new HashSet<>();
    static ArrayList<String>hostidNameList = new ArrayList<>();
    static ArrayList<Integer>hostList = new ArrayList<>();
    static ArrayList<Integer>hostList2 = new ArrayList<>();
    static ArrayList<Integer>amountHostList = new ArrayList<>();
    static ArrayList<Virus> selectedvirusList = new ArrayList<>();
    static ArrayList<Virus> selectedvirushost1List = new ArrayList<>();
    static ArrayList<Virus> selectedvirushost2List = new ArrayList<>();
    static Set<Integer> virus1Set = new LinkedHashSet<Integer>();
    static Set<Integer> virus2Set = new LinkedHashSet<Integer>();
    static Set<Integer> overlapSet = new LinkedHashSet<Integer>();
    static ArrayList<Integer>virus1List = new ArrayList<>();
    static ArrayList<Integer>virus2List = new ArrayList<>();
    static String selectedHostidName;
    static String selectedHostidName2;
    static String[] splitselected;
    static String[] splitselected2;
    static Integer selectedHostid;
    static Integer selectedHostid2; 

    
   public static void makeComboboxList(ArrayList<Virus> virusList){
     /**
     * Methode die na het inlezen van het bestand alle unieke Classificaties in een lijst zet door gebruik te maken van sets
     * Dit wordt ook gedaan voor alle unieke hostID's. De lijsten worden vervolgens in de combobox gezet.
     */
       for (Virus vi: virusList){
           classificationSet.add(vi.getClassification());
           hostidSet.add(vi.getHostID());
           hostidNameSet.add(vi.getHostID()+ " ("+vi.getHostName()+")");
       }
       classificationList = new ArrayList(classificationSet);
       classificationList.add(" No filter");
       hostidList = new ArrayList(hostidSet);
       hostidNameList = new ArrayList(hostidNameSet);
       VirusGui.classificationbox.setModel(new DefaultComboBoxModel(classificationList.toArray()));
       VirusGui.hostid1box.setModel(new DefaultComboBoxModel(hostidNameList.toArray()));
       VirusGui.hostid2box.setModel(new DefaultComboBoxModel(hostidNameList.toArray()));
   }
   
   public static void getaskedvirusList(ArrayList<Virus> virusList){
    /**
     * Methode die na het vullen van de comboboxen en het drukken op submit checkt welke classificatie gekozen is 
     * en een nieuwe lijst maakt met alle objecten die voldoen aan de filter keuze. Bij No filter wordt de complete lijst behouden.
     * Maar eerst worden de Sets leeg gemaakt zodat evt elementen van een vorige opdracht worden verwijdert uit de Lijst.
     */
       selectedvirusList.clear();
       String selectedClassification;
       selectedClassification = (String) VirusGui.classificationbox.getSelectedItem();
       for (Virus vi: virusList){
       if(selectedClassification.equals(vi.getClassification())){
          selectedvirusList.add(vi);
       }
       if(" No filter".equals(selectedClassification)){
           selectedvirusList = virusList;
       }
       }}
   
   public static void getvirusbyhostList1(ArrayList<Virus> selectedvirusList){
       /**
     * Methode die na het vullen van de comboboxen en het drukken op submit checkt welke hostid gekozen is 
     * en een nieuwe lijst maakt met alle objecten die voldoen aan de filter keuze. Maar eerst worden de Sets leeg gemaakt
     * zodat evt elementen van een vorige opdracht worden verwijdert uit de Lijst.
     */
       selectedvirushost1List.clear();
       selectedHostidName = (String) VirusGui.hostid1box.getSelectedItem();
       splitselected = selectedHostidName.split("\\s+");
       selectedHostid = Integer.parseInt(splitselected[0]);
       for (Virus vi: selectedvirusList){
       if((vi.getHostID())==selectedHostid){
          selectedvirushost1List.add(vi);
       }
       }}
   
   public static void getvirusbyhostList2(ArrayList<Virus> selectedvirusList){
       /**
     * Doet hetzelfde als bovenstaande methode alleen wordt hier gekeken naar de tweede hostid die gekozen is
     */
       selectedvirushost2List.clear();
       selectedHostidName2 = (String) VirusGui.hostid2box.getSelectedItem();
       splitselected2 = selectedHostidName2.split("\\s+");
       selectedHostid2 = Integer.parseInt(splitselected2[0]);
       for (Virus vi: selectedvirusList){
       if((vi.getHostID())==selectedHostid2){
          selectedvirushost2List.add(vi);
       }
       }}
   
   public static void sortAmounthost(ArrayList<Virus> selectedvirushost1List,ArrayList<Virus> selectedvirushost2List){
       /**
     * Deze methode wordt aangeroepen wanneer er gekozen is om te sorteren op het aantal hosts. Eerst worden de Lijsten leeg gemaakt
     * zodat evt elementen van een vorige opdracht worden verwijdert uit de Lijst. Het aantal keer dat iedere host voorkomt 
     * word geteld en de frequentie wordt steeds met elkaar vergeleken om ze te sorteren. Dit wordt voor beide lijsten gedaan
     */
       hostList.clear();
       Collections.sort(selectedvirushost1List,new Comparator<Virus>(){
       @Override
       public int compare(final Virus object1, Virus object2){
       for (Virus vi: selectedvirushost1List){
       hostList.add(vi.getHostID());
       }
       Integer id1 = object1.getHostID();
       Integer id2 = object2.getHostID();
       Integer frequencie1 = Collections.frequency(hostList, id1);
       Integer frequencie2 = Collections.frequency(hostList, id2);
       object1.setNumberHost(frequencie1);
       object2.setNumberHost(frequencie2);
       int vergelijk = object1.getNumberHost().compareTo(object2.getNumberHost());
       if(vergelijk==0){
       object1.setNumberHost(0);
       object2.setNumberHost(0);
       }
       if(vergelijk<0){
       object1.setNumberHost(-1);
       object2.setNumberHost(1);
       }
       if(vergelijk>0){
       object1.setNumberHost(1);
       object2.setNumberHost(-1);
       }
       else return 0;
       return object1.getNumberHost().compareTo(object2.getNumberHost());
       }
       
   });
       hostList2.clear();
       Collections.sort(selectedvirushost1List,new Comparator<Virus>(){
       @Override
       public int compare(final Virus object1, Virus object2){
       for (Virus vi: selectedvirushost2List){
       hostList2.add(vi.getHostID());
       }
       Integer id1 = object1.getHostID();
       Integer id2 = object2.getHostID();
       Integer frequencie1 = Collections.frequency(hostList2, id1);
       Integer frequencie2 = Collections.frequency(hostList2, id2);
       object1.setNumberHost(frequencie1);
       object2.setNumberHost(frequencie2);
       int vergelijk = object1.getNumberHost().compareTo(object2.getNumberHost());
       if(vergelijk==0){
       object1.setNumberHost(0);
       object2.setNumberHost(0);
       }
       if(vergelijk<0){
       object1.setNumberHost(-1);
       object2.setNumberHost(1);
       }
       if(vergelijk>0){
       object1.setNumberHost(1);
       object2.setNumberHost(-1);
       }
       else return 0;
       return object1.getNumberHost().compareTo(object2.getNumberHost());
       }
       
   });
       }
   
    public static void sortHostid(ArrayList<Virus> selectedvirushost1List,ArrayList<Virus> selectedvirushost2List){
        /**
     * Deze methode wordt aangeroepen wanneer er wordt gekozen om te sorteren op Hostid. Beide lijsten worden vervolgens gesorteerd.
     */
        selectedvirushost1List.sort(Comparator.comparing(Virus::getHostID));
        selectedvirushost2List.sort(Comparator.comparing(Virus::getHostID));
    }
    public static void sortClassification(ArrayList<Virus> selectedvirushost1List,ArrayList<Virus> selectedvirushost2List){
        /**
     * Deze methode wordt aangeroepen wanneer er wordt gekozen om te sorteren op Classificatie. Beide lijsten worden vervolgens gesorteerd.
     */
        selectedvirushost1List.sort(Comparator.comparing(Virus::getClassification));
        selectedvirushost2List.sort(Comparator.comparing(Virus::getClassification));
    }
    public static void createSets(ArrayList<Virus> selectedvirushost1List,ArrayList<Virus> selectedvirushost2List){
        /**
     * Deze methode krijgt vervolgens de lijsten met virussen die voldoen aan de filter eisen. Eerst worden de Sets leeg gemaakt
     * zodat evt elementen van een vorige opdracht worden verwijdert uit de set. Alle bijbehorende virusID's worden 
     * aan de lijsten toegevoegd. Deze worden vervolgens omgezet tot een LinkedHashSet zodat de juiste volgerde behouden word en 
     * de overlap tussen de twee LinkedHashSets makkelijk bepaald kan worden. Vervolgens worden de tekstarea's van de GUI gevuld met de sets
     */
        virus1Set.clear();
        virus2Set.clear();
        overlapSet.clear();
        for(Virus vi: selectedvirushost1List){
        virus1List.add(vi.getVirusID());
        }
        for(Virus vi: selectedvirushost2List){
        virus2List.add(vi.getVirusID());
        }
    virus1Set = new LinkedHashSet<>(virus1List);
    virus2Set = new LinkedHashSet<>(virus2List);
    overlapSet = new LinkedHashSet<>();
    overlapSet.addAll(virus1Set);
    overlapSet.retainAll(virus2Set);
            for (Integer virusid : virus1Set) {
                VirusGui.virus1.append(virusid.toString()+"\n");
                System.out.println(virus1Set.toString());
            }
            for (Integer virusid : virus2Set) {
                VirusGui.virus2.append(virusid.toString()+"\n");
            }
            for (Integer virusid : overlapSet) {
                VirusGui.overlap.append(virusid.toString()+"\n");
            }
    }
        
    }
   


    

