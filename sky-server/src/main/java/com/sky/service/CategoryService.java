package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 新增分类
     *
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 启用 禁用分类
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 编辑分类
     *
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 根据id查询分类信息
     *
     * @param id
     * @return
     */
    Category getCategoryById(Long id);

    /**
     * 根据id删除分类信息
     *
     * @param id
     */
    void deleteCategoryById(Long id);

    /**
     * 根据类别查询对应的分类信息
     *
     * @param type
     * @return
     */
    List<Category> list(Integer type);
}
