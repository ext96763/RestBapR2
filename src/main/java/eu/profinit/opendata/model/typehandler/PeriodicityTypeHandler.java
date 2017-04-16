package eu.profinit.opendata.model.typehandler;


import eu.profinit.opendata.model.Periodicity;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by livsu on 15.04.2017.
 */
public class PeriodicityTypeHandler implements TypeHandler<Periodicity> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Periodicity periodicity, JdbcType jdbcType) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Periodicity getResult(ResultSet resultSet, String s) throws SQLException {
        String periodicity = resultSet.getString(s);
        return periodicity != null ? Periodicity.valueOf(periodicity.toUpperCase()) : null;
    }

    @Override
    public Periodicity getResult(ResultSet resultSet, int i) throws SQLException {
        String periodicity = resultSet.getString(i);
        return periodicity != null ? Periodicity.valueOf(periodicity.toUpperCase()) : null;
    }

    @Override
    public Periodicity getResult(CallableStatement callableStatement, int i) throws SQLException {
        String periodicity = callableStatement.getString(i);
        return periodicity != null ? Periodicity.valueOf(periodicity.toUpperCase()) : null;
    }
}
