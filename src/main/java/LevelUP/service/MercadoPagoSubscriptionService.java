package LevelUP.service;

import LevelUP.entity.*;
import LevelUP.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


@Service
public class MercadoPagoSubscriptionService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponUsageRepository couponUsageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssinaturaRepository assinaturaRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final String ACCESS_TOKEN = "TEST-247412826400693-060116-a5f0b87343060933e152dbaf3f38a76e-1106849961"; // Trocar pelo token real

    private final double VALOR_BASE_MENSAL = 39.90;
    private final double VALOR_BASE_ANUAL = 399.00;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    @Transactional
    public Assinatura criarAssinaturaComCupom(Long userId, String codigoCupom, String tipoPlano) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        double valorFinal;
        int meses;
        if ("ANUAL".equalsIgnoreCase(tipoPlano)) {
            valorFinal = VALOR_BASE_ANUAL;
            meses = 12;
        } else {
            valorFinal = VALOR_BASE_MENSAL;
            meses = 1;
        }

        Coupon coupon = null;
        if (codigoCupom != null && !codigoCupom.isBlank()) {
            coupon = couponRepository.findByCodeIgnoreCase(codigoCupom)
                    .orElseThrow(() -> new RuntimeException("Cupom inválido"));

            if (!coupon.isActive()) throw new RuntimeException("Cupom inativo");

            boolean usado = couponUsageRepository.existsByCouponAndUser(coupon, user);
            if (usado) throw new RuntimeException("Cupom já usado por este usuário");

            if (coupon.isPercentage()) {
                valorFinal = valorFinal * (1 - coupon.getDiscount() / 100);
            } else {
                valorFinal = valorFinal - coupon.getDiscount();
                if (valorFinal < 0) valorFinal = 0;
            }
        }

        // Gerar datas formatadas corretamente
        String startDate = LocalDateTime.now().plusMinutes(1)
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .format(formatter);
        String endDate = LocalDateTime.now().plusYears(5)
                .atZone(ZoneId.of("America/Sao_Paulo"))
                .format(formatter);

        // Payload da assinatura
        Map<String, Object> payload = new HashMap<>();
        payload.put("reason", "Assinatura LevelUP");
        payload.put("auto_recurring", Map.of(
                "frequency", meses,
                "frequency_type", "months",
                "transaction_amount", valorFinal,
                "currency_id", "BRL",
                "start_date", startDate,
                "end_date", endDate
        ));
        payload.put("payer_email", user.getEmail());
        payload.put("back_url", "https://seusite.com/retorno");
        payload.put("external_reference", String.valueOf(user.getId()));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://api.mercadopago.com/preapproval", request, Map.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao criar assinatura no Mercado Pago");
        }

        Map responseBody = response.getBody();
        String preapprovalId = (String) responseBody.get("id");

        Assinatura assinatura = new Assinatura();
        assinatura.setUser(user);
        assinatura.setPreapprovalId(preapprovalId);
        assinatura.setStartDate(LocalDate.now());
        assinatura.setExpiryDate(LocalDate.now().plusMonths(meses));
        assinatura.setActive(true);
        assinatura.setStatus("pending");

        assinaturaRepository.save(assinatura);

        if (coupon != null) {
            CouponUsage usage = new CouponUsage();
            usage.setCoupon(coupon);
            usage.setUser(user);
            usage.setUsedAt(LocalDateTime.now());
            couponUsageRepository.save(usage);
        }

        return assinatura;
    }
}