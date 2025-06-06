package LevelUP.controller;


import LevelUP.Request.RiotAccountRequest;
import LevelUP.entity.User;
import LevelUP.repository.UserRepository;
import LevelUP.service.RiotAccountService;
import LevelUP.service.RiotApiService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/Connect")
public class RiotAccountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RiotApiService riotApiService;

    @PostMapping("/leag")
    public ResponseEntity<?> associarContaRiot(
            @RequestBody RiotAccountRequest request) {

        if (request == null || request.getNick() == null || request.getTagline() == null) {
            return ResponseEntity.badRequest().body("JSON inválido. Envie nick e tagline.");
        }

        // Obter usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado.");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof User user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado no contexto.");
        }

        try {
            // 1. Consultar API da Riot
            JSONObject perfil = riotApiService.buscarPorRiotId(request.getNick(), request.getTagline());

            String puuid = perfil.getString("puuid");
            String gameName = perfil.getString("gameName");
            String tagLine = perfil.getString("tagLine");

            // 2. Atualizar informações no usuário
            user.setRiotPuuid(puuid);
            user.setRiotSummonerName(gameName);
            user.setRiotId(tagLine);

            // 3. Salvar no banco
            userRepository.save(user);

            return ResponseEntity.ok("Conta Riot associada com sucesso!");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro de conexão com API da Riot: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body("Erro ao buscar conta Riot: " + e.getMessage());
        }
    }
}
