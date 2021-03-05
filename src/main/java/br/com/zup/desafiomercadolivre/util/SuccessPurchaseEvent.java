package br.com.zup.desafiomercadolivre.util;

import br.com.zup.desafiomercadolivre.model.domain.Purchase;

public interface SuccessPurchaseEvent {

    void process(Purchase purchase);
}
