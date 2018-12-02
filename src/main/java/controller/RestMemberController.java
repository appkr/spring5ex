package controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import spring.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class RestMemberController {
    private MemberDao memberDao;
    private MemberRegisterService registerService;

    @GetMapping("/api/members")
    public List<Member> members() {
        return memberDao.selectAll();
    }

    @GetMapping("/api/members/{id}")
    public ResponseEntity<Object> member(@PathVariable Long id) throws IOException {
        Member member = memberDao.selectById(id);
        if (member == null) {
            throw new MemberNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(member);
    }

    @PostMapping("/api/members")
    public ResponseEntity<Object> newMember(@RequestBody @Valid RegisterRequest regReq, Errors errors) throws IOException {
        if (errors.hasErrors()) {
            String errorCode = errors.getAllErrors()
                .stream()
                .map(error -> error.getCodes()[0])
                .collect(Collectors.joining(","));

            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("errorCodes = " + errorCode));
        }

        try {
            Long newMemberId = registerService.regist(regReq);
            URI uri = URI.create("/api/members/" + newMemberId);
            return ResponseEntity.created(uri).build();
        } catch (DuplicateMemberException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    public void setMemberDao(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void setRegisterService(MemberRegisterService registerService) {
        this.registerService = registerService;
    }
}
