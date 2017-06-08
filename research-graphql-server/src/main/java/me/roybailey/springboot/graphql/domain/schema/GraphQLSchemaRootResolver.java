package me.roybailey.springboot.graphql.domain.schema;

import com.coxautodev.graphql.tools.GraphQLRootResolver;
import me.roybailey.data.schema.ProductDto;
import me.roybailey.springboot.service.ProductAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GraphQLSchemaRootResolver implements GraphQLRootResolver {

    @Autowired
    ProductAdaptor productAdaptor;

    public List<ProductDto> products() {

        return productAdaptor.getAllProducts();
    }

}
