package com.beidou.exchange.match;

import com.beidou.exchange.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MatchTask {
    public static final int TASK_OP_SUBMIT = 0;
    public static final int TASK_OP_CANCEL = 1;

    private int op;
    private Order order;
}

