package com.dusan.taxiservice.core.entity.converter;

import javax.persistence.AttributeConverter;

import com.dusan.taxiservice.core.entity.enums.Gender;

public class GenderConverter implements AttributeConverter<Gender, Character> {

    @Override
    public Character convertToDatabaseColumn(Gender attribute) {
        return Gender.toCharacter(attribute);
    }

    @Override
    public Gender convertToEntityAttribute(Character dbData) {
        return Gender.fromCharacter(dbData);
    }
}
