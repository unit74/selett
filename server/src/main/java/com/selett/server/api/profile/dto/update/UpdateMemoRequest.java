package com.selett.server.api.profile.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdateMemoRequest {
    @NotNull(message = "갱신할 번호가 null 이어서는 안됩니다.")
    private Integer id;
    @Size(max = 5000, message = "내용은 5000자까지 가능합니다.")
    private String description;
}
