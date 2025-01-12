package br.com.businesstec.download_service.config;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(String errorStreamingFileContent, Exception e) {
        super(errorStreamingFileContent, e);
    }
}
