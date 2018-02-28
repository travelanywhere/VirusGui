package virusgui;

/**
 * @author Nicky van Bergen, made in Austria & the Netherlands
 * @version 2 
 * In dit bestand wordt het object Virus gedefineerd en staat de bebehorende constructor en de getters en setters
 * Jonathan heeft er voor gezorgd dat de objecten met elkaar vergeleken kunnen worden in de HashSet
 */
public class Virus implements Comparable<Virus> {

    private int virusID;
    private String classification;
    private int hostID;
    private String hostName;
    private int amountHost;

    /**
     * variable geeft aan welke case gebruikt moet worden voor het sorteren
     */
    public static int sort;

    /**
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
     * @param virusID
     */
    public void setVirusID(int virusID) {
        this.virusID = virusID;
    }

    /**
     * @param classification
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }

    /**
     * @param hostID
     */
    public void setHostID(int hostID) {
        this.hostID = hostID;
    }

    /**
     * @param hostName
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return het virus ID
     */
    public int getVirusID() {
        return virusID;
    }

    /**
     * @return de classificatie
     */
    public String getClassification() {
        return classification;
    }

    /**
     * @return de host ID
     */
    public int getHostID() {
        return hostID;
    }

    /**
     * @return de name van de host
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @return de frequentie dat de hostid voorkomt
     */
    public Integer getAmountHost() {
        return amountHost;
    }

    /**
     * @param amountHost
     */
    public void setAmountHost(Integer amountHost) {
        this.amountHost = amountHost;
    }

    @Override
    public int compareTo(Virus o) {
        Virus vi = (Virus) o;
        switch (sort) {
            case 1:
                if (vi.virusID > this.virusID) {
                    return -1;
                }
                if (vi.virusID < this.virusID) {
                    return 1;
                }
                if (vi.virusID == this.virusID) {
                    return 0;
                }
            case 2:
                return vi.classification.compareTo(this.classification);
            case 3:
                if (vi.amountHost > this.amountHost) {
                    return -1;
                }
                if (vi.amountHost < this.amountHost) {
                    return 1;
                }
                if (vi.amountHost == this.amountHost) {
                    return 0;
                }
            default:
                return 0;
        }
    }
    
    @Override   // met hulp van Jonathan
    public int hashCode() {
        return virusID;
    }
    
    @Override   // met hulp van Jonathan
    public boolean equals(Object virus) {
        if (virus instanceof Virus) {
            Virus v = (Virus) virus;
            return virusID == v.virusID;
        }
        return virus.equals(this);
    }
}
