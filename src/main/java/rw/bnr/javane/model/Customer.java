package rw.bnr.javane.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers", uniqueConstraints = { @UniqueConstraint(columnNames = "email") }) // Ensure unique email
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Mobile is mandatory")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number should be 10 digits")
    private String mobile;

    @NotNull(message = "Date of birth is mandatory")
    @Past(message = "Date of birth must be a past date")
    private LocalDate dob;

    @NotBlank(message = "Account is mandatory")
    private String account;

    @NotNull(message = "Balance is mandatory")
    @PositiveOrZero(message = "Balance must be zero or positive")
    private Double balance;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime lastUpdatedTime;

    @PrePersist
    @PreUpdate
    private void validateAge() {
        if (dob != null) {
            int age = Period.between(dob, LocalDate.now()).getYears();
            if (age < 18) {
                throw new IllegalArgumentException("Customer must be at least 18 years old");
            }
        }
    }
}
