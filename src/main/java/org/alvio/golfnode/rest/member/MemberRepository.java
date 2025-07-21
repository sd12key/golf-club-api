package org.alvio.golfnode.rest.member;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmailIgnoreCase(String email);
    boolean existsByPhoneNumber(String phoneNumber);

    // for eager loading when showTournaments=true
    @EntityGraph(attributePaths = "tournaments")
    List<Member> findAllBy(); // eager

    // for eager loading when showTournaments=true
    @EntityGraph(attributePaths = "tournaments")
    Optional<Member> findWithTournamentsById(Long id);

    // lazy loading
    List<Member> findByNameContainingIgnoreCase(String name);

    // for eager loading when showTournaments=true
    @EntityGraph(attributePaths = "tournaments")
    List<Member> findAllByNameContainingIgnoreCase(String name);

    // lazy loading
    List<Member> findByBillingDurationContainingIgnoreCase(String billingDuration);

    // for eager loading when showTournaments=true
    @EntityGraph(attributePaths = "tournaments")
    List<Member> findAllByBillingDurationContainingIgnoreCase(String billingDuration);

    // lazy loading
    List<Member> findByPhoneNumberContaining(String phoneNumber);

    // for eager loading when showTournaments=true
    @EntityGraph(attributePaths = "tournaments")
    List<Member> findAllByPhoneNumberContaining(String phoneNumber);

    // lazy
    List<Member> findByEmailContainingIgnoreCase(String email);

    // eager
    @EntityGraph(attributePaths = "tournaments")
    List<Member> findAllByEmailContainingIgnoreCase(String email);

    // lazy loading
    List<Member> findByStartDateGreaterThanEqual(LocalDate startDate);

    // for eager loading when showTournaments=true
    @EntityGraph(attributePaths = "tournaments")
    List<Member> findAllByStartDateGreaterThanEqual(LocalDate startDate);

    // tournament start date search, always eager loading because of FETCH
    @Query("SELECT DISTINCT m FROM Member m JOIN FETCH m.tournaments t WHERE t.startDate >= :startDate")
    List<Member> findByTournamentStartDate(@Param("startDate") LocalDate startDate);

}
