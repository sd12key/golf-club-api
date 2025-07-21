package org.alvio.golfnode.rest.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TournamentService {

    @Autowired
    private TournamentRepository tournamentRepository;

    public List<Tournament> getAllTournaments(boolean showMembers) {
        return showMembers ? tournamentRepository.findAllBy() : tournamentRepository.findAll();
    }

    public Tournament getTournamentById(Long id, boolean showMembers) {
        return (showMembers
                ? tournamentRepository.findWithMembersById(id)
                : tournamentRepository.findById(id))
                .orElseThrow(() -> new NoSuchElementException("Tournament with ID " + id + " not found."));
    }

    public List<Tournament> searchTournaments(
            LocalDate startDate,
            String location,
            boolean showMembers
    ) {

        if (startDate != null && location != null) {
            return showMembers
                    ? tournamentRepository.findAllByStartDateGreaterThanEqualAndLocationContainingIgnoreCase(startDate, location)
                    : tournamentRepository.findByStartDateGreaterThanEqualAndLocationContainingIgnoreCase(startDate, location);
        }

        if (startDate != null) {
            return showMembers
                    ? tournamentRepository.findAllByStartDateGreaterThanEqual(startDate)
                    : tournamentRepository.findByStartDateGreaterThanEqual(startDate);
        }

        if (location != null) {
            return showMembers
                    ? tournamentRepository.findAllByLocationContainingIgnoreCase(location)
                    : tournamentRepository.findByLocationContainingIgnoreCase(location);
        }

        return getAllTournaments(showMembers);
    }

    public Tournament addTournament(Tournament tournament) {
        if (tournament.getId() != null) {
            throw new IllegalArgumentException("ID must not be provided when creating a new record.");
        }
        return tournamentRepository.save(tournament);
    }

    public List<Tournament> addTournaments(List<Tournament> tournaments) {
        for (Tournament t : tournaments) {
            if (t.getId() != null) {
                throw new IllegalArgumentException("ID must not be provided when creating a new record.");
            }
        }
        return tournamentRepository.saveAll(tournaments);
    }

    public Tournament updateTournament(Long id, Tournament updated) {
        if (updated.getId() != null && !updated.getId().equals(id)) {
            throw new IllegalArgumentException("Payload ID must match path variable or be omitted.");
        }

        Tournament existing = tournamentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tournament with ID " + id + " not found."));

        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setLocation(updated.getLocation());
        existing.setEntryFee(updated.getEntryFee());
        existing.setCashPrize(updated.getCashPrize());

        return tournamentRepository.save(existing);
    }

    public void deleteTournament(Long id) {
        Tournament existing = tournamentRepository.findWithMembersById(id)
                .orElseThrow(() -> new NoSuchElementException("Tournament with ID " + id + " not found."));

        if (!existing.getMembers().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete a tournament with registered members.");
        }

        tournamentRepository.deleteById(id);
    }
}
