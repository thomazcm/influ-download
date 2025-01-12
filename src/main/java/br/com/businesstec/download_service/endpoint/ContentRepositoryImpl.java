package br.com.businesstec.download_service.endpoint;

import br.com.businesstec.download_service.config.InvalidRequestException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.postgresql.largeobject.LargeObjectManager;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;

import javax.sql.DataSource;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@SuppressWarnings("java:S2095")
public class ContentRepositoryImpl implements ContentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final DataSource dataSource;

    public ContentRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public StreamingResponseBody getData(String hash) {
        try {
            final Connection connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            final String sql = "SELECT data FROM files.default_large_file_content WHERE hash = ?";
            final PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, hash);
            final ResultSet resultSet = ps.executeQuery();
            /*--------------------------*/
            if (!resultSet.next()) {
                resultSet.close();
                ps.close();
                connection.rollback();
                connection.close();
                throw new InvalidRequestException("Erro ao acessar conteúdo do arquivo");
            }
            /*--------------------------*/
            final long oid = resultSet.getLong(1);
            final PGConnection pgConnection = connection.unwrap(PGConnection.class);
            final LargeObjectManager lObj = pgConnection.getLargeObjectAPI();
            /*--------------------------*/
            return outputStream -> {
                try (final LargeObject obj = lObj.open(oid, LargeObjectManager.READ);
                     final InputStream is = new FilterInputStream(obj.getInputStream()) {
                         @Override
                         public void close() throws IOException {
                             super.close();
                             try {
                                 resultSet.close();
                                 ps.close();
                                 connection.commit();
                                 connection.close();
                             } catch (SQLException e) {
                                 throw new IOException("Error closing resources", e);
                             }
                         }
                     }) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                } catch (Exception e) {
                    throw new InvalidRequestException("Error streaming file content", e);
                }
            };
            /*--------------------------*/
        } catch (SQLException e) {
            throw new DataAccessException("Error accessing database", e) {};
        }
    }

}
