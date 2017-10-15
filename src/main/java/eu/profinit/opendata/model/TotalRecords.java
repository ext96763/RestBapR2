package eu.profinit.opendata.model;

/**
 * Pojo for mapping number of all records in DB
 */
public class TotalRecords {

    private Long count;

    private TotalRecords(){}

    public TotalRecords(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TotalRecords{" +
                "count=" + count +
                '}';
    }
}