package eu.profinit.opendata.model.typehandler;

import eu.profinit.opendata.model.AuthorityRole;
import eu.profinit.opendata.model.EntityType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by livsu on 15.04.2017.
 */
public class AuthorityRoleTypeHandler implements TypeHandler<AuthorityRole> {

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, AuthorityRole authorityRole, JdbcType jdbcType) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AuthorityRole getResult(ResultSet resultSet, String s) throws SQLException {
        String authorityRole = resultSet.getString(s);
        return authorityRole != null ? AuthorityRole.valueOf(authorityRole.toUpperCase()) : null;
    }

    @Override
    public AuthorityRole getResult(ResultSet resultSet, int i) throws SQLException {
        String authorityRole = resultSet.getString(i);
        return authorityRole != null ? AuthorityRole.valueOf(authorityRole.toUpperCase()) : null;
    }

    @Override
    public AuthorityRole getResult(CallableStatement callableStatement, int i) throws SQLException {
        String authorityRole = callableStatement.getString(i);
        return authorityRole != null ? AuthorityRole.valueOf(authorityRole.toUpperCase()) : null;
    }
}
