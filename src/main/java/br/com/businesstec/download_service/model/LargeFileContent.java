package br.com.businesstec.download_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "default_large_file_content", schema = "files")
public class LargeFileContent {

    @Id
    private String hash;

    @Column(columnDefinition = "oid")
    private Long data;

    protected LargeFileContent() {
    }

    public LargeFileContent(String hash, Long data) {
        this.hash = hash;
        this.data = data;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }

}
