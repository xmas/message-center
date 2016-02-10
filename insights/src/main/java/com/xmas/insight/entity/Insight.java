package com.xmas.insight.entity;

import com.xmas.util.NonePredefinedAttributesConverter;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "insights")
@Data
public class Insight {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long question;

    @ElementCollection
    @CollectionTable(name = "insight_answers", joinColumns = @JoinColumn(name = "insight"))
    private List<Long> answers;

    @ElementCollection
    @CollectionTable(name="insight_parameters", joinColumns=@JoinColumn(name="insight"))
    @MapKeyColumn(name="name")
    @Column(name="value")
    @Convert(converter = NonePredefinedAttributesConverter.class, attributeName = "value")
    private Map<String, Object> parameters;

    @Column
    private LocalDateTime date;

    @Column
    private String title;

    @Column
    private String details;

    @Column
    private String source;
}
