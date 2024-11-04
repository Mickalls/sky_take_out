package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关接口")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("")
    @ApiOperation("新增分类")
    public Result save(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类:{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类分页查询,参数为: {}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result startOrStop(@PathVariable("status") Integer status, Long id) {
        log.info("启用禁用分类: {}, {}", status, id);
        categoryService.startOrStop(status, id);
        return Result.success();
    }

    @PutMapping("")
    @ApiOperation("编辑分类信息")
    public Result updateCategoryMsg(@RequestBody CategoryDTO categoryDTO) {
        log.info("编辑分类信息 {}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查分类信息")
    public Result<Category> getCategoryById(@PathVariable("id") Long id) {
        log.info("根据 id: {} 查询分类信息");
        return Result.success(categoryService.getCategoryById(id));
    }

    @DeleteMapping("")
    @ApiOperation("根据id删除分类信息")
    public Result delete(@RequestParam Long id) {
        log.info("根据 id: {} 删除分类信息", id);
        categoryService.deleteCategoryById(id);
        return Result.success();
    }

    /**
     * 根据类别查询所有对应类型的具体分类名称
     * 用法:菜品管理和套餐管理中的“菜品/套餐 分类”中选择具体的分类类型
     * 优化:查询时只查name字段,但因为前端代码有问题,只能返回List<Category>,不然范围List<String>无法正常显示数据
     */
    @GetMapping("/list")
    @ApiOperation("根据类别查询对应类型的具体分类名称")
    public Result<List<Category>> list(@RequestParam Integer type) {
        log.info("根据类型 {} 查询该类型的具体分类", type);
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
