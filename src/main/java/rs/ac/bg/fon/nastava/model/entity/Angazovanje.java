package rs.ac.bg.fon.nastava.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "angazovanje")
public class Angazovanje {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private Integer brojCasova;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OblikNastave uloga;

    private LocalDateTime datumPocetka;
    private LocalDateTime datumZavrsetka;

    @ManyToOne
    @JoinColumn(name = "predmet_id")
    private Predmet predmet;

    @ManyToOne
    @JoinColumn(name = "zaposleni_id")
    private Zaposleni zaposleni;

}