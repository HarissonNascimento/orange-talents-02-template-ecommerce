package br.com.zup.desafiomercadolivre.model.response;

import br.com.zup.desafiomercadolivre.model.domain.Characteristic;

public class CharacteristicGetResponseBody {

    private String name;
    private String description;

    public CharacteristicGetResponseBody toCharacteristicGetResponseBody(Characteristic characteristic) {
        this.name = characteristic.getName();
        this.description = characteristic.getDescription();
        return this;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
