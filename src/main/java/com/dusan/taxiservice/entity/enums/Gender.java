package com.dusan.taxiservice.entity.enums;


public enum Gender {

    MALE('M'),

    FEMALE('F');

    private final char character;

    private Gender(char character) {
        this.character = character;
    }

    public static Gender fromCharacter(char character) {
        if (character == 'M')
            return Gender.MALE;
        if (character == 'F')
            return Gender.FEMALE;
        throw new IllegalArgumentException(character + " is not supported");
    }

    public static char toCharacter(Gender gender) {
        return gender.character;
    }

}
