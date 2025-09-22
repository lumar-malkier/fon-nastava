package rs.ac.bg.fon.nastava.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "predmet")
public class Katedra {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String naziv;
    private String sifra;
}