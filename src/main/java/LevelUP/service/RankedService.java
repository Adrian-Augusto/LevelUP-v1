package LevelUP.service;

import LevelUP.Exception.UserAlreadyInRankedException;
import LevelUP.Validation.NoRankedFoundException;
import LevelUP.entity.Participante;
import LevelUP.entity.Ranked;
import LevelUP.entity.User;
import LevelUP.repository.ParticipanteRepository;
import LevelUP.repository.RankedRepository;
import LevelUP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.tokens.BlockSequenceStartToken;

import java.util.List;


@Service
public class RankedService {

    private final RankedRepository rankedRepository;
    private final UserRepository userRepository;
    private final ParticipanteRepository participanteRepository;

    public RankedService(RankedRepository rankedRepository, UserRepository userRepository, ParticipanteRepository participanteRepository) {
        this.rankedRepository = rankedRepository;
        this.userRepository = userRepository;
        this.participanteRepository = participanteRepository;
    }

    public Ranked createRanked(Ranked ranked) {
        return rankedRepository.save(ranked);
    }

    public void deleteRanked(Long id) {
        if (!rankedRepository.existsById(id)) {
            throw new IllegalArgumentException("Ranked com ID " + id + " não existe.");
        }
        rankedRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Ranked> listRanked() {
        List<Ranked> rankeds = rankedRepository.findAll();
        if (rankeds.isEmpty()) {
            throw new NoRankedFoundException("Nenhuma ranked encontrada.");
        }
        return rankeds;
    }

    @Transactional
    public void entrarNaRanked(Long rankedId, Long userId) {
        Ranked ranked = rankedRepository.findById(rankedId)
                .orElseThrow(() -> new IllegalArgumentException("Ranked não encontrada com ID: " + rankedId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com ID: " + userId));

        boolean jaParticipa = ranked.getParticipantes().stream()
                .anyMatch(p -> p.getUser().getId().equals(userId));

        if (jaParticipa) {
            throw new UserAlreadyInRankedException("Usuário já está participando desta ranked.");
        }

        Participante participante = new Participante();
        participante.setUser(user);
        participante.setRanked(ranked);
        participante.setPontuacao(0); // pontuação inicial

        participanteRepository.save(participante);
    }
}
