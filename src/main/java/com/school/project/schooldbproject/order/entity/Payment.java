package com.school.project.schooldbproject.order.entity;

import com.school.project.schooldbproject.admin.dto.SalesDto;
import com.school.project.schooldbproject.branch.entity.Branch;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Todo: Remove after
 */
@SqlResultSetMapping(
        name = "BranchesSalesMapping",
        classes = @ConstructorResult(
                targetClass = SalesDto.BranchSalesDto.class,
                columns = {
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "totalSales", type = Long.class),
                })
)
@SqlResultSetMapping(
        name = "CataloguesSalesMapping",
        classes = @ConstructorResult(
                targetClass = SalesDto.BranchSalesDto.class,
                columns = {
                        @ColumnResult(name = "name", type = String.class),
                        @ColumnResult(name = "totalSales", type = Long.class),
                })
)
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalPrice;

    @CreatedDate
    private Date createdAt;

    //    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    //    @JsonManagedReference
    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<OrderDetail>();


    public void setBranch(Branch branch) {
        this.branch = branch;
        branch.getPayments().add(this);
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetails.add(orderDetail);
        orderDetail.setPayment(this);
    }

    public static Payment createPayment(Branch branch, List<OrderDetail> orderDetails) {
        Payment payment = new Payment();

        payment.setBranch(branch);
        orderDetails.forEach(orderDetail -> {
            payment.addOrderDetail(orderDetail);
            orderDetail.setPayment(payment);
        });
        payment.setCreatedAt(new Date());
        payment.setTotalPrice(payment.getTotalPrice());

        return payment;
    }

    public Long getTotalPrice() {
        return orderDetails.stream().mapToLong(OrderDetail::getTotalPrice).sum();
    }


}
