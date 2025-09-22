package rs.ac.bg.fon.nastava.model.dto;

import rs.ac.bg.fon.nastava.model.entity.Predmet;

import java.util.UUID;

public record PredmetDto(
        UUID id,
        String naziv,
        Integer espb,
        String sifra
) {

    public static PredmetDto from(Predmet predmet) {
        return new PredmetDto(
                predmet.getId(),
                predmet.getNaziv(),
                predmet.getEspb(),
                predmet.getSifra()
        );
    }

    public Predmet toEntity() {
        Predmet predmet = new Predmet();
        predmet.setId(id);
        predmet.setNaziv(naziv);
        predmet.setEspb(espb);
        predmet.setSifra(sifra);
        return predmet;
    }
}