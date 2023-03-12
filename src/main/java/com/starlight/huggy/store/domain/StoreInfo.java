package com.starlight.huggy.store.domain;

import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class StoreInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long storeInfo_id;
    private Long store_id;

    @Embedded
    Location location;

    private Integer hug_unit; // 허깅 단위
    private Integer hug_price; // 메뉴 금액

    private Integer remain_hug_cnt; // 잔여 허깅 수
    private Integer hug_monthly; //이달의 허깅 합계
}
