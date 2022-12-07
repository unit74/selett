package com.selett.server.api.profile.service;

import com.selett.server.api.profile.dto.*;
import com.selett.server.api.profile.dto.update.*;
import com.selett.server.jpa.mapper.*;
import com.selett.server.jpa.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor

public class ProfileService {
    private final AwardRepository awardRepository;
    private final EducationRepository educationRepository;
    private final LicenseRepository licenseRepository;
    private final MemoRepository memoRepository;
    private final LanguageSkillRepository languageSkillRepository;

    public MyPageResponse getProfile(Integer userId) {
        MyPageResponse myPageResponse = new MyPageResponse();

        //award
        List<Award> awardResponse = new ArrayList<>();
        awardRepository.findAllByUserId(userId).forEach(award -> {
            Award awardInput = new Award();
            awardInput.setId(award.getId());
            awardInput.setTitle(award.getTitle());
            awardInput.setDate(award.getDate());
            awardInput.setOrganization(award.getOrganization());
            awardInput.setGrade(award.getGrade());
            awardInput.setDescription(award.getDescription());

            awardResponse.add(awardInput);
        });

        //License
        List<License> licenseResponse = new ArrayList<>();
        licenseRepository.findAllByUserId(userId).forEach(license -> {
            License licenseInput = new License();
            licenseInput.setId(license.getId());
            licenseInput.setTitle(license.getTitle());
            licenseInput.setDate(license.getDate());
            licenseInput.setDescription(license.getDescription());

            licenseResponse.add(licenseInput);
        });

        //Education
        List<Education> educationResponse = new ArrayList<>();
        educationRepository.findAllByUserId(userId).forEach(education -> {
            Education educationInput = new Education();
            educationInput.setId(education.getId());
            educationInput.setName(education.getName());
            educationInput.setMajor(education.getMajor());
            educationInput.setDegree(education.getDegree());
            educationInput.setAdmissionDate(education.getAdmissionDate());
            educationInput.setGraduationDate(education.getGraduationDate());
            educationInput.setMajorGrade(education.getMajorGrade());
            educationInput.setMajorCourse(education.getMajorCourse());
            educationInput.setGrade(education.getGrade());
            educationInput.setMaxGrade(education.getMaxGrade());
            educationInput.setCourse(education.getCourse());

            educationResponse.add(educationInput);
        });

        //Memo
        MemoEntity memo = memoRepository.findByUserId(userId);
        Memo memoInput = new Memo();
        memoInput.setId(memo.getId());
        memoInput.setDescription(memo.getDescription());

        myPageResponse.setMemo(memoInput);

        //LanguageSkill
        List<LanguageSkill> languageSkillResponse = new ArrayList<>();
        languageSkillRepository.findAllByUserId(userId).forEach(languageSkill -> {
            LanguageSkill languageSkillInput = new LanguageSkill();
            languageSkillInput.setId(languageSkill.getId());
            languageSkillInput.setTitle(languageSkill.getTitle());
            languageSkillInput.setGrade(languageSkill.getGrade());

            languageSkillResponse.add(languageSkillInput);
        });

        myPageResponse.setAwards(awardResponse);
        myPageResponse.setLicenses(licenseResponse);
        myPageResponse.setEducations(educationResponse);
        myPageResponse.setLanguageSkills(languageSkillResponse);

        return myPageResponse;
    }

    //Award
    public void postProfileAward(String title, LocalDate date, String organization, String grade, Integer userId, String description) {
        AwardEntity awardEntity = new AwardEntity();
        awardEntity.setTitle(title);
        awardEntity.setDate(date);
        awardEntity.setOrganization(organization);
        awardEntity.setGrade(grade);
        awardEntity.setDescription(description);
        awardEntity.setUserId(userId);

        awardRepository.save(awardEntity);
        awardRepository.flush();
    }

    //Education
    public void postProfileEducation(String name, String major, String degree, LocalDate admissionDate, LocalDate graduationDate,
                                     Float majorGrade, Integer majorCourse, Float grade, Float maxGrade, Integer course,Integer userId) {
        EducationEntity educationEntity = new EducationEntity();
        educationEntity.setName(name);
        educationEntity.setMajor(major);
        educationEntity.setDegree(degree);
        educationEntity.setAdmissionDate(admissionDate);
        educationEntity.setGraduationDate(graduationDate);
        educationEntity.setMajorGrade(majorGrade);
        educationEntity.setMajorCourse(majorCourse);
        educationEntity.setGrade(grade);
        educationEntity.setMaxGrade(maxGrade);
        educationEntity.setCourse(course);
        educationEntity.setUserId(userId);

        educationRepository.save(educationEntity);
        educationRepository.flush();
    }

    //LanguageSkill
    public void postProfileLanguageSkill(String title, String grade, Integer userId) {
        LanguageSkillEntity languageSkillEntity = new LanguageSkillEntity();
        languageSkillEntity.setTitle(title);
        languageSkillEntity.setGrade(grade);
        languageSkillEntity.setUserId(userId);

        languageSkillRepository.save(languageSkillEntity);
        languageSkillRepository.flush();
    }

    //License
    public void postProfileLicense(String title, LocalDate date, String description, Integer userId) {
        LicenseEntity licenseEntity = new LicenseEntity();
        licenseEntity.setTitle(title);
        licenseEntity.setDate(date);
        licenseEntity.setDescription(description);
        licenseEntity.setUserId(userId);

        licenseRepository.save(licenseEntity);
        licenseRepository.flush();
    }


    //Memo
    public void postProfileMemo(String description, Integer userId) {
        MemoEntity memoEntity = new MemoEntity();

        memoEntity.setDescription(description);
        memoEntity.setUserId(userId);

        memoRepository.save(memoEntity);
        memoRepository.flush();
    }


    public void deleteProfileAward(Integer id) {
        awardRepository.deleteById(id);
        awardRepository.flush();
    }
    public void deleteProfileEducation(Integer id) {
        educationRepository.deleteById(id);
        educationRepository.flush();
    }
    public void deleteProfileLanguage(Integer id) {
        languageSkillRepository.deleteById(id);
        languageSkillRepository.flush();
    }
    public void deleteProfileLicense(Integer id) {
        licenseRepository.deleteById(id);
        licenseRepository.flush();
    }

    public void updateProfileAward(UpdateAwardRequest updateAwardRequest) {
        awardRepository.findById(updateAwardRequest.getId()).ifPresent(award -> {
            if (updateAwardRequest.getTitle() != null) {
                award.setTitle(award.getTitle());
            }
            if (updateAwardRequest.getDate() != null) {
                award.setDate(award.getDate());
            }
            if (updateAwardRequest.getOrganization() != null) {
                award.setOrganization(award.getOrganization());
            }
            if (updateAwardRequest.getGrade() != null) {
                award.setGrade(award.getGrade());
            }
            if (updateAwardRequest.getDescription() != null) {
                award.setDescription(award.getDescription());
            }

            awardRepository.save(award);
            awardRepository.flush();
        });
    }

    public void updateProfileEducation(UpdateEducationRequest updateEducationRequest) {
        educationRepository.findById(updateEducationRequest.getId()).ifPresent(education -> {
            if (updateEducationRequest.getName() != null) {
                education.setName(education.getName());
            }
            if (updateEducationRequest.getMajor() != null) {
                education.setMajor(education.getMajor());
            }
            if (updateEducationRequest.getDegree() != null) {
                education.setDegree(education.getDegree());
            }
            if (updateEducationRequest.getAdmissionDate() != null) {
                education.setAdmissionDate(education.getAdmissionDate());
            }
            if (updateEducationRequest.getGraduationDate() != null) {
                education.setGraduationDate(education.getGraduationDate());
            }
            if (updateEducationRequest.getMajorGrade() != null) {
                education.setMajorGrade(education.getMajorGrade());
            }
            if (updateEducationRequest.getMajorCourse() != null) {
                education.setMajorCourse(education.getMajorCourse());
            }
            if (updateEducationRequest.getCourse() != null) {
                education.setGrade(education.getGrade());
            }
            if (updateEducationRequest.getMaxGrade() != null) {
                education.setMaxGrade(education.getMaxGrade());
            }
            if (updateEducationRequest.getCourse() != null) {
                education.setCourse(education.getCourse());
            }

            educationRepository.save(education);
            educationRepository.flush();
        });
    }

    public void updateProfileLanguageSkill(UpdateLanguageSkillRequest updateLanguageSkillRequest) {
        languageSkillRepository.findById(updateLanguageSkillRequest.getId()).ifPresent(languageSkill -> {
            if(languageSkill.getTitle() != null) {
                languageSkill.setTitle(languageSkill.getTitle());
            }
            if(languageSkill.getGrade() != null) {
                languageSkill.setGrade(languageSkill.getGrade());
            }

            languageSkillRepository.save(languageSkill);
            languageSkillRepository.flush();
        });
    }

    public void updateProfileLicense(UpdateLicenseRequest updateLicenseRequest) {
        licenseRepository.findById(updateLicenseRequest.getId()).ifPresent(license -> {
            if(license.getTitle() != null) {
                license.setTitle(license.getTitle());
            }
            if(license.getDate() != null) {
                license.setDate(license.getDate());
            }
            if(license.getDescription() != null) {
                license.setDescription(updateLicenseRequest.getDescription());
            }

            licenseRepository.save(license);
            licenseRepository.flush();
        });


    }

    public void updateProfileMemo(UpdateMemoRequest updateMemoRequest) {
        memoRepository.findById(updateMemoRequest.getId()).ifPresent(memo -> {
            if(updateMemoRequest.getDescription() != null) {
                memo.setDescription(updateMemoRequest.getDescription());
            }

            memoRepository.save(memo);
            memoRepository.flush();
        });
    }

}