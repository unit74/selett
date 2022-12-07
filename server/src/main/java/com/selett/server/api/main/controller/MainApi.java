package com.selett.server.api.main.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.selett.server.api.main.dto.MainRequest;
import com.selett.server.api.main.dto.MainResponse;
import com.selett.server.api.main.dto.create.CreateCoverLetterRequest;
import com.selett.server.api.main.dto.create.CreateListRequest;
import com.selett.server.api.main.dto.delete.DeleteCoverLetterRequest;
import com.selett.server.api.main.dto.delete.DeleteListRequest;
import com.selett.server.api.main.dto.spell.SpellCheckRequest;
import com.selett.server.api.main.dto.update.*;
import com.selett.server.api.main.service.MainService;
import com.selett.server.utils.RequestTokenValidation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;

@AllArgsConstructor
@RestController
@RequestMapping("")
public class MainApi {
    private final MainService mainService;
    private final RequestTokenValidation requestTokenValidation;

    @GetMapping("")
    @Operation(summary = "모든 자기소개서 불러오기", description = "해당 유저의 자기소개서 정보를 불러옵니다." +
            "<br/>" +
            "<br/>" +
            "조회할 유저의 번호를 넣어주세요.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK",
                            content = @Content(schema = @Schema(implementation = MainResponse.class)))
            }
    )
    @io.swagger.annotations.ApiResponses(
            @io.swagger.annotations.ApiResponse(
                    response = MainResponse.class, message = "OK", code = 200)
    )
    public ResponseEntity<?> getListAndCoverLetter(@Valid MainRequest mainRequest) {
        try {
            requestTokenValidation.verify(mainRequest.getUserId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        MainResponse mainResponse = mainService.getListAndCoverLetter(mainRequest.getUserId());

        return new ResponseEntity<>(mainResponse, HttpStatus.OK);
    }

    @PostMapping("/lists")
    @Operation(summary = "리스트 생성", description = "리스트를 생성합니다." +
            "<br/>" +
            "<br/>" +
            "생성할 유저의 번호를 넣어주세요."
    )
    public ResponseEntity<?> createList(@Valid @RequestBody CreateListRequest createListRequest) {
        try {
            requestTokenValidation.verify(createListRequest.getUserId());

            mainService.existListTitle(createListRequest.getUserId(), createListRequest.getTitle());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        mainService.createList(createListRequest.getUserId(), createListRequest.getTitle());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/cover-letters")
    @Operation(summary = "자기소개서 생성", description = "자기소개서를 생성합니다." +
            "<br/>" +
            "<br/>" +
            "생성항 리스트의 번호를 넣어주세요."
    )
    public ResponseEntity<?> createCoverLetter(@Valid @RequestBody CreateCoverLetterRequest createCoverLetterRequest) {
        try {
            requestTokenValidation.verifyList(createCoverLetterRequest.getListId());

            mainService.existCoverLetterTitle(createCoverLetterRequest.getListId(), createCoverLetterRequest.getTitle());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        mainService.createCoverLetter(createCoverLetterRequest.getListId(), createCoverLetterRequest.getTitle());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/lists")
    @Operation(summary = "리스트 삭제", description = "리스트를 삭제합니다." +
            "<br/>" +
            "<br/>" +
            "삭제할 리스트의 번호를 넣어주세요."
    )
    public ResponseEntity<?> deleteList(@Valid DeleteListRequest deleteListRequest) {
        try {
            requestTokenValidation.verifyList(deleteListRequest.getListId());

            mainService.isOnlyOneList(deleteListRequest.getListId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        mainService.deleteList(deleteListRequest.getListId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/cover-letters")
    @Operation(summary = "자기소개서 삭제", description = "자기소개서를 삭제합니다." +
            "<br/>" +
            "<br/>" +
            "삭제할 자기소개서의 번호를 넣어주세요."
    )
    public ResponseEntity<?> deleteCoverLetter(@Valid DeleteCoverLetterRequest deleteCoverLetterRequest) {
        try {
            requestTokenValidation.verifyCoverLetter(deleteCoverLetterRequest.getId());

            mainService.isOnlyOneCoverLetter(deleteCoverLetterRequest.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        mainService.deleteCoverLetter(deleteCoverLetterRequest.getId());

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/lists")
    @Operation(summary = "리스트 갱신", description = "리스트를 갱신합니다." +
            "<br/>" +
            "<br/>" +
            "갱신할 리스트의 번호와 갱신할 정보를 입력해주세요."
    )
    public ResponseEntity<?> updateList(@Valid @RequestBody UpdateListRequest updateListRequest) {
        try {
            requestTokenValidation.verifyList(updateListRequest.getListId());

            mainService.existListTitle(mainService.getUserIdFromListId(updateListRequest.getListId()), updateListRequest.getTitle());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        mainService.updateList(updateListRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/cover-letters")
    @Operation(summary = "자기소개서 갱신", description = "자기소개서를 갱신합니다." +
            "<br/>" +
            "<br/>" +
            "갱신할 자기소개서의 번호와 갱신할 정보를 입력해주세요."
    )
    public ResponseEntity<?> updateCoverLetter(@Valid @RequestBody UpdateCoverLetterRequest updateCoverLetterRequest) {
        try {
            requestTokenValidation.verifyCoverLetter(updateCoverLetterRequest.getId());

            mainService.existCoverLetterTitle(mainService.getListIdFromId(updateCoverLetterRequest.getId()), updateCoverLetterRequest.getTitle());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        mainService.updateCoverLetter(updateCoverLetterRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/lists/position")
    @Operation(summary = "리스트 위치 이동", description = "리스트 위치를 이동합니다." +
            "<br/>" +
            "<br/>" +
            "이동할 리스트 번호와 이동할 위치의 이전 리스트 번호와 다음 리스트 번호를 입력해주세요." +
            "<br/>" +
            "(끝은 null 입니다.)"
    )
    public ResponseEntity<?> updatePostionList(@Valid @RequestBody UpdatePositionListRequest updatePositionListRequest) {
        try {
            requestTokenValidation.verifyList(updatePositionListRequest.getListId());
            if (updatePositionListRequest.getToMovePrevListId() != null) {
                requestTokenValidation.verifyList(updatePositionListRequest.getToMovePrevListId());
            }
            if (updatePositionListRequest.getToMoveNextListId() != null) {
                requestTokenValidation.verifyList(updatePositionListRequest.getToMoveNextListId());
            }

            mainService.isOnlyOneList(updatePositionListRequest.getListId());

            mainService.checkSafeList(updatePositionListRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        mainService.updatePositionList(updatePositionListRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/cover-letters/position")
    @Operation(summary = "자기소개서 위치 이동", description = "자기소개서를 이동합니다." +
            "<br/>" +
            "<br/>" +
            "이동할 자기소개서 번호와 이동할 위치의 이전 자기소개서 번호와 다음 자기소개서 번호를 입력해주세요." +
            "<br/>" +
            "(끝은 null 입니다.)"
    )
    public ResponseEntity<?> updatePositionCoverLetter(@Valid @RequestBody UpdatePositionCoverLetterRequest updatePositionCoverLetterRequest) {
        try {
            requestTokenValidation.verifyCoverLetter(updatePositionCoverLetterRequest.getId());
            if (updatePositionCoverLetterRequest.getToMovePrevId() != null) {
                requestTokenValidation.verifyCoverLetter(updatePositionCoverLetterRequest.getToMovePrevId());
            }
            if (updatePositionCoverLetterRequest.getToMoveNextId() != null) {
                requestTokenValidation.verifyCoverLetter(updatePositionCoverLetterRequest.getToMoveNextId());
            }

            mainService.isOnlyOneCoverLetter(updatePositionCoverLetterRequest.getId());

            mainService.checkSafeCoverLetter(updatePositionCoverLetterRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        mainService.updatePositionCoverLetter(updatePositionCoverLetterRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/cover-letters/position/lists")
    @Operation(summary = "자기소개서 리스트 이동", description = "자기소개서를 다른 리스트로 이동합니다." +
            "<br/>" +
            "<br/>" +
            "이동할 자기소개서 번호와 이동할 리스트의 번호를 입력해주세요."
    )
    public ResponseEntity<?> updatePositionCoverLetterDiffList(@Valid @RequestBody UpdatePositionCoverLetterDiffListRequest updatePositionCoverLetterDiffListRequest) {
        try {
            requestTokenValidation.verifyCoverLetter(updatePositionCoverLetterDiffListRequest.getId());
            requestTokenValidation.verifyList(updatePositionCoverLetterDiffListRequest.getToMoveListId());

            mainService.isOnlyOneCoverLetter(updatePositionCoverLetterDiffListRequest.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        mainService.updatePositionCoverLetterDiffList(updatePositionCoverLetterDiffListRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/spell-check")
    @Operation(summary = "맞춤법 검사", description = "맞춤법을 검사합니다." +
            "<br/>" +
            "검사하고 싶은 자기소개서의 id를 입력하세요." +
            "<br/>" +
            "<br/>" +
            "errorIdx - 교정 순서" +
            "<br/>" +
            "correctMethod - 교정 방법 분류" +
            "<br/>" +
            "orgStr - 입력 내용" +
            "<br/>" +
            "candWord - 대치어 (2개 이상이면 '|'로 구분)" +
            "<br/>" +
            "help - 도움말" +
            "<br/>" +
            "start, end - 원문에서 입력어 위치"
    )
    public ResponseEntity<?> spellCheck(@Valid SpellCheckRequest spellCheckRequest) throws JsonProcessingException, URISyntaxException {
        try {
            requestTokenValidation.verifyCoverLetter(spellCheckRequest.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body(mainService.spellCheck(spellCheckRequest));
    }
}
