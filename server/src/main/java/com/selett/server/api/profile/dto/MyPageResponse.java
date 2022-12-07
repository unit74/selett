package com.selett.server.api.profile.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MyPageResponse {
    List<Award> awards;
    List<Education> educations;
    List<License> licenses;
    Memo memo;
    @JsonProperty("language_skills")
    List<LanguageSkill> languageSkills;
}
