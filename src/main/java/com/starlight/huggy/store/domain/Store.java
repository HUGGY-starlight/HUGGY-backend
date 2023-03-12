package com.starlight.huggy.store.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long store_id;

    @Enumerated(EnumType.STRING)
    private StoreCategory category;

    @Column(nullable = false, length = 20)
    private String store_name;

    @Column(length = 100)
    private String store_detail;

    @Column(nullable = false, name = "owner_id")
    private Long member_id;

    private boolean isVerified;
    private boolean isAvailable;

    private LocalDateTime created_at;
    private LocalDateTime updated_at;

}
