package com.dan.dome.entity;

import com.dan.library.entity.BaseLongEntity;
import com.dan.library.util.JsonUtil;

import java.math.BigDecimal;

/**
 * Created by Bo on 2019/1/17 12:24
 */
public class Material extends BaseLongEntity {
    private String materialCode;
    private String materialName;
    private String materialBatch;
    private BigDecimal netWeight;
    private Double Similarity;
    private String batchFormat;
    private String ocrKey;
    /**
     * by:2019年2月14日15:03:04,新增
     */
    private Long batchFormatId;
    /**
     * 是否新增数据,为true为页面上新增数据
     */
    private Boolean tempFlag;
    /**
     * 修改所用的下标
     */
    private int positionIndex;
    /**
     * 用于新增修改->储存在map里面的临时ID
     */
    private Long tempId;
    /**
     * 数量
     */
    private BigDecimal storeQuantity;

    /**
     * 新增是的原料materialId
     */
    private Long materialId;

    /**
     * 包数
     */
    private Integer packageCount;

    public Integer getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(Integer packageCount) {
        this.packageCount = packageCount;
    }

    public Long getBatchFormatId() {
        return batchFormatId;
    }

    public void setBatchFormatId(Long batchFormatId) {
        this.batchFormatId = batchFormatId;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public BigDecimal getStoreQuantity() {
        return storeQuantity;
    }

    public void setStoreQuantity(BigDecimal storeQuantity) {
        this.storeQuantity = storeQuantity;
    }

    public Long getTempId() {
        return tempId;
    }

    public void setTempId(Long tempId) {
        this.tempId = tempId;
    }

    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }

    public String getMaterialBatch() {
        return materialBatch;
    }

    public void setMaterialBatch(String materialBatch) {
        this.materialBatch = materialBatch;
    }

    public Boolean getTempFlag() {
        return tempFlag;
    }

    public void setTempFlag(Boolean tempFlag) {
        this.tempFlag = tempFlag;
    }

    public String getOcrKey() {
        return ocrKey;
    }

    public void setOcrKey(String ocrKey) {
        this.ocrKey = ocrKey;
    }


    public BigDecimal getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(BigDecimal netWeight) {
        this.netWeight = netWeight;
    }


    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Double getSimilarity() {
        return Similarity;
    }

    public void setSimilarity(Double similarity) {
        Similarity = similarity;
    }

    public String getBatchFormat() {
        return batchFormat;
    }

    public void setBatchFormat(String batchFormat) {
        this.batchFormat = batchFormat;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

}
