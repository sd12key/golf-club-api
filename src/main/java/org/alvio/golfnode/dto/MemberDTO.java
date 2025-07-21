package org.alvio.golfnode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDTO {

    private Long id;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private LocalDate startDate;
    private String billingDuration;
    private List<TournamentSummaryDTO> tournaments;

    public MemberDTO(Long id, String name, String address, String email,
                     String phoneNumber, LocalDate startDate, String billingDuration) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.billingDuration = billingDuration;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDate getStartDate() { return startDate; }
    public String getBillingDuration() { return billingDuration; }

    public List<TournamentSummaryDTO> getTournaments() { return tournaments; }
    public void setTournaments(List<TournamentSummaryDTO> tournaments) { this.tournaments = tournaments; }
}
