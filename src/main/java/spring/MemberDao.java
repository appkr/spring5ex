package spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class MemberDao {
    private static final String QUERY_COUNT = "SELECT count(*) FROM members";
    private static final String QUERY_SELECT_BY_ID = "SELECT * FROM members WHERE id = ?";
    private static final String QUERY_SELECT_BY_EMAIL = "SELECT * FROM members WHERE email = ?";
    private static final String QUERY_SELECT_BY_REGDATE = "SELECT * FROM members WHERE regdate >= ? AND regdate < ?";
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

    public Member selectById(Long memId) {
        List<Member> results = jdbcTemplate.query(QUERY_SELECT_BY_ID, new MemberRowMapper(), memId);
        return results.isEmpty() ? null : results.get(0);
    }

    public Member selectByEmail(String email) {
        List<Member> results = jdbcTemplate.query(QUERY_SELECT_BY_EMAIL, new MemberRowMapper(), email);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Member> selectByRegdate(LocalDateTime from, LocalDateTime to) {
        List<Member> results = jdbcTemplate.query(QUERY_SELECT_BY_REGDATE, new MemberRowMapper(), from, to);
        return results;
    }

    public List<Member> selectAll() {
        List<Member> results = jdbcTemplate.query(QUERY_SELECT_ALL, new MemberRowMapper());
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
