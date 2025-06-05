package LevelUP.controller;

import LevelUP.service.RiotApiService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/riot")
public class RiotController {

    private final RiotApiService riotApiService;

    public RiotController(RiotApiService riotApiService) {
        this.riotApiService = riotApiService;
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarPerfil(
            @RequestParam String gameName,
            @RequestParam String tagLine) {

        try {
            JSONObject perfil = riotApiService.buscarPorRiotId(gameName, tagLine);
            Map<String, Object> mapa = perfil.toMap(); // converte para Map

            return ResponseEntity.ok(mapa);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro ao buscar perfil: " + e.getMessage());
        }
    }}