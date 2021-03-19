package com.mediscreen.patients.api.model;

import com.mediscreen.common.api.validation.group.Update;
import com.mediscreen.patients.api.validation.constraint.IsAddress;
import com.mediscreen.patients.api.validation.constraint.IsCity;
import com.mediscreen.patients.api.validation.constraint.IsCountryCode;
import com.mediscreen.patients.api.validation.constraint.IsName;
import com.mediscreen.patients.api.validation.constraint.IsPhoneNumber;
import com.mediscreen.patients.api.validation.constraint.IsPostalCode;
import com.mediscreen.patients.api.validation.constraint.IsState;
import java.time.LocalDate;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.With;

@Builder(builderClassName = "Builder", toBuilder = true)
@Value
@With
@AllArgsConstructor(onConstructor = @__(@Deprecated)) // intended to be used only by tools (MapStruct, Jackson, etc)
public class Patient {
    UUID id;

    @NotNull(groups = Update.class)
    @IsName(groups = Update.class, strict = true)
    String firstName;

    @NotNull(groups = Update.class)
    @IsName(groups = Update.class, strict = true)
    String lastName;

    LocalDate birthdate;

    Gender gender;

    @IsPhoneNumber
    String phone;

    @IsAddress(strict = true)
    String address;

    @IsCity(strict = true)
    String city;

    @IsPostalCode
    String postalCode;

    @IsState(strict = true)
    String state;

    @IsCountryCode
    String countryCode;
}
