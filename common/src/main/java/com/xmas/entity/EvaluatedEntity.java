package com.xmas.entity;

import java.time.LocalDateTime;

public interface EvaluatedEntity {
    void setParent(Object parent);
    void setDate(LocalDateTime date);
}
