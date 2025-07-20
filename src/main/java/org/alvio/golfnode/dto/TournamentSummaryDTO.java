package org.alvio.golfnode.dto;

import java.time.LocalDate;

public class TournamentSummaryDTO {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private String location;
    private double entryFee;
    private double cashPrize;

    public TournamentSummaryDTO(Long id, LocalDate startDate, LocalDate endDate,
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
}
