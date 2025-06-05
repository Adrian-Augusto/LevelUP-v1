package LevelUP.controller;

import LevelUP.Request.AssinaturaRequest;
import LevelUP.entity.Assinatura;
import LevelUP.service.MercadoPagoSubscriptionService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AssinaturaController {
    private final MercadoPagoSubscriptionService mercadoPagoSubscriptionService;

    public AssinaturaController(MercadoPagoSubscriptionService mercadoPagoSubscriptionService) {
        this.mercadoPagoSubscriptionService = mercadoPagoSubscriptionService;
    }

    @PostMapping("/assinatura")
    public ResponseEntity<?> criarAssinatura(@RequestBody AssinaturaRequest request) {
        Assinatura assinatura = mercadoPagoSubscriptionService.criarAssinaturaComCupom(
                request.getUserId(), request.getCodigoCupom(), request.getTipoPlano());
        return ResponseEntity.ok(assinatura);
    }

}
