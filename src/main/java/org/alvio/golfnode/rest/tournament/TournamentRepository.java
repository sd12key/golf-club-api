package org.alvio.golfnode.rest.tournament;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    @EntityGraph(attributePaths = "members")
    Optional<Tournament> findWithMembersById(Long id);

    // eager loading for all tournaments and getting members
    @EntityGraph(attributePaths = "members")
    List<Tournament> findAllBy();

    // lazy loading
    List<Tournament> findByStartDateGreaterThanEqual(LocalDate startDate);

    // eager loading when showMembers=true
    @EntityGraph(attributePaths = "members")
    List<Tournament> findAllByStartDateGreaterThanEqual(LocalDate startDate);

    // lazy loading
    List<Tournament> findByLocationContainingIgnoreCase(String location);

    // eager loading when showMembers=true
    @EntityGraph(attributePaths = "members")
    List<Tournament> findAllByLocationContainingIgnoreCase(String location);

    // lazy loading
    List<Tournament> findByStartDateGreaterThanEqualAndLocationContainingIgnoreCase(LocalDate startDate, String location);

    // eager loading when showMembers=true
    @EntityGraph(attributePaths = "members")
    List<Tournament> findAllByStartDateGreaterThanEqualAndLocationContainingIgnoreCase(LocalDate startDate, String location);
}
