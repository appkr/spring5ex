package spring;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberRowMapper implements RowMapper<Member> {
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
}
