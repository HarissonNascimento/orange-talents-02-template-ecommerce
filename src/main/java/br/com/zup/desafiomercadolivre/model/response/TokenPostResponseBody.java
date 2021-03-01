package br.com.zup.desafiomercadolivre.model.response;

public class TokenPostResponseBody {

    private final String token;
    private final String type;

    public TokenPostResponseBody(String token, String type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }
}
