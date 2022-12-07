package com.selett.server.api.main.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.selett.server.jpa.mapper.ListEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderList {
    @JsonProperty("list_id")
    private Integer listId;

    private String title;

    @JsonProperty("cover_letter")
    private List<CoverLetter> coverLetter;

    public FolderList(ListEntity listEntity, List<CoverLetter> coverLetter) {
        listId = listEntity.getListId();
        title = listEntity.getTitle();
        this.coverLetter = coverLetter;
    }
}
