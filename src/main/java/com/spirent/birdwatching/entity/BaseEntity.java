package com.spirent.birdwatching.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Version;

// todo used for Optimistic Locking
@Getter
@Setter
public class BaseEntity {
    @Version
    private Long version;
}
