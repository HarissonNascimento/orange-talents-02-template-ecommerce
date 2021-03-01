package br.com.zup.desafiomercadolivre.model.request;

import br.com.zup.desafiomercadolivre.model.domain.Characteristic;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CharacteristicPostRequestBody {

    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 1000)
    private String description;

    public CharacteristicPostRequestBody(@NotBlank String name,
                                         @NotBlank @Size(max = 1000) String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Characteristic toCharacteristic() {
        return new Characteristic(this.name, this.description);
    }
}
