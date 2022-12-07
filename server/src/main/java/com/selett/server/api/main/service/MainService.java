package com.selett.server.api.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.selett.server.api.main.dto.CoverLetter;
import com.selett.server.api.main.dto.FolderList;
import com.selett.server.api.main.dto.MainResponse;
import com.selett.server.api.main.dto.spell.SpellCheckRequest;
import com.selett.server.api.main.dto.update.*;
import com.selett.server.jpa.mapper.CoverLetterEntity;
import com.selett.server.jpa.mapper.ListEntity;
import com.selett.server.jpa.repository.CoverLetterRepository;
import com.selett.server.jpa.repository.ListRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@AllArgsConstructor
public class MainService {
    private final ListRepository listRepository;
    private final CoverLetterRepository coverLetterRepository;
    private final EntityManager em;
    private final String errorMsgOverLap = "중복된 제목입니다.";
    private final String errorMsgRemainOne = "1개일 때는 이동 또는 삭제할 수 없습니다.";
    private final String errorMsgMoveNull = "이동할 위치가 둘 다 null이면 안됩니다.";
    private final String errorMsgMoveOwn = "이동할 아이디와 이동할 위치의 아이디가 같아야 합니다.";
    private final String errorMsgConnected = "이동할 위치가 연결되어 있어야 합니다.";

    private void setCoverLetterNext(Integer id, Integer to) {
        if (id != null) {
            coverLetterRepository.findById(id).ifPresent(coverLetter -> {
                coverLetter.setNext(to);
                coverLetterRepository.saveAndFlush(coverLetter);
            });
        }
    }

    private void setCoverLetterPrev(Integer id, Integer to) {
        if (id != null) {
            coverLetterRepository.findById(id).ifPresent(coverLetter -> {
                coverLetter.setPrev(to);
                coverLetterRepository.saveAndFlush(coverLetter);
            });
        }
    }

    private void setListNext(Integer id, Integer to) {
        if (id != null) {
            ListEntity listEntity = listRepository.findByListId(id);
            listEntity.setNext(to);
            listRepository.saveAndFlush(listEntity);
        }
    }

    private void setListPrev(Integer id, Integer to) {
        if (id != null) {
            ListEntity listEntity = listRepository.findByListId(id);
            listEntity.setPrev(to);
            listRepository.saveAndFlush(listEntity);
        }
    }

    public void existListTitle(Integer userId, String title) {
        if (listRepository.existsByUserIdAndTitle(userId, title)) {
            throw new IllegalArgumentException(errorMsgOverLap);
        }
    }

    public Integer getUserIdFromListId(Integer listId) {
        ListEntity list = listRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 아이디입니다."));

        return list.getUserId();
    }

    public Integer getListIdFromId(Integer id) {
        CoverLetterEntity coverLetter = coverLetterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 아이디입니다."));

        return coverLetter.getListId();
    }

    public void existCoverLetterTitle(Integer listId, String title) {
        if (coverLetterRepository.existsByListIdAndTitle(listId, title)) {
            throw new IllegalArgumentException(errorMsgOverLap);
        }
    }

    public void isOnlyOneList(Integer listId) {
        listRepository.findById(listId).ifPresent(list -> {
            Integer userId = list.getUserId();

            if (listRepository.countByUserId(userId) == 1) {
                throw new IllegalArgumentException(errorMsgRemainOne);
            }
        });
    }

    public void isOnlyOneCoverLetter(Integer id) {
        coverLetterRepository.findById(id).ifPresent(coverLetter -> {
            Integer listId = coverLetter.getListId();

            if (coverLetterRepository.countByListId(listId) == 1) {
                throw new IllegalArgumentException(errorMsgRemainOne);
            }
        });
    }

    public void checkSafeList(UpdatePositionListRequest updatePositionListRequest) {
        Integer listId = updatePositionListRequest.getListId();
        Integer toMovePrevListId = updatePositionListRequest.getToMovePrevListId();
        Integer toMoveNextListId = updatePositionListRequest.getToMoveNextListId();

        if (toMoveNextListId == null && toMovePrevListId == null) {
            throw new IllegalArgumentException(errorMsgMoveNull);
        }
        if (Objects.equals(listId, toMoveNextListId)) {
            throw new IllegalArgumentException(errorMsgMoveOwn);
        }
        if (Objects.equals(listId, toMovePrevListId)) {
            throw new IllegalArgumentException(errorMsgMoveOwn);
        }

        if (toMovePrevListId != null) {
            listRepository.findById(toMovePrevListId).ifPresent(list -> {
                Integer next = list.getNext();

                if (!Objects.equals(next, toMoveNextListId)) {
                    throw new IllegalArgumentException(errorMsgConnected);
                }
            });
        }

        if (toMoveNextListId != null) {
            listRepository.findById(toMoveNextListId).ifPresent(list -> {
                Integer prev = list.getPrev();

                if (!Objects.equals(prev, toMovePrevListId)) {
                    throw new IllegalArgumentException(errorMsgConnected);
                }
            });
        }
    }

    public void checkSafeCoverLetter(UpdatePositionCoverLetterRequest updatePositionCoverLetterRequest) {
        Integer id = updatePositionCoverLetterRequest.getId();
        Integer toMovePrevId = updatePositionCoverLetterRequest.getToMovePrevId();
        Integer toMoveNextId = updatePositionCoverLetterRequest.getToMoveNextId();

        if (toMoveNextId == null && toMovePrevId == null) {
            throw new IllegalArgumentException(errorMsgMoveNull);
        }
        if (Objects.equals(id, toMoveNextId)) {
            throw new IllegalArgumentException(errorMsgMoveOwn);
        }
        if (Objects.equals(id, toMovePrevId)) {
            throw new IllegalArgumentException(errorMsgMoveOwn);
        }

        if (toMovePrevId != null) {
            coverLetterRepository.findById(toMovePrevId).ifPresent(list -> {
                Integer next = list.getNext();

                if (!Objects.equals(next, toMoveNextId)) {
                    throw new IllegalArgumentException(errorMsgConnected);
                }
            });
        }

        if (toMoveNextId != null) {
            coverLetterRepository.findById(toMoveNextId).ifPresent(list -> {
                Integer prev = list.getPrev();

                if (!Objects.equals(prev, toMovePrevId)) {
                    throw new IllegalArgumentException(errorMsgConnected);
                }
            });
        }
    }

    public MainResponse getListAndCoverLetter(Integer userId) {
        MainResponse response = new MainResponse(new ArrayList<>());

        List<ListEntity> listEntities = listRepository.findAllByUserId(userId);

        if (listEntities.isEmpty())
            return response;

        Map<Integer, ListEntity> listEntityMap = new HashMap<>();
        ListEntity nowList = new ListEntity();
        for (ListEntity listEntity : listEntities) {
            listEntityMap.put(listEntity.getListId(), listEntity);

            if (listEntity.getPrev() == null)
                nowList = listEntity;
        }

        List<FolderList> folderLists = new ArrayList<>();

        while (true) {
            List<CoverLetter> coverLetters = new ArrayList<>();
            List<CoverLetterEntity> coverLetterEntities = coverLetterRepository.findAllByListId(nowList.getListId());

            // 필수적으로 하나는 있는 상태이므로 없음을 검사하지 않음

            Map<Integer, CoverLetterEntity> coverLetterEntityMap = new HashMap<>();
            CoverLetterEntity nowCoverLetter = new CoverLetterEntity();
            for (CoverLetterEntity coverLetterEntity : coverLetterEntities) {
                coverLetterEntityMap.put(coverLetterEntity.getId(), coverLetterEntity);

                if (coverLetterEntity.getPrev() == null)
                    nowCoverLetter = coverLetterEntity;
            }

            while (true) {
                coverLetters.add(new CoverLetter(nowCoverLetter));

                if (nowCoverLetter.getNext() == null)
                    break;

                nowCoverLetter = coverLetterEntityMap.get(nowCoverLetter.getNext());
            }

            FolderList nowFolder = new FolderList(nowList, coverLetters);
            folderLists.add(nowFolder);

            if (nowList.getNext() == null)
                break;

            nowList = listEntityMap.get(nowList.getNext());
        }

        response.setList(folderLists);

        return response;
    }

    public void createList(Integer userId, String title) {
        ListEntity newListEntity = new ListEntity();
        newListEntity.setTitle(title);
        newListEntity.setPrev(null);
        newListEntity.setNext(null);
        newListEntity.setUserId(userId);

        if (listRepository.countByUserId(userId) == 0) {
            newListEntity = listRepository.saveAndFlush(newListEntity);
            createCoverLetter(newListEntity.getListId(), "새 자기소개서");

            return;
        }

        ListEntity lastListEntity = listRepository.findByUserIdAndNext(userId, null);

        newListEntity.setPrev(lastListEntity.getListId());
        newListEntity = listRepository.saveAndFlush(newListEntity);

        lastListEntity.setNext(newListEntity.getListId());
        listRepository.saveAndFlush(lastListEntity);

        createCoverLetter(newListEntity.getListId(), "새 자기소개서");
    }

    public void createCoverLetter(Integer listId, String title) {
        CoverLetterEntity newCoverLetterEntity = new CoverLetterEntity();
        newCoverLetterEntity.setTitle(title);
        newCoverLetterEntity.setQuestionLock(false);
        newCoverLetterEntity.setDescriptionLock(false);
        newCoverLetterEntity.setPrev(null);
        newCoverLetterEntity.setNext(null);
        newCoverLetterEntity.setListId(listId);

        if (coverLetterRepository.countByListId(listId) == 0) {
            coverLetterRepository.saveAndFlush(newCoverLetterEntity);

            return;
        }

        CoverLetterEntity lastCoverLetterEntity = coverLetterRepository.findByListIdAndNext(listId, null);

        newCoverLetterEntity.setPrev(lastCoverLetterEntity.getId());
        newCoverLetterEntity = coverLetterRepository.saveAndFlush(newCoverLetterEntity);

        lastCoverLetterEntity.setNext(newCoverLetterEntity.getId());
        coverLetterRepository.saveAndFlush(lastCoverLetterEntity);
    }

    @Transactional
    public void deleteList(Integer listId) {
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        listRepository.findById(listId).ifPresent(list -> {
            Integer prevId = list.getPrev();
            Integer nextId = list.getNext();

            setListNext(prevId, nextId);
            setListPrev(nextId, prevId);

            list.setPrev(null);
            list.setNext(null);

            listRepository.saveAndFlush(list);
            listRepository.deleteById(listId);
            listRepository.flush();
        });
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    @Transactional
    public void deleteCoverLetter(Integer id) {
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        coverLetterRepository.findById(id).ifPresent(coverLetter -> {
            Integer prevId = coverLetter.getPrev();
            Integer nextId = coverLetter.getNext();

            setCoverLetterNext(prevId, nextId);
            setCoverLetterPrev(nextId, prevId);

            coverLetter.setPrev(null);
            coverLetter.setNext(null);

            coverLetterRepository.saveAndFlush(coverLetter);
            coverLetterRepository.deleteById(id);
            coverLetterRepository.flush();
        });
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }

    public void updateList(UpdateListRequest updateListRequest) {
        Integer listId = updateListRequest.getListId();

        listRepository.findById(listId).ifPresent(list -> {
            if (updateListRequest.getTitle() != null) {
                list.setTitle(updateListRequest.getTitle());
            }

            listRepository.saveAndFlush(list);
        });
    }

    public void updateCoverLetter(UpdateCoverLetterRequest updateCoverLetterRequest) {
        Integer id = updateCoverLetterRequest.getId();

        coverLetterRepository.findById(id).ifPresent(coverLetter -> {
            if (updateCoverLetterRequest.getTitle() != null) {
                coverLetter.setTitle(updateCoverLetterRequest.getTitle());
            }
            if (updateCoverLetterRequest.getQuestion() != null) {
                coverLetter.setQuestion(updateCoverLetterRequest.getQuestion());
            }
            if (updateCoverLetterRequest.getQuestionLock() != null) {
                coverLetter.setQuestionLock(updateCoverLetterRequest.getQuestionLock());
            }
            if (updateCoverLetterRequest.getDescription() != null) {
                coverLetter.setDescription(updateCoverLetterRequest.getDescription());
            }
            if (updateCoverLetterRequest.getDescriptionLock() != null) {
                coverLetter.setDescriptionLock(updateCoverLetterRequest.getDescriptionLock());
            }
            coverLetterRepository.saveAndFlush(coverLetter);
        });
    }


    public void updatePositionList(UpdatePositionListRequest updatePositionListRequest) {
        Integer listId = updatePositionListRequest.getListId();
        Integer toMovePrevListId = updatePositionListRequest.getToMovePrevListId();
        Integer toMoveNextListId = updatePositionListRequest.getToMoveNextListId();

        listRepository.findById(listId).ifPresent(list -> {
            Integer currentPrevListId = list.getPrev();
            Integer currentNextListId = list.getNext();

            setListPrev(currentNextListId, currentPrevListId);
            setListNext(currentPrevListId, currentNextListId);

            setListPrev(toMoveNextListId, listId);
            setListNext(toMovePrevListId, listId);

            list.setPrev(toMovePrevListId);
            list.setNext(toMoveNextListId);

            listRepository.saveAndFlush(list);
        });
    }

    public void updatePositionCoverLetter(UpdatePositionCoverLetterRequest updatePositionCoverLetterRequest) {
        Integer id = updatePositionCoverLetterRequest.getId();
        Integer toMovePrevId = updatePositionCoverLetterRequest.getToMovePrevId();
        Integer toMoveNextId = updatePositionCoverLetterRequest.getToMoveNextId();

        coverLetterRepository.findById(id).ifPresent(coverLetter -> {
            Integer currentPrevId = coverLetter.getPrev();
            Integer currentNextId = coverLetter.getNext();

            setCoverLetterPrev(currentNextId, currentPrevId);
            setCoverLetterNext(currentPrevId, currentNextId);

            setCoverLetterPrev(toMoveNextId, id);
            setCoverLetterNext(toMovePrevId, id);

            coverLetter.setPrev(toMovePrevId);
            coverLetter.setNext(toMoveNextId);

            coverLetterRepository.saveAndFlush(coverLetter);
        });
    }

    public void updatePositionCoverLetterDiffList(UpdatePositionCoverLetterDiffListRequest updatePositionCoverLetterDiffListRequest) {
        coverLetterRepository.findById(updatePositionCoverLetterDiffListRequest.getId()).ifPresent(coverLetter -> {
            CoverLetterEntity lastCoverLetter = coverLetterRepository.findByListIdAndNext(updatePositionCoverLetterDiffListRequest.getToMoveListId(), null);

            Integer currentPrevId = coverLetter.getPrev();
            Integer currentNextId = coverLetter.getNext();

            setCoverLetterPrev(currentNextId, currentPrevId);
            setCoverLetterNext(currentPrevId, currentNextId);

            lastCoverLetter.setNext(coverLetter.getId());
            coverLetter.setListId(updatePositionCoverLetterDiffListRequest.getToMoveListId());
            coverLetter.setNext(null);

            coverLetterRepository.saveAndFlush(lastCoverLetter);
            coverLetterRepository.saveAndFlush(coverLetter);
        });
    }

    public Map spellCheck(SpellCheckRequest spellCheckRequest) throws JsonProcessingException, URISyntaxException {
        CoverLetterEntity coverLetter = coverLetterRepository.findById(spellCheckRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 아이디입니다."));

        if (coverLetter.getDescription() == null)
            return null;

        String replace = coverLetter.getDescription().replaceAll("\n", "\r\n");
        String encode = URLEncoder.encode(replace, StandardCharsets.UTF_8);

        RequestEntity<String> res = RequestEntity
                .post(new URI("http://164.125.7.61/speller/results"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body("text1=" + encode);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(res, String.class);

        ObjectMapper mapper = new ObjectMapper();
        if(response.toString().contains("기술적 한계로 찾지 못한 맞춤법 오류나 문법 오류가 있을 수 있습니다.")) {
            return mapper.readValue("{\"errInfo\":[]}", Map.class);
        }

        String data = "{" + Objects.requireNonNull(response.getBody()).substring(response.getBody().indexOf("\"errInfo\":[{"), response.getBody().indexOf("],\"idx\":") + 1) + "}";

        return mapper.readValue(data, Map.class);
    }
}
