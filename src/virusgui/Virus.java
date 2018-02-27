package virusgui;

/**
 * @author Nicky van Bergen, made in Austria & the Netherlands
 * @version IDE 8.2 & Java 8
 * In dit bestand wordt het object virus gedefineerd en staat de bebehorende constructor en de getters en setters
 */

public class Virus implements Comparable<Virus>{
    private int virusID;
    private String classification;
    private int hostID;
    private String hostName;
    private int amountHost;

    /**
     *
     * @param virusID
     * @param classification
     * @param hostID
     * @param hostName
     */
    public Virus(int virusID, String classification, int hostID, String hostName) {
        this.virusID = virusID;
        this.classification = classification;
        this.hostID = hostID;
        this.hostName = hostName;
    }

    /**
     *
     * @param virusID
     */
    public void setVirusID(int virusID) {
        this.virusID = virusID;
    }

    /**
     *
     * @param classification
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }

    /**
     *
     * @param hostID
     */
    public void setHostID(int hostID) {
        this.hostID = hostID;
    }

    /**
     *
     * @param hostName
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     *
     * @return
     */
    public int getVirusID() {
        return virusID;
    }

    /**
     *
     * @return
     */
    public String getClassification() {
        return classification;
    }

    /**
     *
     * @return
     */
    public int getHostID() {
        return hostID;
    }

    /**
     *
     * @return
     */
    public String getHostName() {
        return hostName;
    }

    /**
     *
     * @return
     */
    public Integer getNumberHost() {
        return amountHost;
    }

    /**
     *
     * @param amountHost
     */
    public void setAmountHost(Integer amountHost) {
        this.amountHost = amountHost;
    }

    @Override
    public int compareTo(Virus o) {
        //Fini heeft geholpen bij het opzetten van deze methoden om de Lists te sorteren.
        Virus vi = (Virus) o;
        switch (sort){
            case 0:
                return vi.virusID - this.virusID;
            case 1:
                return vi.classification.compareTo(this.classification);
            case 2:
                return vi.amountHost - this.amountHost;
            default:
                return 0;      
    }      
    }   
public static int sort;      
}
    
    

