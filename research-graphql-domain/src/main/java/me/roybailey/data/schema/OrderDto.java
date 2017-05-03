
package me.roybailey.data.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * An Order instruction belonging to http://microformats.org/wiki/h-card for http://microformats.org/wiki/h-product
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "userId",
    "items",
    "status"
})
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class OrderDto {

    @JsonProperty("id")
    private String id;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("items")
    @lombok.Builder.Default
private List<OrderItemDto> items = new ArrayList<OrderItemDto>();
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("status")
    private OrderDto.Status status;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonProperty("items")
    public List<OrderItemDto> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("status")
    public OrderDto.Status getStatus() {
        return status;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("status")
    public void setStatus(OrderDto.Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(userId).append(items).append(status).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OrderDto) == false) {
            return false;
        }
        OrderDto rhs = ((OrderDto) other);
        return new EqualsBuilder().append(id, rhs.id).append(userId, rhs.userId).append(items, rhs.items).append(status, rhs.status).isEquals();
    }

    public enum Status {

        SUBMITTED("SUBMITTED"),
        PAYED("PAYED"),
        DISPATCHED("DISPATCHED");
        private final String value;
        private final static Map<String, OrderDto.Status> CONSTANTS = new HashMap<String, OrderDto.Status>();

        static {
            for (OrderDto.Status c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static OrderDto.Status fromValue(String value) {
            OrderDto.Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
