package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {

    /**
     * 新增菜品及菜品对应的口味数据
     *
     * @param dishDTO
     */
    void saveWithFlavor(DishDTO dishDTO);
}
