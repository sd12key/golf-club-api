package org.alvio.golfnode.mapper;

import org.alvio.golfnode.dto.*;
import org.alvio.golfnode.rest.member.Member;
import org.alvio.golfnode.rest.tournament.Tournament;

import java.util.ArrayList;
import java.util.List;

public class MemberMapper {

    public static MemberDTO toMemberDTO(Member member, boolean showTournaments) {
        MemberDTO dto = new MemberDTO(
                member.getId(),
                member.getName(),
                member.getAddress(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getStartDate(),
                member.getMembershipDurationMonths()
        );

        if (showTournaments && member.getTournaments() != null && !member.getTournaments().isEmpty()) {
            List<TournamentSummaryDTO> tournamentSummaries = new ArrayList<>();
            for (Tournament t : member.getTournaments()) {
                tournamentSummaries.add(TournamentMapper.toSummary(t));
            }
            dto.setTournaments(tournamentSummaries);
        }

        return dto;
    }

    public static MemberDTO toMemberDTO(Member member) {
        return toMemberDTO(member, false);
    }

    public static MemberSummaryDTO toSummary(Member member) {
        return new MemberSummaryDTO(
                member.getId(),
                member.getName(),
                member.getAddress(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getStartDate(),
                member.getMembershipDurationMonths()
        );
    }
}
