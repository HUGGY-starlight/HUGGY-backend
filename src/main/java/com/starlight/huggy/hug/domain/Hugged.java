package com.starlight.huggy.hug.domain;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Hugged {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long hugged_id;

    private Long user_id; // sponser_id
    private Long store_id;
    private Long hugged_unit; // 후원자가 기부한 금액
    private Long hugged_total; // 제공받은 메뉴 금액

    private LocalDateTime verified_at;
    private LocalDateTime created_at;

}
