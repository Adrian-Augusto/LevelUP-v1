package LevelUP.repository;


import LevelUP.entity.Assinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AssinaturaRepository extends JpaRepository<Assinatura, Long> {
    Optional<Assinatura> findByPreapprovalId(String preapprovalId);
    Optional<Assinatura> findByUser_Id(Long id);
}