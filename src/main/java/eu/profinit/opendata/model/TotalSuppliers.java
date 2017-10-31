package eu.profinit.opendata.model;

/**
 * Pojo for mapping number of all records in DB
 */
public class TotalSuppliers {

    private Long totalSuppliers;

    private TotalSuppliers(){}

    public TotalSuppliers(Long totalSuppliers) {
        this.totalSuppliers = totalSuppliers;
    }

    public Long getTotalSuppliers() {
        return totalSuppliers;
    }

    public void setTotalSuppliers(Long totalSuppliers) {
        this.totalSuppliers = totalSuppliers;
    }

    @Override
    public String toString() {
        return "TotalRecords{" +
                "totalSuppliers=" + totalSuppliers +
                '}';
    }
}