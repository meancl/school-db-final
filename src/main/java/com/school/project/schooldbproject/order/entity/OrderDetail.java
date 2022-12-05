package com.school.project.schooldbproject.order.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long quantity;

    private Long totalPrice;


    /**
     * FK Payment Many to one
     * */


    /**
     * FK Catalogue Many to one
     */
    private Long catalogueId;
}