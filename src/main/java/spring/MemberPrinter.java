package spring;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;

public class MemberPrinter {
    private DateTimeFormatter dateTimeFormatter;

    public MemberPrinter() {
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
    }

    public void print(Member member) {
        if (dateTimeFormatter == null) {
            System.out.printf("회원정보: 아이디=%d, 이메일=%s, 이름=%s, 등록일=%tF\n",
                member.getId(), member.getEmail(), member.getName(), member.getRegDateTime());
        } else {
            System.out.printf("회원정보: 아이디=%d, 이메일=%s, 이름=%s, 등록일=%s\n",
                member.getId(), member.getEmail(), member.getName(),
                dateTimeFormatter.format(member.getRegDateTime()));
        }
    }

    @Autowired(required = false)
    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter) {
        // 필드가 없어도 작동하도록 로직을 구현했음에도 불구하고
        // @Autowired(required = false)를 선언하지 않으면 Bean 객체를 찾을 수 없어서 오류 발생함.
        this.dateTimeFormatter = dateTimeFormatter;
    }
}
