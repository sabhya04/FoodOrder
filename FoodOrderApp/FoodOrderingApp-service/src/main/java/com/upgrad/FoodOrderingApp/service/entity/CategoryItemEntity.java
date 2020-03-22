package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "category_item")

@NamedQueries({
        @NamedQuery(
                name = "getItemByCategory",
                query = "select c from CategoryItemEntity c join ItemEntity i on c.itemId.Id = i.Id where c.categoryId.uuid = :uuid"
        )
})
public class CategoryItemEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Integer Id;

    @OneToOne(cascade = CascadeType.REMOVE)
    @NotNull
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private ItemEntity itemId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @NotNull
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryEntity categoryId;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public ItemEntity getItemId() {
        return itemId;
    }

    public void setItemId(ItemEntity itemId) {
        this.itemId = itemId;
    }

    public CategoryEntity getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(CategoryEntity categoryId) {
        this.categoryId = categoryId;
    }
}
