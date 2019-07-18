package com.dan.library.entity;

/**
 * @fileName: BaseEntity
 * @author: Bo
 * @createDate: 2019-01-07 11:15.
 * @description: 父类实体类
 */
public class BaseLongEntity extends BaseEntity {

    private Long Id;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
