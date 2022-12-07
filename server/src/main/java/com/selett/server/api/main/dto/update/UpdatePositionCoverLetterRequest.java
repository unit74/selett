package com.selett.server.api.main.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePositionCoverLetterRequest {
    @NotNull(message = "자기소개서 번호가 null 이어서는 안됩니다.")
    private Integer id;

    @JsonProperty("to_move_prev_id")
    private Integer toMovePrevId;

    @JsonProperty("to_move_next_id")
    private Integer toMoveNextId;
}
