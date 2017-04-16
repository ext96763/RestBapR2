package eu.profinit.opendata.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single published data file. Could be an XLS file, a link to generate one or a dummy container for
 * metadata. A single DataSource can have multiple DataInstances. Some DataInstances are be generated automatically by
 * Handlers, some are used perpetually, others need to be inserted periodically via SQL.
 * Refer to the data catalogue for details about individual sources.
 * @see DataSource
 */

public class DataInstance {

    /** "xls" or "xlsx" depending on the data file format*/
    
    @JsonProperty
    private String format;

    /** The time of the last <em>successful</em> processing of this DataInstance */
   
    @JsonProperty
    private Timestamp lastProcessedDate;

    /** URL to be used to retrieve the actual XLS file */
    
    @JsonProperty
    private String url;

    /** Application identifier */
  
    @JsonProperty
    private Long dataInstanceId;

    /** The parent DataSource */
    
    @JsonProperty
    private DataSource dataSource;

    /** Retrievals performed on this DataInstance */
    
    private Collection<Retrieval> retrievals;

    /** How often this DataInstance can be expected to contain new data. This should be APERIODIC unless the exact
     * same data file at the exact same URL can be periodically reused. */
    
    @JsonProperty
    private Periodicity periodicity;

    /** The date after which this DataInstance should no longer be processed. */
    
    @JsonProperty
    private Date expires;

    /** Indicates whether the rows that have already been processed previously can be skipped in any new retrieval.
     * Should be false unless new records are only ever added at the end of this data file. */
    
    @JsonProperty
    private Boolean incremental = true;

    /** The row at which the last retrieval has ended. Only applicable if incremental is true. */
     
    @JsonProperty
    private Integer lastProcessedRow;

    /** The authority's own identifier for this DataInstance. Used for identifying results returned by an API */
     
    @JsonProperty
    private String authorityId;

    /** Description of the data file's contents */
     
    @JsonProperty
    private String description;

    /** Path to the mapping file that should be used when processing this DataInstance.*/
     
    @JsonProperty
    private String mappingFile;

     

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

     

    public String getMappingFile() {
        return mappingFile;
    }

    public void setMappingFile(String mappingFile) {
        this.mappingFile = mappingFile;
    }

     

    public String getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(String authorityId) {
        this.authorityId = authorityId;
    }

     

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

     

    public Timestamp getLastProcessedDate() {
        return lastProcessedDate;
    }

    public void setLastProcessedDate(Timestamp last_processed_date) {
        this.lastProcessedDate = last_processed_date;
    }

     

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }


    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Integer getLastProcessedRow() {
        return lastProcessedRow;
    }

    public void setLastProcessedRow(Integer lastProcessedRow) {
        this.lastProcessedRow = lastProcessedRow;
    }

    public Long getDataInstanceId() {
        return dataInstanceId;
    }

    public void setDataInstanceId(Long dataInstanceId) {
        this.dataInstanceId = dataInstanceId;
    }


    public boolean isIncremental() {
        return incremental;
    }

    public void setIncremental(boolean incremental) {
        this.incremental = incremental;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataInstance that = (DataInstance) o;

        if (!Objects.equals(dataInstanceId, that.dataInstanceId)) return false;
        if (periodicity != null ? !periodicity.equals(that.periodicity) : that.periodicity != null) return false;
        if (lastProcessedDate != null ?
                !lastProcessedDate.equals(that.lastProcessedDate) : that.lastProcessedDate != null) return false;
        if (format != null ? !format.equals(that.format) : that.format != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (incremental != ((DataInstance) o).isIncremental()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = format != null ? format.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + dataInstanceId.intValue();
        result = 31 * result + periodicity.hashCode();
        result = 31 * result + lastProcessedDate.hashCode();
        result = 31 * result + (incremental ? 1 : 0);
        return result;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Collection<Retrieval> getRetrievals() {
        return retrievals;
    }

    public void setRetrievals(Collection<Retrieval> retrievals) {
        this.retrievals = retrievals;
    }

    /**
     * Sets the expiry date of this DataInstance to the current date.
     */
    public void expire() {
        if(!hasExpired()) {
            setExpires(new Date(System.currentTimeMillis()));
        }
    }

    /**
     * @return True if this DataInstance's expiry date is in the past.
     */
    public boolean hasExpired() {
        return getExpires() != null
                && getExpires().after(new java.util.Date(System.currentTimeMillis() - Duration.ofDays(1).toMillis()));
    }

    @Override
    public String toString() {
        return "DataInstance{" +
                "format='" + format + '\'' +
                ", lastProcessedDate=" + lastProcessedDate +
                ", url='" + url + '\'' +
                ", dataInstanceId=" + dataInstanceId +
                ", dataSource=" + dataSource +
                ", periodicity=" + periodicity +
                ", expires=" + expires +
                ", incremental=" + incremental +
                ", lastProcessedRow=" + lastProcessedRow +
                ", authorityId='" + authorityId + '\'' +
                ", description='" + description + '\'' +
                ", mappingFile='" + mappingFile + '\'' +
                '}';
    }
}
