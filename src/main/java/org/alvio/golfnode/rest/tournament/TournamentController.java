package org.alvio.golfnode.rest.tournament;

import jakarta.validation.Valid;
import org.alvio.golfnode.dto.TournamentDTO;
import org.alvio.golfnode.dto.TournamentRegistrationRequestDTO;
import org.alvio.golfnode.mapper.MemberMapper;
import org.alvio.golfnode.mapper.TournamentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @GetMapping("/tournaments")
    public ResponseEntity<?> getAllTournaments(
            @RequestParam(name = "show-members", required=false, defaultValue = "false") boolean showMembers
    ) {
        List<TournamentDTO> dtos = tournamentService.getAllTournaments(showMembers).stream()
                .map(t -> TournamentMapper.toTournamentDTO(t, showMembers))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/tournament/{id}")
    public ResponseEntity<?> getTournamentById(
            @PathVariable Long id,
            @RequestParam(name = "show-members", required=false, defaultValue = "false") boolean showMembers
    ) {
        Tournament tournament = tournamentService.getTournamentById(id, showMembers);
        return ResponseEntity.ok(TournamentMapper.toTournamentDTO(tournament, showMembers));
    }

    @GetMapping("/tournament-search")
    public ResponseEntity<?> searchTournaments(
            @RequestParam Map<String, String> allParams,
            @RequestParam(name = "start-date", required = false) LocalDate startDate,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "show-members", required=false, defaultValue = "false") boolean showMembers
    ) {
        // Validate allowed params
        List<String> allowedParams = List.of("start-date", "location", "show-members");
        List<String> unknownParams = allParams.keySet().stream()
                .filter(p -> !allowedParams.contains(p))
                .toList();

        if (!unknownParams.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Unknown parameter(s): " + String.join(", ", unknownParams)
            ));
        }

        List<TournamentDTO> dtos = tournamentService.searchTournaments(startDate, location, showMembers).stream()
                .map(t -> TournamentMapper.toTournamentDTO(t, showMembers))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/tournament")
    public ResponseEntity<?> addTournament(@Valid @RequestBody Tournament tournament) {
        Tournament created = tournamentService.addTournament(tournament);
        return ResponseEntity.status(HttpStatus.CREATED).body(TournamentMapper.toTournamentDTO(created));
    }

    @PostMapping("/tournaments")
    public ResponseEntity<?> addTournaments(@Valid @RequestBody List<Tournament> tournaments) {
        List<TournamentDTO> dtos = tournamentService.addTournaments(tournaments).stream()
                .map(TournamentMapper::toTournamentDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(dtos);
    }

    @PutMapping("/tournament/{id}")
    public ResponseEntity<?> updateTournament(@PathVariable Long id, @Valid @RequestBody Tournament tournament) {
        Tournament updated = tournamentService.updateTournament(id, tournament);
        return ResponseEntity.ok(TournamentMapper.toTournamentDTO(updated));
    }

    @DeleteMapping("/tournament/{id}")
    public ResponseEntity<?> deleteTournament(@PathVariable Long id) {
        tournamentService.deleteTournament(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/tournament/member")
    public ResponseEntity<?> addMemberToTournament(@Valid @RequestBody TournamentRegistrationRequestDTO request) {
        TournamentMemberPair result = tournamentService.addMemberToTournament(request.getTournamentId(), request.getMemberId());
        return ResponseEntity.ok(Map.of(
                "message", "Member added to tournament.",
                "tournament", TournamentMapper.toSummary(result.tournament()),
                "member", MemberMapper.toSummary(result.member())
        ));
    }

    @PostMapping("/tournament/members")
    public ResponseEntity<?> addMembersToTournament(@Valid @RequestBody List<TournamentRegistrationRequestDTO> requests) {
        if (requests == null || requests.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Request must include at least one booking."));
        }

        List<Map<String, Object>> resultList = new ArrayList<>();

        for (TournamentRegistrationRequestDTO req : requests) {
            try {
                TournamentMemberPair result = tournamentService.addMemberToTournament(req.getTournamentId(), req.getMemberId());
                resultList.add(Map.of(
                        "message", "Member added to tournament.",
                        "tournament", TournamentMapper.toSummary(result.tournament()),
                        "member", MemberMapper.toSummary(result.member())
                ));
            } catch (Exception ex) {
                resultList.add(Map.of(
                        "error", ex.getClass().getSimpleName() + ": " + ex.getMessage(),
                        "tournamentId", req.getTournamentId(),
                        "memberId", req.getMemberId()
                ));
            }
        }

        return ResponseEntity.ok(resultList);
    }


    @DeleteMapping("/tournament/member")
    public ResponseEntity<?> removeMemberFromTournament(@Valid @RequestBody TournamentRegistrationRequestDTO request) {
        tournamentService.removeMemberFromTournament(request.getTournamentId(), request.getMemberId());
        return ResponseEntity.ok(Map.of(
                "message", "Member removed from tournament.",
                "tournamentId", request.getTournamentId(),
                "memberId", request.getMemberId()
        ));
    }




}
