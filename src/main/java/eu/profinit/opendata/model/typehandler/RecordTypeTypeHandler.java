package eu.profinit.opendata.model.typehandler;

import eu.profinit.opendata.model.RecordType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Type handler for Record type enum.
 */

public class RecordTypeTypeHandler implements TypeHandler<RecordType> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, RecordType recordType, JdbcType jdbcType) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public RecordType getResult(ResultSet resultSet, String s) throws SQLException {
        String recordType = resultSet.getString(s);
        return recordType != null ? RecordType.valueOf(recordType.toUpperCase()) : null;
    }

    @Override
    public RecordType getResult(ResultSet resultSet, int i) throws SQLException {
        String recordType = resultSet.getString(i);
        return recordType != null ? RecordType.valueOf(recordType.toUpperCase()) : null;
    }

    @Override
    public RecordType getResult(CallableStatement callableStatement, int i) throws SQLException {
        String recordType = callableStatement.getString(i);
        return recordType != null ? RecordType.valueOf(recordType.toUpperCase()) : null;
    }
}
