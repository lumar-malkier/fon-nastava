package rs.ac.bg.fon.nastava.model.dto;

import rs.ac.bg.fon.nastava.model.entity.Katedra;

import java.util.UUID;

public record KatedraDto(
        UUID id,
        String naziv,
        String sifra
) {

    public static KatedraDto from(Katedra katedra) {
        return new KatedraDto(katedra.getId(), katedra.getNaziv(), katedra.getSifra());
    }

    public Katedra toEntity() {
        return new Katedra(id, naziv, sifra);
    }
}
