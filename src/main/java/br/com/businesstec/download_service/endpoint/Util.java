package br.com.businesstec.download_service.endpoint;

import java.util.Optional;

public final class Util {

    private Util() {
        throw new IllegalStateException("Utility class");
    }


    public static Optional<String> parseString(Object input) {
        if (input == null || input.toString().isBlank()) {
            return Optional.empty();
        }
        return Optional.of(input.toString());
    }

}
