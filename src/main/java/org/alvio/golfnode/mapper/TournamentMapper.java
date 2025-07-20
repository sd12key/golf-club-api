package org.alvio.golfnode.mapper;

import org.alvio.golfnode.dto.*;
import org.alvio.golfnode.rest.tournament.Tournament;
import org.alvio.golfnode.rest.member.Member;

import java.util.ArrayList;
import java.util.List;

public class TournamentMapper {

    public static TournamentDTO toTournamentDTO(Tournament tournament, boolean showMembers) {
        TournamentDTO dto = new TournamentDTO(
                tournament.getId(),
                tournament.getStartDate(),
                tournament.getEndDate(),
                tournament.getLocation(),
                tournament.getEntryFee(),
                tournament.getCashPrize()
        );

        if (showMembers && tournament.getMembers() != null && !tournament.getMembers().isEmpty()) {
            List<MemberSummaryDTO> memberSummaries = new ArrayList<>();
            for (Member m : tournament.getMembers()) {
                memberSummaries.add(MemberMapper.toSummary(m));
            }
            dto.setMembers(memberSummaries);
        }

        return dto;
    }

    public static TournamentDTO toTournamentDTO(Tournament tournament) {
        return toTournamentDTO(tournament, false);
    }

    public static TournamentSummaryDTO toSummary(Tournament tournament) {
        return new TournamentSummaryDTO(
                tournament.getId(),
                tournament.getStartDate(),
                tournament.getEndDate(),
                tournament.getLocation(),
                tournament.getEntryFee(),
                tournament.getCashPrize()
        );
    }
}
