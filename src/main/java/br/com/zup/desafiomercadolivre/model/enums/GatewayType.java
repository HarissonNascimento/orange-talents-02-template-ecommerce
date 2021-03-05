package br.com.zup.desafiomercadolivre.model.enums;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public enum GatewayType {
    PAGSEGURO {
        @Override
        public String returnUrl(UriComponentsBuilder uriComponentsBuilder,
                                Long purchaseId) {
            if (purchaseId == null) {
                return null;
            }
            UriComponents urlReturn = uriComponentsBuilder
                    .path("/pagseguro-return/{id}")
                    .buildAndExpand(purchaseId.toString());
            return "pagseguro.com?returnId=" + purchaseId + "&redirectUrl=" + urlReturn;
        }
    }, PAYPAL {
        @Override
        public String returnUrl(UriComponentsBuilder uriComponentsBuilder,
                                Long purchaseId) {
            if (purchaseId == null) {
                return null;
            }
            UriComponents urlReturn = uriComponentsBuilder.path("/paypal-return/{id}")
                    .buildAndExpand(purchaseId.toString());
            return "paypal.com/" + purchaseId + "?redirectUrl=" + urlReturn;
        }
    };

    public abstract String returnUrl(UriComponentsBuilder uriComponentsBuilder, Long purchaseId);
}
