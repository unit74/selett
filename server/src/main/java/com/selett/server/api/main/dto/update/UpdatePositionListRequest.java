package com.selett.server.api.main.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePositionListRequest {
    @NotNull(message = "리스트 번호가 null 이어서는 안됩니다.")
    @JsonProperty("list_id")
    private Integer listId;

    @JsonProperty("to_move_prev_list_id")
    private Integer toMovePrevListId;

    @JsonProperty("to_move_next_list_id")
    private Integer toMoveNextListId;
}
