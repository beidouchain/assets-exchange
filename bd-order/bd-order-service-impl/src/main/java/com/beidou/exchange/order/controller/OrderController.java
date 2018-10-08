package com.beidou.exchange.order.controller;

import com.beidou.exchange.balance.entity.Journal;
import com.beidou.exchange.balance.service.JournalService;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.Result;
import com.beidou.exchange.order.entity.Order;
import com.beidou.exchange.order.service.OrderService;
import com.beidou.exchange.order.vo.OrderVO;
import com.beidou.exchange.service.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fengguoqing on 2018/6/19.
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JournalService journalService;

    /**
     * 下订单 买
     *
     * @param vo
     * @return
     */
    @RequestMapping("/buy")
    public ServiceResponse buy(@RequestBody OrderVO vo) {
        Order orderParams = new Order();
        Short direction = 0;
        orderParams.setDirection(direction);
        Short type = 0;
        orderParams.setType(type);
        BigDecimal price = vo.getPrice().setScale(6, RoundingMode.UP);
        BigDecimal amount = vo.getAmount();
        orderParams.setPrice(price);
        orderParams.setAmount(amount);
        BigDecimal totalPrice = price.multiply(amount).setScale(6, RoundingMode.UP);
        orderParams.setTotalPrice(totalPrice);
        orderParams.setUid(vo.getUid());
        orderParams.setAsset(vo.getAsset());
        orderParams.setPriceAsset(vo.getPriceAsset());
        Result result = orderService.createOrder(orderParams);
        return ServiceResponse.buildResponse(result.code, result.payload, result.msg);
    }

    /**
     * 下订单 卖
     *
     * @param vo
     * @return
     */
    @RequestMapping("/sell")
    public ServiceResponse sell(@RequestBody OrderVO vo) {
        Order orderParams = new Order();
        Short direction = 1;
        orderParams.setDirection(direction);
        Short type = 0;
        orderParams.setType(type);
        BigDecimal price = vo.getPrice().setScale(6, RoundingMode.UP);
        BigDecimal amount = vo.getAmount();
        orderParams.setPrice(price);
        orderParams.setAmount(amount);
        BigDecimal totalPrice = price.multiply(amount).setScale(6, RoundingMode.UP);
        orderParams.setTotalPrice(totalPrice);
        orderParams.setUid(vo.getUid());
        orderParams.setAsset(vo.getAsset());
        orderParams.setPriceAsset(vo.getPriceAsset());
        Result result = orderService.createOrder(orderParams);
        return ServiceResponse.buildResponse(result.code, result.payload, result.msg);
    }

    /**
     * 当前订单
     *
     * @param vo
     * @return
     */
    @RequestMapping("/per")
    public ServiceResponse present(@RequestBody OrderVO vo) {

        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, null);
    }

    /**
     * 历史订单
     *
     * @param vo
     * @return
     */
    @RequestMapping("/his")
    public ServiceResponse history(@RequestBody OrderVO vo) {
        String asset1 = vo.getAsset();
        String asset2 = vo.getPriceAsset();

        Journal journal = new Journal();
        journal.setUid(vo.getUid());
        boolean f = Order.compare(asset1, asset2) >= 0;
        if (f) {
            journal.setAsset(asset2);
            journal.setPriceAsset(asset1);
        } else {
            journal.setAsset(asset1);
            journal.setPriceAsset(asset2);
        }
        List<Journal> journalList = journalService.list(journal);
        List<OrderVO> list = new ArrayList<>();
        OrderVO orderVO;
        for (Journal j : journalList) {
            orderVO = new OrderVO();
            orderVO.setAmount(j.getAmount());
            orderVO.setPrice(j.getTotalPrice().divide(j.getAmount(), 6, RoundingMode.UP));
            orderVO.setId(j.getId());
            orderVO.setCreatedOn(j.getUpdatedOn());
            orderVO.setTurnOver(new BigDecimal(0));
            orderVO.setOriginAmount(j.getAmount());
            orderVO.setOperation(j.getDirection() == 0 ? "buy" : "sell");
            list.add(orderVO);
        }
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, list);
    }

    /**
     * 撤销订单
     *
     * @param vo
     * @return
     */
    @RequestMapping("/cancel")
    public ServiceResponse cancel(@RequestBody Order vo) {
        Result result = orderService.cancelOrder(vo.getId());
        return ServiceResponse.buildResponse(result.code, result.payload, result.msg);
    }

    /**
     * 获取订单列表 根据状态
     *
     * @param vo
     * @return
     */
    @RequestMapping("/listOrder")
    public ServiceResponse listOrder(@RequestBody OrderVO vo) {
        List<Order> orderList = orderService.listOrderNowForStatus(vo.getUid(), vo.getAsset(), vo.getPriceAsset(), vo.getStatus());
        List<OrderVO> list = new ArrayList<>();
        OrderVO orderVO;
        for (Order o : orderList) {
            orderVO = new OrderVO();
            orderVO.setAmount(o.getAmount());
            orderVO.setPrice(o.getPrice());
            orderVO.setId(o.getId());
            orderVO.setIdStr(o.getId() + "");
            orderVO.setCreatedOn(o.getCreatedOn());
            orderVO.setTurnOver(new BigDecimal(0));
            orderVO.setOriginAmount(o.getOriginAmount());
            list.add(orderVO);
        }
        return ServiceResponse.buildResponse(ErrorEnum.SUCCESS, list);
    }

}
