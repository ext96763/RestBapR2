package eu.profinit.opendata.model.typehandler;

import eu.profinit.opendata.model.EntityType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EntityTypeTypeHandler implements TypeHandler<EntityType> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, EntityType entityType, JdbcType jdbcType) throws SQLException {
         throw new UnsupportedOperationException();
    }

    @Override
    public EntityType getResult(ResultSet resultSet, String s) throws SQLException {
        String entityType = resultSet.getString(s);
        return entityType != null ? EntityType.valueOf(entityType.toUpperCase()) : null;
    }

    @Override
    public EntityType getResult(ResultSet resultSet, int i) throws SQLException {
        String entityType = resultSet.getString(i);
        return entityType != null ? EntityType.valueOf(entityType.toUpperCase()) : null;
    }

    @Override
    public EntityType getResult(CallableStatement callableStatement, int i) throws SQLException {
        String entityType = callableStatement.getString(i);
        return entityType != null ? EntityType.valueOf(entityType.toUpperCase()) : null;
    }

}
