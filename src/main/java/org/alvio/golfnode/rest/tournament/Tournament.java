package org.alvio.golfnode.rest.tournament;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.alvio.golfnode.rest.member.Member;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL auto-increment
    private Long id;

    @NotNull(message = "Start date is required.")
    @Column(nullable = false)
    private LocalDate startDate;

    @NotNull(message = "End date is required.")
    @Column(nullable = false)
    private LocalDate endDate;

    @AssertTrue(message = "End date must be after or equal to start date.")
    public boolean isEndDateValid() {
        if (startDate == null || endDate == null) { return true; }
        return endDate.isAfter(startDate) || endDate.isEqual(startDate);
    }

    @NotBlank(message = "Location is required.")
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String location;

    @PositiveOrZero(message = "Entry fee must be 0 or positive.")
    @Column(nullable = false)
    private double entryFee;

    @PositiveOrZero(message = "Cash prize must be 0 or positive.")
    @Column(nullable = false)
    private double cashPrize;

    @ManyToMany
    @JoinTable(
        name = "member_tournament",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "member_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"tournament_id", "member_id"})
    )
    private List<Member> members = new ArrayList<>();

    public Long getId() { return id; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location.trim(); }

    public double getEntryFee() { return entryFee; }
    public void setEntryFee(double entryFee) { this.entryFee = entryFee; }

    public double getCashPrize() { return cashPrize; }
    public void setCashPrize(double cashPrize) { this.cashPrize = cashPrize; }

    public List<Member> getMembers() { return members; }
    public void setMembers(List<Member> members) {
        this.members = (members != null) ? members : new ArrayList<>();
    }

    // member management methods, not sure yet if I will actually use these methods
    public void addMember(Member member) {
        if (!this.members.contains(member)) {
            this.members.add(member);
        }
        if (!member.getTournaments().contains(this)) {
            member.getTournaments().add(this);
        }
    }

    public void removeMember(Member member) {
        this.members.remove(member);
        member.getTournaments().remove(this);
    }

    public boolean hasMember(Member member) {
        return this.members.contains(member);
    }

    public boolean doesNotHaveMember(Member member) {
        return !this.members.contains(member);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tournament that = (Tournament) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}
