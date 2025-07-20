package org.alvio.golfnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TournamentDTO {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private double entryFee;
    private double cashPrize;
    private List<MemberSummaryDTO> members;

    public TournamentDTO(Long id, LocalDate startDate, LocalDate endDate,
                         String location, double entryFee, double cashPrize) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.entryFee = entryFee;
        this.cashPrize = cashPrize;
    }

    public Long getId() { return id; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public String getLocation() { return location; }
    public double getEntryFee() { return entryFee; }
    public double getCashPrize() { return cashPrize; }

    public List<MemberSummaryDTO> getMembers() { return members; }
    public void setMembers(List<MemberSummaryDTO> members) { this.members = members; }
}
