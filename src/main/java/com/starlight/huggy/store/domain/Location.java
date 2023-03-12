package com.starlight.huggy.store.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Location {
    private String latitude; // 위도
    private String longitude; // 경도

}
