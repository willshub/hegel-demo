package org.hegel.app

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.PreparedStatementCreator
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.jdbc.support.KeyHolder
import org.springframework.stereotype.Repository
import org.springframework.util.ObjectUtils
import org.springframework.util.StringUtils

import javax.sql.DataSource
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by wilsonraphale on 7/12/15.
 */
@Repository
class UserRepository {

    protected final Logger log = LoggerFactory.getLogger(getClass());


    protected JdbcTemplate jdbc;

    @Autowired
    public UserRepository(@Qualifier("hegelDataSource") DataSource dataSource) {

        jdbc = new JdbcTemplate(dataSource);
    }

    public User getUser(long id) {
        return jdbc.queryForObject("SELECT * FROM hegel_user WHERE id=?", userMapper, id);
    }

    public List<User> getUsers(long[] ids) {
        String inIds = StringUtils.arrayToCommaDelimitedString(ObjectUtils.toObjectArray(ids));
        return jdbc.query("SELECT * FROM hegel_user WHERE id IN (" + inIds + ")", userMapper);
    }

    public long createUser(final String username) {

        long result = 0;
        final String SQL = "INSERT INTO hegel_user (username) "
        + " VALUES(?)";

        KeyHolder holder = new GeneratedKeyHolder();
        jdbc.update(new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                def ids = new String [1];
                ids.putAt(1, 'id')
                PreparedStatement ps = connection.prepareStatement(SQL, ids);
                ps.setString(1, username);
                return ps;
            }
        }, holder);

        Long newPersonId = holder.getKey().longValue();

        return newPersonId;

    }

    private static final RowMapper<User> userMapper = new RowMapper<User>() {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User(rs.getLong("id"), rs.getString("username"));
            return user;
        }
    };
}
