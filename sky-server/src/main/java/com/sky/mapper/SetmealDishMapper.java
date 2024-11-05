package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查找对应的套餐id
     *
     * @param dishIds
     * @return
     */
    // select setmeal_id from seatmeal_dish where dish_id in ids(1,2,...n)
    List<Long> getSetmealIdsByDishId(List<Long> dishIds);
}
