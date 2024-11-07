package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        // 1. 判断当前加入购物车的商品是否已经存在
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        // 2. 如果已经存在，修改数量加1
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(cart);
            return;
        }

        // 3. 不存在则插入新数据
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        if (dishId != null) {
            // 添加的是菜品
            Dish dish = dishMapper.getById(dishId);
            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setAmount(dish.getPrice());
            // 插入到购物车表
        } else {
            // 添加的是套餐
            Setmeal setmeal = setmealMapper.getById(setmealId);
            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setAmount(setmeal.getPrice());

            // 插入到购物车表
        }
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());
        // 统一插入到购物车表
        shoppingCartMapper.insert(shoppingCart);
    }

    /**
     * 查看购物车
     *
     * @return
     */
    public List<ShoppingCart> showShoppingCart() {
        // 获取当前微信用户的id
        Long currentId = BaseContext.getCurrentId();

        // 调用mapper层的list动态查询
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(currentId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    public void clean() {
        // 获取当前微信用户的id
        Long currentId = BaseContext.getCurrentId();

        // 调用mapper层方法
        shoppingCartMapper.deleteByUserId(currentId);
    }

    /**
     * 删除购物车中的一件商品
     *
     * @param shoppingCartDTO
     */
    public void deleteOne(ShoppingCartDTO shoppingCartDTO) {
        // 获取当前微信用户的id
        Long currentId = BaseContext.getCurrentId();

        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(currentId);

        // 如果有这个记录且数量大于1，则update数量为1
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        ShoppingCart foundShoppingCart = list.get(0);
        if (foundShoppingCart.getNumber() == 1) {
            // 如果购物车中数量只有一件，直接删除
            shoppingCartMapper.deleteOne(shoppingCart);
            return;
        }
        // 否则只删除购物车中的一件数量
        // 先讲查找到的记录中的number字段减1
        foundShoppingCart.setNumber(foundShoppingCart.getNumber() - 1);
        shoppingCartMapper.updateNumberById(foundShoppingCart);
    }
}
