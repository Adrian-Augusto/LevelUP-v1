package LevelUP.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coupon")

public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // ex: ADRIAN10

    @Column(nullable = false)
    private Double discount = 10.0; // valor padrão 10, pode ser alterado

    @Column(nullable = false)
    private boolean percentage = true; // se true, desconto é percentual; se false, valor fixo

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; // streamer/influencer dono do cupom

}
