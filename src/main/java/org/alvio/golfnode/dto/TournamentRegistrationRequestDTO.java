package org.alvio.golfnode.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TournamentRegistrationRequestDTO {

    @NotNull(message = "Tournament ID is required.")
    @Min(value = 1, message = "Tournament ID must be positive.")
    private Long tournamentId;

    @NotNull(message = "Member ID is required.")
    @Min(value = 1, message = "Member ID must be positive.")
    private Long memberId;

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
