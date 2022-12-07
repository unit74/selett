package com.selett.server.api.main.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePositionCoverLetterDiffListRequest {
    @NotNull(message = "자기소개서 번호가 null 이어서는 안됩니다.")
    private Integer id;

    @NotNull(message = "이동할 리스트의 번호가 null 이어서는 안됩니다.")
    @JsonProperty("to_move_list_id")
    private Integer toMoveListId;
}
