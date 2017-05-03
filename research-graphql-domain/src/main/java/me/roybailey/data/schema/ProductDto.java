
package me.roybailey.data.schema;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * A Product following the convention of http://microformats.org/wiki/h-product
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "brand",
    "category",
    "description",
    "price"
})
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class ProductDto {

    @JsonProperty("id")
    private String id;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    private String name;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("category")
    @lombok.Builder.Default
private List<String> category = new ArrayList<String>();
    @JsonProperty("description")
    private String description;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("price")
    private BigDecimal price;

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
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("brand")
    public String getBrand() {
        return brand;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("brand")
    public void setBrand(String brand) {
        this.brand = brand;
    }

    @JsonProperty("category")
    public List<String> getCategory() {
        return category;
    }

    @JsonProperty("category")
    public void setCategory(List<String> category) {
        this.category = category;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("price")
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("price")
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(name).append(brand).append(category).append(description).append(price).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ProductDto) == false) {
            return false;
        }
        ProductDto rhs = ((ProductDto) other);
        return new EqualsBuilder().append(id, rhs.id).append(name, rhs.name).append(brand, rhs.brand).append(category, rhs.category).append(description, rhs.description).append(price, rhs.price).isEquals();
    }

}
