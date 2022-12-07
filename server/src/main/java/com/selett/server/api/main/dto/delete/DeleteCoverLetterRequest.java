package com.selett.server.api.main.dto.delete;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCoverLetterRequest {
    @NotNull(message = "자기소개서 번호가 null 이어서는 안됩니다.")
    private Integer id;
}
