package br.com.businesstec.download_service.endpoint;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Map;

@Service
public class DownloadService {

    private final ContentRepository repository;

    public DownloadService(ContentRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<StreamingResponseBody> downloadFile(Map<String, Object> params) {

        final String hash = Util.parseString(params.get("hash"))
                .orElseThrow(() -> new IllegalArgumentException("Hash is required"));
        final String fileName = Util.parseString(params.get("fileName"))
                .orElseThrow(() -> new IllegalArgumentException("Filename is required"));
        final String contentType = Util.parseString(params.get("contentType"))
                .orElseThrow(() -> new IllegalArgumentException("ContentType is required"));

        final HttpHeaders headers = buildHeaders(fileName, contentType);

        return new ResponseEntity<>(repository.getData(hash), headers, HttpStatus.OK);
    }

    private HttpHeaders buildHeaders(String fileName, String contentType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.attachment().filename(fileName).build());
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        return headers;
    }

}
