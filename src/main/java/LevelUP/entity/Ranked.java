package LevelUP.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ranked")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ranked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long id;
    private String titulo;
    private String descricao;
    private boolean ativa;


    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "ranked")
    private List<Participante> participantes = new ArrayList<>();


}