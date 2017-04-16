package eu.profinit.opendata.mapper;


import eu.profinit.opendata.model.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Created by livsu on 15.04.2017.
 */


@Mapper
public interface RecordMapper {

    List<Record> searchSupplier(@Param("ico") String ico, @Param("name") String name);

    List<Record> searchCustomer(@Param("ico") String ico, @Param("name") String name);
}
