package com.upgrad.quora.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * SignUpUserResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen")

public class SignUpUserResponse {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("status")
    private String status = null;

    public SignUpUserResponse id(String id) {
        this.id = id;
        return this;
    }

    /**
     * uuid of the signed up user
     * @return id
     **/
    @ApiModelProperty(required = true, value = "uuid of the signed up user")
    @NotNull


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SignUpUserResponse status(String status) {
        this.status = status;
        return this;
    }

    /**
     * message showing the status of the signed up user
     * @return status
     **/
    @ApiModelProperty(required = true, value = "message showing the status of the signed up user")
    @NotNull


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SignUpUserResponse signupUserResponse = (SignUpUserResponse) o;
        return Objects.equals(this.id, signupUserResponse.id) &&
                Objects.equals(this.status, signupUserResponse.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class SignUpUserResponse {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    status: ").append(toIndentedString(status)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
