package com.ctflearn.dataobjects;

import com.ctflearn.utils.RandomValueGenerator;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewChallengeInfo {

    private static final int TEXT_LENGTH = 30;
    private String event;
    private String title;
    private String flag;
    private String description;
    private String category;
    private String points;
    private String content;

    public NewChallengeInfo() {
        this.event = "CTFlearn";
        this.title = "Title - " + RandomValueGenerator.getRandomAlphaNumberStringFixedLength(TEXT_LENGTH);
        this.flag = "CTFlearn{" + RandomValueGenerator.getRandomAlphaNumberStringFixedLength(TEXT_LENGTH) + "}";
        this.description = "Description - " + RandomValueGenerator.getRandomAlphaNumberStringFixedLength(TEXT_LENGTH);
        this.category = "Cryptography";
        this.points = "50";
        this.content = "Content - " + RandomValueGenerator.getRandomAlphaNumberStringFixedLength(TEXT_LENGTH);
    }
}
