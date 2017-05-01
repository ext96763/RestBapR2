package eu.profinit.opendata.model;


/**
 * Represents the assignment of an identification code to a partner made by a publishing institution, where documents
 * containing records may only specify this identification code instead of other details about the partner.
 * This is the scheme currently used by MF invoices from 2010 to 2014. First, only the partner list is downloaded and
 * PartnerListEntries are saved. When processing documents containing the invoices, partners are searched for by their
 * PartnerListEntry codes.
 */
public class PartnerListEntry {

    /** The authority that has published information about the partner */
    private Entity authority;

    /** The partner that is published by the authority under an identification code. */
    private Entity partner;

    /** The identification code used by the authority to refer to the partner. */
    private String code;

    /** The database primary key. */
    private Long partnerListEntryId;
    
    public Entity getAuthority() {
        return authority;
    }

    public void setAuthority(Entity authority) {
        this.authority = authority;
    }
    
    public Entity getPartner() {
        return partner;
    }

    public void setPartner(Entity partner) {
        this.partner = partner;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getPartnerListEntryId() {
        return partnerListEntryId;
    }

    public void setPartnerListEntryId(Long partnerListEntryId) {
        this.partnerListEntryId = partnerListEntryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartnerListEntry other = (PartnerListEntry) o;
        if(!other.getAuthority().equals(getAuthority())) return false;
        if(!other.getPartner().equals(getPartner())) return false;
        if(!other.getPartnerListEntryId().equals(getPartnerListEntryId())) return false;
        if(!other.getCode().equals(getCode())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result += 31 * result + (partnerListEntryId != null ? partnerListEntryId : 0);
        result += 31 * result + (authority != null ? authority.hashCode() : 0);
        result += 31 * result + (partner != null ? partner.hashCode() : 0);
        result += 31 * result + (code != null ? code.hashCode() : 0);

        return result;
    }

}
