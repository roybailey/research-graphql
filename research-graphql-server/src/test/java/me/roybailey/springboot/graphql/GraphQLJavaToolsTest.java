package me.roybailey.springboot.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.GraphQLResolver;
import com.coxautodev.graphql.tools.SchemaParser;
import graphql.GraphQL;
import graphql.schema.DataFetchingEnvironment;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * graphql-java-tools example schema
 */
@Slf4j
public class GraphQLJavaToolsTest {

    @Data
    @Builder
    private static class Book {
        private int id;
        private String name;
        private int authorId;
    }

    @Data
    @Builder
    private static class Author {
        private int id;
        private String name;
    }

    static class BookResolver implements GraphQLResolver<Book> /* This class is a resolver for the Book "Data Class" */ {

        Map<Integer, Author> mapAuthors;

        public BookResolver() {
            mapAuthors = new HashMap<>();
            mapAuthors.put(1, Author.builder().id(1).name("Anna").build());
            mapAuthors.put(2, Author.builder().id(2).name("Burt").build());
            mapAuthors.put(3, Author.builder().id(3).name("Carl").build());
        }

        public Author author(int authorId) {
            return mapAuthors.get(authorId);
        }
        public Author author(Book book) {
            return mapAuthors.get(book.getAuthorId());
        }
        public Author author(DataFetchingEnvironment dfe) {
            Book book = dfe.getSource();
            return mapAuthors.get(book.getAuthorId());
        }
    }

    private static class Query implements GraphQLQueryResolver {

        Map<Integer, Book> mapBooks;

        public Query() {
            mapBooks = new HashMap<>();
            mapBooks.put(1, Book.builder().id(1).name("Anna and her armpit hairs").authorId(1).build());
            mapBooks.put(2, Book.builder().id(2).name("Burt and his burnt bits").authorId(2).build());
            mapBooks.put(3, Book.builder().id(3).name("Carl and his colon cleansing").authorId(3).build());
        }

        public List<Book> books() {
            return new ArrayList<>(mapBooks.values());
        }
    }

    @Test
    public void testGraphQlJavaToolsSchema() {

        // My application class
        SchemaParser file = SchemaParser.newParser()
                .file("sample-schema.graphql")
                .resolvers(new Query(), new BookResolver())
                .dictionary(Book.class, Author.class)
                .build();

        GraphQL graphQL = GraphQL.newGraphQL(file.makeExecutableSchema()).build();

        Map<String, Object> result = graphQL.execute("{books {id}}").getData();
        System.out.println(result);
    }
}
