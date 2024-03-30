package com.sppart.admin.exhibition.domain.entity;

import lombok.Getter;

@Getter
public enum ExhibitionStatus {
    current("전시중"),
    end("전시 종료");

    private final String value;

    ExhibitionStatus(String value) {
        this.value = value;
    }
}
