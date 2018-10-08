package com.beidou.exchange.order.service.impl;

import com.beidou.exchange.balance.service.BalanceService;
import com.beidou.exchange.common.Constants;
import com.beidou.exchange.common.DateUtility;
import com.beidou.exchange.common.ErrorEnum;
import com.beidou.exchange.common.Result;
import com.beidou.exchange.common.exception.BizException;
import com.beidou.exchange.match.service.MatchService;
import com.beidou.exchange.order.entity.Order;
import com.beidou.exchange.order.mapper.OrderMapper;
import com.beidou.exchange.order.service.OrderService;
import com.beidou.exchange.service.IdWorkerService;
import com.beidou.exchange.service.impl.CommonGenericServiceImpl;
import com.beidou.market.entity.AssetCategory;
import com.beidou.market.service.DbAssetsService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl extends CommonGenericServiceImpl<Order> implements OrderService {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private IdWorkerService idWorkerService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private DbAssetsService dbAssetsService;

    private boolean checkOrder(Order order) {
        if (StringUtils.isBlank(order.getUid()) ||
                StringUtils.isBlank(order.getAsset()) ||
                StringUtils.isBlank(order.getPriceAsset()) ||
                order.getAmount() == null || order.getDirection() == null ||
                order.getType() == null) {
            return false;
        }
        if (order.getType() == 0) {
            //限价单
            if (order.getTotalPrice() == null || order.getPrice() == null) {
                return false;
            }
        }
        return true;
    }

    private void fillOrder(Order order) {
        Long id = idWorkerService.nextId();
        order.setId(id);
        order.setOriginAmount(order.getAmount());
        order.setOriginPrice(order.getTotalPrice());
        order.setCreatedOn(System.currentTimeMillis());
        order.setUpdatedOn(System.currentTimeMillis());
        //默认保留6位小数，四舍五入
        order.setPrice(order.getTotalPrice().divide(order.getAmount(), Constants.DEFAULT_DECIMAL_SCALE, BigDecimal.ROUND_UP));
        order.setStatus((short) 0);
    }

    @Transactional
    @Override
    public Result createOrder(Order order) {
        if (!checkOrder(order)) {
            throw new BizException(ErrorEnum.MISSING_PARAM);
        }
        if (order.getDirection() == 1) {
            // 卖，锁定标的资产
            Result lockResult = balanceService.lockBalance(order.getUid(), order.getAsset(), order.getAmount());
            //余额不足
            if (lockResult.code == ErrorEnum.BALANCE_NOT_ENOUGH_AVL_BALANCE) {
                lockResult.payload = order.getAsset();
                return lockResult;
            }
        } else {
            // 买，锁定等价物资产
            Result lockResult = balanceService.lockBalance(order.getUid(), order.getPriceAsset(), order.getTotalPrice());
            //余额不足
            if (lockResult.code == ErrorEnum.BALANCE_NOT_ENOUGH_AVL_BALANCE) {
                lockResult.payload = order.getPriceAsset();
                return lockResult;
            }
        }
        //创建订单
        fillOrder(order);
        orderMapper.insert(order);

        //提交订单到撮合服务
        matchService.submitOrder(order);
        return new Result(ErrorEnum.SUCCESS);
    }

    @Override
    public int updateOrder(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order);
    }

    /**
     * 查询并锁定订单记录
     * @param orderId
     * @return
     */
    @Transactional
    @Override
    public Order getOrderForUpdate(Long orderId) {
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("id", orderId);
        example.setForUpdate(true);
        List<Order> order = orderMapper.selectByExample(example);
        if (order.size() > 0) {
            return order.get(0);
        } else {
            return null;
        }
    }

    /**
     * 撤销订单
     *
     * @param orderId
     * @return
     */
    @Override
    public Result cancelOrder(Long orderId) {
        //通知撮合服务取消订单
        Order queryOrder = new Order();
        queryOrder.setId(orderId);
        Order order = orderMapper.selectByPrimaryKey(queryOrder);
        matchService.cancelOrder(order);
        return new Result();
    }

    /**
     * 撤销订单
     * 解锁资产，通知撮合引擎
     * @param orderId
     * @return
     */
    @Transactional
    @Override
    public Result doCancelOrder(Long orderId) {
        if (orderId == null) {
            throw new BizException(ErrorEnum.MISSING_PARAM);
        }
        Order order = getOrderForUpdate(orderId);
        if (order == null) {
            return new Result(ErrorEnum.SUCCESS, "order not exist");
        }

        if (order.getDirection() == 1) {
            // 卖，解锁标的资产
            Result lockResult = balanceService.unlockBalance(order.getUid(), order.getAsset(), order.getAmount());
            //余额不足，通常是发生了异常
            if (lockResult.code == ErrorEnum.BALANCE_NOT_ENOUGH_LOC_BALANCE) {
                lockResult.payload = order.getAsset();
                return lockResult;
            }
        } else {
            // 买，解锁等价物资产
            Result lockResult = balanceService.unlockBalance(order.getUid(), order.getPriceAsset(), order.getTotalPrice());
            //余额不足
            if (lockResult.code == ErrorEnum.BALANCE_NOT_ENOUGH_LOC_BALANCE) {
                lockResult.payload = order.getPriceAsset();
                return lockResult;
            }
        }

        //删除订单
        order.setStatus(Order.ORDER_STATUS_CANCELED);
        orderMapper.updateByPrimaryKeySelective(order);

        return new Result(ErrorEnum.SUCCESS);
    }

    /**
     * 查看指定用户指定交易对的订单
     *
     * @param uid        指定用户
     * @param asset      交易主资产
     * @param priceAsset 交易等价物资产
     * @return
     */
    @Override
    public List<Order> listOrder(String uid, String asset, String priceAsset) {
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("uid", uid)
                .andEqualTo("asset", asset)
                .andEqualTo("priceAsset", priceAsset);
        return orderMapper.selectByExample(example);
    }

    @Override
    public List<Order> listAllOrderWaiting() {
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("status", 0);
        example.orderBy("id").asc();
        return orderMapper.selectByExample(example);
    }

    /**
     * 匹配订单
     *
     * @param awaitOrder    在队列里等待匹配的限价单
     * @param incomingOrder 新进来的订单
     * @param matchAmount 成交量
     * @param matchTotalPrice 成交总价
     * @return
     */
    @Transactional
    @Override
    public Result matchOrder(Order awaitOrder, Order incomingOrder, BigDecimal matchAmount, BigDecimal matchTotalPrice) {
//        Order oldAwaitOrder = getOrderForUpdate(awaitOrder.getId());
//        Order oldIncomingOrder = getOrderForUpdate(incomingOrder.getId());
        //先判断订单是否取消
//        if (oldAwaitOrder.getStatus() == Order.ORDER_STATUS_CANCELED || oldIncomingOrder.getStatus() == Order.ORDER_STATUS_CANCELED) {
//            awaitOrder.setStatus(oldAwaitOrder.getStatus());
//            incomingOrder.setStatus(oldIncomingOrder.getStatus());
//            return new Result(ErrorEnum.ORDER_CACELED);
//        } else {
            awaitOrder.setUpdatedOn(System.currentTimeMillis());
            orderMapper.updateByPrimaryKeySelective(awaitOrder);
            incomingOrder.setUpdatedOn(System.currentTimeMillis());
            orderMapper.updateByPrimaryKeySelective(incomingOrder);
            return new Result();
//        }
    }

    /**
     * 标记已入队等待匹配的订单，用于恢复队列
     *
     * @param orderId 订单id
     * @return
     */
    @Override
    public int markOrderEnqueue(long orderId) {
        Order order = new Order();
        order.setId(orderId);
        order.setStatus((short)1);
        return orderMapper.updateByPrimaryKeySelective(order);
    }



    public List<Order> listOrderNowForStatus(String uid,String asset,String priceAsset,Integer status) {
        Example example = new Example(Order.class);
        Long currentTime = System.currentTimeMillis();
        Date now = new Date();
        Long startTime = DateUtility.getStartTimeOfDay(now);
        Long endTime = DateUtility.getEndTimeOfDay(now);
        example.createCriteria().andEqualTo("status", status).andEqualTo("uid",uid)
                .andEqualTo("asset",asset).andEqualTo("priceAsset",priceAsset)
                .andBetween("createdOn",startTime,endTime);
        example.orderBy("id").asc();

        Map<String,AssetCategory> allAssetMap = dbAssetsService.getAllAssetMap();
        List<Order> list = orderMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)) {
            for (Order order : list) {
                order.setAssetNick(allAssetMap.get(order.getAsset()).getNick());
                order.setPriceAssetNick(allAssetMap.get(order.getPriceAsset()).getNick());
            }
        }
        return list;
    }

}

