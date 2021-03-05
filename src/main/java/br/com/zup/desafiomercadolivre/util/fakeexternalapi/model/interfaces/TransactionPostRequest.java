package br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.interfaces;

import br.com.zup.desafiomercadolivre.model.domain.Purchase;
import br.com.zup.desafiomercadolivre.model.domain.Transaction;

public interface TransactionPostRequest {
    Transaction toTransaction(Purchase purchase);
}
