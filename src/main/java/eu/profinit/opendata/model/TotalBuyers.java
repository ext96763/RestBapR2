package eu.profinit.opendata.model;

/**
 * Pojo for mapping number of all records in DB
 */
public class TotalBuyers {

    private Long totalBuyers;

    private TotalBuyers(){}

    public TotalBuyers(Long totalBuyers) {
        this.totalBuyers = totalBuyers;
    }

    public Long getTotalBuyers() {
        return totalBuyers;
    }

    public void setTotalBuyers(Long totalBuyers) {
        this.totalBuyers = totalBuyers;
    }

    @Override
    public String toString() {
        return "TotalRecords{" +
                "totalBuyers=" + totalBuyers +
                '}';
    }
}