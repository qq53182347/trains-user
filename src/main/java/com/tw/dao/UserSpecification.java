package com.tw.dao;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification {

    String name;
    Integer age;
    String createdAtFrom;
    String createdAtTo;

    public UserSpecification(String name, Integer age, String beginDateTime, String endDateTime) {
        this.name = name;
        this.age = age;
        this.createdAtFrom = beginDateTime;
        this.createdAtTo = endDateTime;
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        if (name != null && !"".equals(name)) {
            predicates.add(builder.like(root.get("name"), name + "%"));
        }
        if (age != null) {
            predicates.add(builder.equal(root.get("age"), age));
        }
        boolean queryTimeExist = null != createdAtFrom && !"".equals(createdAtFrom)
                && null != createdAtTo && !"".equals(createdAtTo);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (queryTimeExist) {
            try {
                predicates.add(builder.between(root.get("createdAt"),
                        LocalDateTime.parse(sdf.format(sdf.parse(createdAtFrom)),df),
                        LocalDateTime.parse(sdf.format(sdf.parse(createdAtTo).getTime()+86400000), df)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return builder.and(predicates.toArray(new Predicate[predicates.size()]));
    }

}
