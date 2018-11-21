package spring;

import java.util.Collection;
import java.util.HashMap;

public class MemberDao {
    private static long nextId = 0;
    private HashMap<String, Member> map = new HashMap<>();

    public Member selectByEmail(String email) {
        return map.get(email);
    }

    public Collection<Member> selectAll() {
        return map.values();
    }

    public void insert(Member member) {
        member.setId(++nextId);
        map.put(member.getEmail(), member);
    }

    public void update(Member member) {
        map.put(member.getEmail(), member);
    }
}
