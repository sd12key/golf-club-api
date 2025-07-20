package org.alvio.golfnode.dto;

import java.time.LocalDate;

public class MemberSummaryDTO {

    private Long id;
    private String name;
    private String address;
    private String email;
    private String phoneNumber;
    private LocalDate startDate;
    private int membershipDurationMonths;

    public MemberSummaryDTO(Long id, String name, String address, String email,
                            String phoneNumber, LocalDate startDate, int membershipDurationMonths) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.membershipDurationMonths = membershipDurationMonths;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public LocalDate getStartDate() { return startDate; }
    public int getMembershipDurationMonths() { return membershipDurationMonths; }
}
