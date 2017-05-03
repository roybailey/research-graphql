
package me.roybailey.data.schema;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * A Product following the convention of http://microformats.org/wiki/h-card
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "title",
    "given-name",
    "family-name",
    "bday",
    "email"
})
@lombok.Builder
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
public class UserDto {

    @JsonProperty("id")
    private String id;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("title")
    private String title;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("given-name")
    private String givenName;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("family-name")
    private String familyName;
    @JsonProperty("bday")
    private java.time.LocalDateTime bday;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("email")
    private String email;

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
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("given-name")
    public String getGivenName() {
        return givenName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("given-name")
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("family-name")
    public String getFamilyName() {
        return familyName;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("family-name")
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    @JsonProperty("bday")
    public java.time.LocalDateTime getBday() {
        return bday;
    }

    @JsonProperty("bday")
    public void setBday(java.time.LocalDateTime bday) {
        this.bday = bday;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("email")
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(title).append(givenName).append(familyName).append(bday).append(email).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof UserDto) == false) {
            return false;
        }
        UserDto rhs = ((UserDto) other);
        return new EqualsBuilder().append(id, rhs.id).append(title, rhs.title).append(givenName, rhs.givenName).append(familyName, rhs.familyName).append(bday, rhs.bday).append(email, rhs.email).isEquals();
    }

}
