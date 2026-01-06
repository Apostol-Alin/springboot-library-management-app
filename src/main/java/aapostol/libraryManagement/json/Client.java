package aapostol.libraryManagement.json;

import java.util.Date;

import org.json.JSONObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name", nullable = false, length = 255)
    @NotBlank(message = "Client name cannot be blank")
    @Size(max = 255, message = "Client name must be less than 256 characters")
    private String name;

    @Column(name = "Phone", nullable = false, length = 20, unique = true)
    @NotBlank(message = "Phone number cannot be blank")
    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phone;

    @Column(name = "registration_date", nullable = false)
    @NotNull(message = "Registration date cannot be null")
    @PastOrPresent(message = "Registration date cannot be in the future")
    private Date registration_date;

    public Client() {}

    public Client(String name, String phone, Date registration_date) {
        this.name = name;
        this.phone = phone;
        this.registration_date = registration_date;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public Date getRegistrationDate() {
        return registration_date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRegistrationDate(Date registration_date) {
        this.registration_date = registration_date;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("phone", phone);
        json.put("registration_date", registration_date);
        return json.toString();
    }
}
