package br.com.businesstec.download_service.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class DownloadEndpoint {

    private final DownloadService downloadService;

    public DownloadEndpoint(DownloadService downloadService) {
        this.downloadService = downloadService;
    }

    @GetMapping("/download")
    public ResponseEntity<StreamingResponseBody> downloadFile(@RequestParam Map<String, Object> params) {
        return downloadService.downloadFile(params);
    }

}
