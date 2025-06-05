package LevelUP.service;

import LevelUP.entity.Coupon;
import LevelUP.entity.User;
import LevelUP.enums.Role;
import LevelUP.repository.CouponRepository;
import LevelUP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CouponRepository couponRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;


    public Coupon createCouponForStreamerByEmail(String code, String email) {
        User streamer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário com e-mail não encontrado"));

        Coupon coupon = new Coupon();
        coupon.setCode(code.toUpperCase());
        coupon.setOwner(streamer);
        coupon.setActive(true);

        return couponRepository.save(coupon);
    }
}
