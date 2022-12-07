package com.selett.server.api.profile.dto.delete;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DeleteRequest {
    @NotNull(message = "삭제할 번호가 null 이어서는 안됩니다.")
    private Integer id;
}
