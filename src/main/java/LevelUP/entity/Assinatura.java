package LevelUP.entity;

import LevelUP.enums.TipoPlano;
import jakarta.persistence.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

@Entity
@Table(name = "assinatura")

public class Assinatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String preapprovalId;

    private LocalDate startDate;
    private LocalDate expiryDate;

    private boolean active;
    @Enumerated(EnumType.STRING)

    private TipoPlano tipoPlano;
    private String status; // Ex: authorized, cancelled, paused

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User user;

    // Construtor vazio (obrigatório para JPA)
    public Assinatura() {}

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPreapprovalId() {
        return preapprovalId;
    }

    public void setPreapprovalId(String preapprovalId) {
        this.preapprovalId = preapprovalId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
