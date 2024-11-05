package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品管理接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @PostMapping("")
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品: {}", dishDTO);
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品信息")
    public Result<DishVO> getDishById(@PathVariable("id") Long id) {
        log.info("根据 id:{} 查询菜品信息", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        log.info("got dish: {}", dishVO);
        return Result.success(dishVO);
    }

    @GetMapping("/list")
    @ApiOperation("根据分类 id 查询菜品信息")
    public Result<Category> getDishByCategoryId(@RequestParam Long CategoryId) {
        log.info("！！！！根据分类id查询菜品信息，id:{}", CategoryId);
        return null;
    }

    @DeleteMapping("")
    @ApiOperation("(批量)删除菜品")
    public Result deleteByIds(@RequestParam List<Long> ids) {
        log.info("批量删除菜品 ids: {}", ids);
        dishService.deleteByIds(ids);
        return Result.success();
    }

    @PutMapping("")
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品: {}", dishDTO);
        dishService.updateWithFlavor(dishDTO);
        
        return Result.success();
    }


}
