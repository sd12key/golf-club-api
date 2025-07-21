package org.alvio.golfnode.rest.member;

import jakarta.validation.Valid;
import org.alvio.golfnode.dto.MemberDTO;
import org.alvio.golfnode.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/members")
    public ResponseEntity<?> getAllMembers(
            @RequestParam(name = "show-tournaments", defaultValue = "false") boolean showTournaments
    ) {
        List<MemberDTO> dtos = memberService.getAllMembers(showTournaments).stream()
                .map(m -> MemberMapper.toMemberDTO(m, showTournaments))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/member/{id}")
    public ResponseEntity<?> getMemberById(
            @PathVariable Long id,
            @RequestParam(name = "show-tournaments", defaultValue = "false") boolean showTournaments
    ) {
        Member member = memberService.getMemberById(id, showTournaments);
        return ResponseEntity.ok(MemberMapper.toMemberDTO(member, showTournaments));
    }

    @GetMapping("/member-search")
    public ResponseEntity<?> searchMembers(
            @RequestParam Map<String, String> allParams,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "billing-duration", required = false) String billingDuration,
            @RequestParam(name = "phone-number", required = false) String phoneNumber,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "membership-start-date", required = false) LocalDate membershipStartDate,
            @RequestParam(name = "tournament-start-date", required = false) LocalDate tournamentStartDate,
            @RequestParam(name = "show-tournaments", defaultValue = "false") boolean showTournaments
    ) {
        List<String> allowedParams = List.of(
                "name", "billing-duration", "phone-number",
                "email", "membership-start-date", "tournament-start-date", "show-tournaments"
        );

        List<String> unknownParams = allParams.keySet().stream()
                .filter(p -> !allowedParams.contains(p))
                .toList();

        if (!unknownParams.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Unknown parameter(s): " + String.join(", ", unknownParams)
            ));
        }

        int count = 0;
        if (name != null) count++;
        if (billingDuration != null) count++;
        if (phoneNumber != null) count++;
        if (email != null) count++;
        if (membershipStartDate != null) count++;
        if (tournamentStartDate != null) count++;

        if (count > 1) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Only one search parameter can be used at a time."
            ));
        }

        List<MemberDTO> dtos = memberService
                .searchMembers(name, billingDuration, phoneNumber, email, membershipStartDate, tournamentStartDate, showTournaments)
                .stream()
                .map(m -> MemberMapper.toMemberDTO(m, showTournaments))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/member")
    public ResponseEntity<?> addMember(@Valid @RequestBody Member member) {
        Member created = memberService.addMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(MemberMapper.toMemberDTO(created));
    }

    @PostMapping("/members")
    public ResponseEntity<?> addMembers(@Valid @RequestBody List<Member> members) {
        List<MemberDTO> dtos = memberService.addMembers(members).stream()
                .map(MemberMapper::toMemberDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }

    @PutMapping("/member/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @Valid @RequestBody Member member) {
        Member updated = memberService.updateMember(id, member);
        return ResponseEntity.ok(MemberMapper.toMemberDTO(updated));
    }

    @DeleteMapping("/member/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
