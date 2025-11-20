package saj.techie.catalgueui.client;

import saj.techie.catalgueui.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import saj.techie.catalgueui.config.ObjectMapperProvider;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

@Component
public class BookManagementClient {

    @Value("${management.api.base-url:http://localhost:8081/api}")
    private String baseUrl;

    private Client client;

    @PostConstruct
    public void init() {
        this.client = ClientBuilder.newBuilder()
                .register(org.glassfish.jersey.jackson.JacksonFeature.class)
                .register(ObjectMapperProvider.class)
                .build();
    }

    private Invocation.Builder request(String path) {
        WebTarget target = client.target(baseUrl).path(path);
        return target.request(MediaType.APPLICATION_JSON_TYPE);
    }

    public List<Book> listBooks() {
        Response response = request("books").get();
        if (response.getStatus() >= 400) {
            throw new RuntimeException("Failed to list books: HTTP " + response.getStatus());
        }
        Book[] books = response.readEntity(Book[].class);
        return Arrays.asList(books);
    }

    public Book getBook(Long id) {
        Response response = request("books/" + id).get();
        if (response.getStatus() == 404) {
            return null;
        }
        if (response.getStatus() >= 400) {
            throw new RuntimeException("Failed to get book: HTTP " + response.getStatus());
        }
        return response.readEntity(Book.class);
    }

    public Book createBook(Book book) {
        Response response = request("books")
                .post(Entity.entity(book, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatus() >= 400) {
            throw new RuntimeException("Failed to create book: HTTP " + response.getStatus());
        }
        return response.readEntity(Book.class);
    }

    public Book updateBook(Long id, Book book) {
        Response response = request("books/" + id)
                .put(Entity.entity(book, MediaType.APPLICATION_JSON_TYPE));
        if (response.getStatus() >= 400) {
            throw new RuntimeException("Failed to update book: HTTP " + response.getStatus());
        }
        return response.readEntity(Book.class);
    }

    public void deleteBook(Long id) {
        Response response = request("books/" + id).delete();
        if (response.getStatus() >= 400) {
            throw new RuntimeException("Failed to delete book: HTTP " + response.getStatus());
        }
    }
}
