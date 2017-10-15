package eu.profinit.opendata.mapper;


import eu.profinit.opendata.model.PartialRecord;
import eu.profinit.opendata.model.Record;
import eu.profinit.opendata.model.Retrieval;
import eu.profinit.opendata.model.TotalRecords;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

/**
 * Interface to mybatis. Passing parameters to mybatis.
 */

@Mapper
public interface RecordMapper {

    //TODO dokoncit dotazy v mapperu podle toho co je zde. Rozhoodnout jak to bude z detailem a jak s fullrecordem

    List<PartialRecord> searchTendersByNameOrDateOrVolumeShortDetail(@Param("name") String name, @Param("dateFrom")Date dateCreated, @Param("dateTo")Date dueDate, @Param("volumeFrom") Double volumeFrom, @Param("volumeTo")Double volumeTo);

    List<Record> searchTenderByIdFulleRecord(@Param("id") Long id);

    List<PartialRecord> searchSuppliersByNameOrIcoShortDetail(@Param("ico") String ico, @Param("name") String name);

    List<Record> searchSupplierByIdFullDetail(@Param("id") Long id);

    List<PartialRecord> searchCustomersByNameOrIcoShortDetail(@Param("ico") String ico, @Param("name") String name);

    List<Record> searchCustomerByIdFullDetail(@Param("id") Long id);

    List<PartialRecord> searchByNamePartialRecord(@Param("name") String name);

    List<Record> searchByIdFullRecord(@Param("id") Long id);

    List<TotalRecords> countAllRecords();

    List<Retrieval> findLastDate();
}
