package LevelUP.repository;

import LevelUP.entity.Coupon;
import LevelUP.entity.CouponUsage;
import LevelUP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {
    long countByCoupon(Coupon coupon);
    long countByCoupon_Owner(User owner); // total de usos de todos os cupons de um streamer

    boolean existsByCouponAndUser(Coupon coupon, User user);
}
