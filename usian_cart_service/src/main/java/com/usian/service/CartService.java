package com.usian.service;

import com.rabbitmq.client.Channel;
import com.usian.mapper.TbItemMapper;
import com.usian.pojo.TbItem;
import com.usian.util.RedisClient;
import com.usian.utils.JsonUtils;
import com.usian.vo.CartItemVo;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CartService {

    @Autowired
    private RedisClient redisClient;
    @Autowired
    private TbItemMapper itemMapper;

    //添加购物车项
    public void addItem(Long userId, Long itemId) {
        //判断该用户是否使用过购物车
        Boolean exists = redisClient.exists("CART"+userId);
        if (exists){//使用过
            CartItemVo cartItemVo = (CartItemVo) redisClient.hget("CART" + userId, itemId + "");
            //判断是否存在该商品
            if (cartItemVo==null){//不存在
                redisClient.hset("CART"+userId,itemId+"",getCart(itemId));
            }else {//存在
                cartItemVo.setNum(cartItemVo.getNum()+1);
                redisClient.hset("CART"+userId,itemId+"",cartItemVo);
            }
        }else {//没使用过
            CartItemVo cartItemVo = getCart(itemId);
            redisClient.hset("CART"+userId,itemId+"",cartItemVo);
        }
    }

    //获取购物车项
    public CartItemVo getCart(Long itemId){
        CartItemVo cartItemVo = new CartItemVo();
        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        cartItemVo.setId(itemId);
        cartItemVo.setImage(item.getImage());
        cartItemVo.setNum(1);
        cartItemVo.setPrice(item.getPrice());
        cartItemVo.setSellPoint(item.getSellPoint());
        cartItemVo.setTitle(item.getTitle());
        return cartItemVo;
    }

    //同步cookie数据
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value="user_queue",durable = "true"),
            exchange = @Exchange(value="user_exchange",type= ExchangeTypes.TOPIC),
            key= {"user.login"}
    ))
    public void listener(String cartJsonAndUserId, Channel channel, Message message){
        String[] cartJsonAndUserIdArray = cartJsonAndUserId.split("---");
        String userId =  cartJsonAndUserIdArray[1];
        //转换cookie中的数据
        Map<Long,CartItemVo> map = JsonUtils.jsonToMap(cartJsonAndUserIdArray[0], Long.class, CartItemVo.class);
        //同步到登录用户的redis中
        Set<Long> itemIds = map.keySet();
        for (Long itemId : itemIds) {
            //获取redis中的该用户存在商品
            CartItemVo redCartItemVo = (CartItemVo) redisClient.hget("CART" + userId, itemId + "");
            if (redCartItemVo==null){//不存在该商品
                CartItemVo cartItemVo = map.get(itemId);
                redisClient.hset("CART"+userId,itemId+"",cartItemVo);
            }else {//存在该商品
                CartItemVo cartItemVo = map.get(itemId);
                redCartItemVo.setNum(redCartItemVo.getNum()+cartItemVo.getNum());
                redisClient.hset("CART"+userId,itemId+"",redCartItemVo);
            }
        }
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查询购物车列表

    public List<CartItemVo> showCart(Long userId) {

        //获取redis中用户购物车项数据
        Map<Object, Object> map = redisClient.hgetAll("CART" + userId);
        ArrayList<CartItemVo> cartList = new ArrayList<>();
        //获取redis中该用户存入的所有的购物车项
        Collection<Object> values = map.values();
        for (Object value : values) {
            cartList.add((CartItemVo)value);
        }
        return cartList;
    }

    public void updateItemNum(Integer num, Long userId, Long itemId) {
        CartItemVo cartItemVo = (CartItemVo) redisClient.hget("CART" + userId,itemId+"");
        if (num==0){
            redisClient.hdel("CART"+userId,itemId+"");
        }else {
            cartItemVo.setNum(num);
            redisClient.hset("CART"+userId,itemId+"",cartItemVo);
        }
    }

    public void deleteItemFromCart(Long itemId, Long userId) {
        redisClient.hdel("CART"+userId,itemId+"");
    }
}
