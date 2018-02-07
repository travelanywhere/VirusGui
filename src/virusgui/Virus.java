package virusgui;

/**
 * @author Nicky van Bergen, made in Austria & the Netherlands
 * @version IDE 8.2 & Java 8
 * In dit bestand wordt het object virus gedefineerd en staat de bebehorende constructor en de getters en setters
 */
public class Virus implements Comparable{
    private int virusID;
    private String classification;
    private int hostID;
    private String hostName;
    private Integer numberHost;

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
        return numberHost;
    }

    /**
     *
     * @param numberHost
     */
    public void setNumberHost(Integer numberHost) {
        this.numberHost = numberHost;
    }

    @Override
    public int compareTo(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //staat in de VirusLogica
    }
       
}
    
    

