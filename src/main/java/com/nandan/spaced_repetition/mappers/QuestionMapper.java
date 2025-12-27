package com.nandan.spaced_repetition.mappers;


import com.nandan.spaced_repetition.dto.QuestionListResponse;
import com.nandan.spaced_repetition.dto.QuestionResponseDTO;
import com.nandan.spaced_repetition.entity.QuestionEntity;
import com.nandan.spaced_repetition.entity.TopicTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    // API -> ENTITY (for syncing to DB)
    @Mapping(source = "paidOnly", target = "isPaidOnly")
    @Mapping(target = "problemUrl", expression = "java(dto.getProblemUrl())")
    QuestionEntity apiDtoToEntity(QuestionListResponse.Question dto);

    TopicTag apiTagToEntityTag(QuestionListResponse.TopicTag dtoTag);


    @Mapping(source = "isPaidOnly", target = "isPaidOnly")
    QuestionResponseDTO entityToResponseDTO(QuestionEntity entity);

}