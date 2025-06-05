package LevelUP.controller;


import LevelUP.Request.RiotAccountRequest;
import LevelUP.entity.User;
import LevelUP.repository.UserRepository;
import LevelUP.service.RiotAccountService;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/riot")
public class RiotAccountController {

    private final RiotAccountService riotAccountService;
    private final UserRepository userRepository;

    public RiotAccountController(RiotAccountService riotAccountService, UserRepository userRepository) {
        this.riotAccountService = riotAccountService;
        this.userRepository = userRepository;
    }

    @PostMapping("lol/connect")
    public ResponseEntity<?> associarRiotAccount(
            @RequestBody RiotAccountRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


            riotAccountService.associarContaRiot(request.getNick(), request.getTagline(), user);

            return ResponseEntity.ok("Conta Riot associada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro: " + e.getMessage());
        }
    }
}
