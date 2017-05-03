package me.roybailey.springboot.mapper;


import lombok.extern.slf4j.Slf4j;
import me.roybailey.data.schema.OrderDto;
import me.roybailey.data.schema.OrderItemDto;
import me.roybailey.springboot.domain.OrderForm;
import me.roybailey.springboot.domain.OrderItem;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class OrderMapper {

    final ModelMapper mapper;

    public OrderMapper() {
        mapper = new ModelMapper();
        // use strict to prevent over eager matching (happens with ID fields)
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        // create PropertyMap<source,target> here and add to mapper
        mapper.addMappings(new PropertyMap<OrderForm, OrderDto>() {
            @Override
            protected void configure() {
            }
        });
        mapper.addMappings(new PropertyMap<OrderDto, OrderForm>() {
            @Override
            protected void configure() {
            }
        });
        mapper.addMappings(new PropertyMap<OrderItemDto, OrderItem>() {
            @Override
            protected void configure() {
            }
        });
        // need to add the parent link to jpa order items (as these don't exist in dto form)
        mapper.getTypeMap(OrderDto.class, OrderForm.class)
                .setPostConverter(context -> {
                    OrderForm target = context.getDestination();
                    log.info("post-converter fixing OrderItem parent links {}", target);
                    if (target.getItems() != null) {
                        target.getItems().stream().forEach((item) -> item.setOrder(target));
                    }
                    return target;
                });
    }

    public OrderDto toOrderDto(OrderForm order) {
        return this.mapper.map(order, OrderDto.class);
    }

    public OrderForm toOrderForm(OrderDto order) {
        return this.mapper.map(order, OrderForm.class);
    }

    public List<OrderDto> toOrderDtoList(List<OrderForm> orders) {
        return this.mapper.map(orders, new TypeToken<List<OrderDto>>() {
        }.getType());
    }
}
