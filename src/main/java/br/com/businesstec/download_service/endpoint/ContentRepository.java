package br.com.businesstec.download_service.endpoint;

import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface ContentRepository {

    StreamingResponseBody getData(String hash);

}
