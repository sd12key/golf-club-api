package org.alvio.golfnode.rest.tournament;

import org.alvio.golfnode.rest.member.Member;

public record TournamentMemberPair(
        Tournament tournament,
        Member member
) { }
