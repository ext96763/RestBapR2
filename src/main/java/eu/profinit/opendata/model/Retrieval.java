package eu.profinit.opendata.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single attempt to process a data file. Contains metadata about the outcome of the attempt and all
 * Records inserted during the retrieval hold a reference to it.
 */

public class Retrieval {
    /** The time the retrieval was started */
      
    @JsonProperty
    private Timestamp date;

    /** The reason for a total failure, if there was one. */
       
    @JsonProperty
    private String failureReason;

    /** The number of records that haven't been inserted due to a non-fatal error during processing. */
       
    @JsonProperty
    private int numBadRecords;

    /** The number of records successfully inserted during processing. */
       
    @JsonProperty
    private int numRecordsInserted;

    /** Indicates whether this Retrieval has finished successfully. */
       
    @JsonProperty
    private boolean success;

    /** The appplication's primary key */
       
    @JsonProperty
    private Long retrievalId;

    /** The data instance on which the Retrieval was attempted. */
       
    @JsonProperty
    private DataInstance dataInstance;

    /** The Records inserted as a result of this Retrieval. */

    @JsonProperty
    private Collection<Record> records;

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public int getNumBadRecords() {
        return numBadRecords;
    }

    public void setNumBadRecords(int numBadRecords) {
        this.numBadRecords = numBadRecords;
    }

    public int getNumRecordsInserted() {
        return numRecordsInserted;
    }

    public void setNumRecordsInserted(int numRecordsInserted) {
        this.numRecordsInserted = numRecordsInserted;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getRetrievalId() {
        return retrievalId;
    }

    public void setRetrievalId(Long retrievalId) {
        this.retrievalId = retrievalId;
    }

    public DataInstance getDataInstance() {
        return dataInstance;
    }

    public void setDataInstance(DataInstance dataInstance) {
        this.dataInstance = dataInstance;
    }

    public Collection<Record> getRecords() {
        return records;
    }

    public void setRecords(Collection<Record> records) {
        this.records = records;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Retrieval retrieval = (Retrieval) o;

        if (numBadRecords != retrieval.numBadRecords) return false;
        if (numRecordsInserted != retrieval.numRecordsInserted) return false;
        if (!Objects.equals(retrievalId, retrieval.retrievalId)) return false;
        if (success != retrieval.success) return false;
        if (date != null ? !date.equals(retrieval.date) : retrieval.date != null) return false;
        if (failureReason != null ? !failureReason.equals(retrieval.failureReason) : retrieval.failureReason != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = date != null ? date.hashCode() : 0;
        result = 31 * result + (failureReason != null ? failureReason.hashCode() : 0);
        result = 31 * result + numBadRecords;
        result = 31 * result + numRecordsInserted;
        result = 31 * result + (success ? 1 : 0);
        result = 31 * result + retrievalId.intValue();
        return result;
    }

    @Override
    public String toString() {
        return "Retrieval{" +
                "date=" + date +
                ", failureReason='" + failureReason + '\'' +
                ", numBadRecords=" + numBadRecords +
                ", numRecordsInserted=" + numRecordsInserted +
                ", success=" + success +
                ", retrievalId=" + retrievalId +
                ", dataInstance=" + dataInstance +
                '}';
    }
}
