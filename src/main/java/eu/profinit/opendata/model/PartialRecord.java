package eu.profinit.opendata.model;

/**
 * Pojo class for partial record mapping(short record, only name and ID)
 */

public class PartialRecord {

    private String subject;

    private Long recordId;

    public PartialRecord(){}

    public PartialRecord(String subject, Long recordId) {
        this.subject = subject;
        this.recordId = recordId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    @Override
    public String toString() {
        return "PartialRecord{" +
                "subject='" + subject + '\'' +
                ", recordId=" + recordId +
                '}';
    }
}