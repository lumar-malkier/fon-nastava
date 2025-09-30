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
@Table(name = "predmet")
public class Predmet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String naziv;
    private Integer espb;
    private String sifra;

    private LocalDateTime datumZaposlenja;

    private OffsetDateTime deletedAt;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "predmet")
    private Set<Angazovanje> angazovanja;

}