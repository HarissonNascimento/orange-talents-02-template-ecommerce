package br.com.zup.desafiomercadolivre.util.fakeexternalapi.model.enums;

import br.com.zup.desafiomercadolivre.model.enums.StatusPurchase;

public enum PagSeguroStatusReturn {
    SUCCESSFUL {
        @Override
        public StatusPurchase processStatus() {
            return StatusPurchase.SUCCESSFUL;
        }
    }, ERROR {
        @Override
        public StatusPurchase processStatus() {
            return StatusPurchase.ERROR;
        }
    };

    public abstract StatusPurchase processStatus();
}
