package LevelUP.service;

import LevelUP.entity.Coupon;
import LevelUP.entity.User;
import LevelUP.repository.CouponRepository;
import LevelUP.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepository couponRepository;

    public Coupon createCouponForStreamerByEmail(String code, String email) {
        User streamer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário com e-mail não encontrado"));

        Coupon coupon = new Coupon();
        coupon.setCode(code.toUpperCase()); // Ex: "ADRIAN10"
        coupon.setOwner(streamer);
        coupon.setActive(true);

        coupon.setDiscount(10.0);
        coupon.setPercentage(true);

        return couponRepository.save(coupon);
    }
}
