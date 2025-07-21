package org.alvio.golfnode.rest.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.alvio.golfnode.rest.tournament.Tournament;

import java.time.LocalDate;
import java.util.*;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL auto-increment
    private Long id;

    @NotBlank(message = "Member name is required.")
    @Size(max = 50, message = "Maximum 50 characters.")
    @Column(nullable = false, length = 50)
    private String name;

    @NotBlank(message = "Address is required.")
    @Size(max = 300, message = "Maximum 300 characters.")
    @Column(nullable = false, length = 300)
    private String address;

    @NotBlank(message = "Email address is required.")
    @Email(message = "Invalid email address.")
    @Size(max = 100)
    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits.")
    @Column(nullable = false, length = 10, unique = true)
    private String phoneNumber;

    @NotNull(message = "Start date is required.")
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd")
    private LocalDate startDate;

    @NotBlank(message = "Billing duration is required.")
    @Pattern(
            regexp = "^(1mo|3mo|6mo|1yr|2yr|5yr|life)$",
            message = "Invalid billing duration. Allowed: 1mo, 3mo, 6mo, 1yr, 2yr, 5yr, life"
    )
    @Column(nullable = false, length=4)
    private String billingDuration;

    @ManyToMany(mappedBy = "members")
    private List<Tournament> tournaments = new ArrayList<>();

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name.trim(); }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address.trim(); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email.trim().toLowerCase(); }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber.trim(); }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public String getBillingDuration() { return billingDuration; }
    public void setBillingDuration(String billingDuration) {
        this.billingDuration = billingDuration.trim().toLowerCase();
    }

    public List<Tournament> getTournaments() { return tournaments; }
    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = (tournaments != null) ? tournaments : new ArrayList<>();
    }

    public void addTournament(Tournament tournament) {
        if (!this.tournaments.contains(tournament)) {
            this.tournaments.add(tournament);
        }
        if (!tournament.getMembers().contains(this)) {
            tournament.getMembers().add(this);
        }
    }

    public void removeTournament(Tournament tournament) {
        this.tournaments.remove(tournament);
        tournament.getMembers().remove(this);
    }

    public boolean hasTournament(Tournament tournament) {
        return this.tournaments.contains(tournament);
    }

    public boolean doesNotHaveTournament(Tournament tournament) {
        return !this.tournaments.contains(tournament);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id != null && id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
