package spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

public class MemberDao {
    private static final String QUERY_COUNT = "SELECT count(*) FROM members";
    private static final String QUERY_SELECT_BY_EMAIL = "SELECT * FROM members WHERE email = ?";
    private static final String QUERY_SELECT_ALL = "SELECT * FROM members";
    private static final String QUERY_INSERT = "INSERT INTO members (email, password, name, regdate) VALUES (?, ?, ?, ?)";
    private static final String QUERY_UPDATE = "UPDATE members SET name = ?, password = ? WHERE email = ?";

    private JdbcTemplate jdbcTemplate;

    public MemberDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int count() {
        return jdbcTemplate.queryForObject(QUERY_COUNT, Integer.class);
    }

    public Member selectByEmail(String email) {
        List<Member> results = jdbcTemplate.query(QUERY_SELECT_BY_EMAIL, new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Member member = new Member(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getTimestamp("regdate").toLocalDateTime()
                );
                member.setId(rs.getLong("id"));
                return member;
            }
        }, email);

        return results.isEmpty() ? null : results.get(0);
    }

    public List<Member> selectAll() {
        List<Member> results = jdbcTemplate.query(QUERY_SELECT_ALL, new RowMapper<Member>() {
            @Override
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
                Member member = new Member(
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getTimestamp("regdate").toLocalDateTime()
                );
                member.setId(rs.getLong("id"));
                return member;
            }
        });

        return results;
    }

    public void insert(Member member) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement pstmt = con.prepareStatement(QUERY_INSERT, new String[]{"id"});
                pstmt.setString(1, member.getEmail());
                pstmt.setString(2, member.getPassword());
                pstmt.setString(3, member.getName());
                pstmt.setTimestamp(4, Timestamp.valueOf(member.getRegDateTime()));
                return pstmt;
            }
        }, keyHolder);
        Number keyValue = keyHolder.getKey();
        member.setId(keyValue.longValue());
    }

    public void update(Member member) {
        jdbcTemplate.update(QUERY_UPDATE, member.getName(), member.getPassword(), member.getEmail());
    }
}
