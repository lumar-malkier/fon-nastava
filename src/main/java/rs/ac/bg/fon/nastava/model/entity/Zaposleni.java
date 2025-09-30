package rs.ac.bg.fon.nastava.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "zaposleni")
public class Zaposleni {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String ime;
    private String prezime;
    private String jmbg;
    private String email;
    private Zvanje zvanje;

    private LocalDateTime datumZaposlenja;

    private OffsetDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "katedra_id")
    private Katedra katedra;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "zaposleni")
    private Set<Angazovanje> angazovanja;

}