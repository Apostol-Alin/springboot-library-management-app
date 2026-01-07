package aapostol.libraryManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ClientRequest {
    @NotBlank(message = "Client name cannot be blank")
    @Size(max = 255, message = "Client name must be less than 256 characters")
    @Schema(description = "Name of the client", example = "John Doe")
    private String name;

    @NotBlank(message = "Phone number cannot be blank")
    @Size(max = 20, message = "Phone number must be less than 20 characters")
    @Schema(description = "Phone number of the client", example = "+1234567890")
    private String phone;

    public ClientRequest() {}

    public ClientRequest(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
