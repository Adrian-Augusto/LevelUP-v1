package LevelUP.service;


import LevelUP.entity.User;
import LevelUP.repository.UserRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RiotAccountService {

    private final RiotApiService riotApiService;
    private final UserRepository userRepository;

    public RiotAccountService(RiotApiService riotApiService, UserRepository userRepository) {
        this.riotApiService = riotApiService;
        this.userRepository = userRepository;
    }

    public void associarContaRiot(String nick, String tagline, User user) throws IOException {
        // 1. Buscar dados da Riot API
        JSONObject riotData = riotApiService.buscarPorRiotId(nick, tagline);

        // 2. Atualizar o usuário com os dados retornados
        user.setRiotPuuid(riotData.getString("puuid"));
        user.setRiotSummonerName(riotData.getString("gameName"));
        user.setRiotId(riotData.getString("tagLine"));
        user.setElo(null);

        // 3. Salvar o usuário no banco
        userRepository.save(user);
    }
}
