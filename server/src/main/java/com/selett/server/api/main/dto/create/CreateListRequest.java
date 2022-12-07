package com.selett.server.api.main.dto.create;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateListRequest {
    @NotNull
    @JsonProperty("user_id")
    private Integer userId;

    @Size(max = 200)
    private String title;
}
