package LevelUP.repository;

import LevelUP.entity.Coupon;
import LevelUP.entity.CouponUsage;
import LevelUP.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCode(String code);
    List<Coupon> findByOwner(User owner);

    Optional<Coupon> findByCodeIgnoreCase(String codigoCupom);
}

