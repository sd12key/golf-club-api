package org.alvio.golfnode.rest.member;

import org.alvio.golfnode.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> getAllMembers(boolean showTournaments) {
        return showTournaments ? memberRepository.findAllBy() : memberRepository.findAll();
    }

    public Member getMemberById(Long id, boolean showTournaments) {
        return (showTournaments
                ? memberRepository.findWithTournamentsById(id)
                : memberRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("Member with ID " + id + " not found."));
    }

    public List<Member> searchMembers(
            String name,
            String billingDuration,
            String phoneNumber,
            String email,
            LocalDate membershipStartDate,
            LocalDate tournamentStartDate,
            boolean showTournaments
    ) {
        if (tournamentStartDate != null) {
            return memberRepository.findByTournamentStartDate(tournamentStartDate);
        }
        if (name != null) {
            return showTournaments
                    ? memberRepository.findAllByNameContainingIgnoreCase(name)
                    : memberRepository.findByNameContainingIgnoreCase(name);
        }
        if (billingDuration != null) {
            return showTournaments
                    ? memberRepository.findAllByBillingDurationIgnoreCase(billingDuration)
                    : memberRepository.findByBillingDurationIgnoreCase(billingDuration);
        }
        if (phoneNumber != null) {
            return showTournaments
                    ? memberRepository.findAllByPhoneNumberContaining(phoneNumber)
                    : memberRepository.findByPhoneNumberContaining(phoneNumber);
        }
        if (email != null) {
            return showTournaments
                    ? memberRepository.findAllByEmailContainingIgnoreCase(email)
                    : memberRepository.findByEmailContainingIgnoreCase(email);
        }
        if (membershipStartDate != null) {
            return showTournaments
                    ? memberRepository.findAllByStartDateGreaterThanEqual(membershipStartDate)
                    : memberRepository.findByStartDateGreaterThanEqual(membershipStartDate);
        }
        return showTournaments ? memberRepository.findAllBy() : memberRepository.findAll();
    }

    public Member addMember(Member member) {
        if (member.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided when creating a new record.");
        }

        if (memberRepository.existsByEmailIgnoreCase(member.getEmail())) {
            throw new ConflictException("A member with the same email already exists.");
        }

        if (memberRepository.existsByPhoneNumber(member.getPhoneNumber())) {
            throw new ConflictException("A member with the same phone number already exists.");
        }

        return memberRepository.save(member);
    }

    public List<Member> addMembers(List<Member> members) {
        for (Member m : members) {
            if (m.getId() != null) {
                throw new IllegalArgumentException("ID must not be provided when creating a new record.");
            }
            if (memberRepository.existsByEmailIgnoreCase(m.getEmail())) {
                throw new ConflictException("Duplicate email: " + m.getEmail());
            }
            if (memberRepository.existsByPhoneNumber(m.getPhoneNumber())) {
                throw new ConflictException("Duplicate phone number: " + m.getPhoneNumber());
            }
        }
        return memberRepository.saveAll(members);
    }

    public Member updateMember(Long id, Member updated) {
        if (updated.getId() != null && !updated.getId().equals(id)) {
            throw new IllegalArgumentException("Payload ID must match path variable or be omitted.");
        }

        Member existing = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with ID " + id + " not found."));

        if (!existing.getEmail().equalsIgnoreCase(updated.getEmail()) &&
                memberRepository.existsByEmailIgnoreCase(updated.getEmail())) {
            throw new ConflictException("A member with the same email already exists.");
        }

        if (!existing.getPhoneNumber().equals(updated.getPhoneNumber()) &&
                memberRepository.existsByPhoneNumber(updated.getPhoneNumber())) {
            throw new ConflictException("A member with the same phone number already exists.");
        }

        existing.setName(updated.getName());
        existing.setAddress(updated.getAddress());
        existing.setEmail(updated.getEmail());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setStartDate(updated.getStartDate());
        existing.setBillingDuration(updated.getBillingDuration());

        return memberRepository.save(existing);
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findWithTournamentsById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with ID " + id + " not found."));

        if (!member.getTournaments().isEmpty()) {
            throw new ConflictException("Cannot delete member who is registered for tournaments.");
        }

        memberRepository.deleteById(id);
    }


}
