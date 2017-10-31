package eu.profinit.opendata.mapper;


import eu.profinit.opendata.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

/**
 * Interface to mybatis. Passing parameters to mybatis.
 */

@Mapper
public interface RecordMapper {

    List<PartialRecord> searchTendersByNameOrDateOrVolumeShortDetail(@Param("name") String name, @Param("dateFrom")Date dateCreated, @Param("dateTo")Date dueDate, @Param("volumeFrom") Double volumeFrom, @Param("volumeTo")Double volumeTo);

    List<Record> searchTenderByIdFullRecord(@Param("id") Long id);

    List<PartialRecord> searchSuppliersByNameOrIcoShortDetail(@Param("ico") String ico, @Param("name") String name);

    List<Record> searchSupplierByIdFullDetail(@Param("id") Long id);

    List<PartialRecord> searchBuyersByNameOrIcoShortDetail(@Param("ico") String ico, @Param("name") String name);

    List<Record> searchBuyerByIdFullDetail(@Param("id") Long id);

    List<PartialRecord> searchByNamePartialRecord(@Param("name") String name);

    List<Record> searchByIdFullRecord(@Param("id") Long id);

    List<TotalRecords> countAllRecords();

    List<TotalBuyers> countAllBuyers();

    List<TotalSuppliers> countAllSuppliers();

    List<Retrieval> findLastDate();
}
