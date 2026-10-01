package com.example.designpatterns.behavioral.state;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Replays a sequence of events against a fresh order.
 *
 * <pre>
 * curl -s -X POST 'localhost:8080/api/patterns/state/replay?events=PAY,SHIP,DELIVER'
 * curl -s -X POST 'localhost:8080/api/patterns/state/replay?events=PAY,SHIP,CANCEL'
 * </pre>
 */
@RestController
@RequestMapping("/api/patterns/state")
public class StateController {

    @PostMapping("/replay")
    public Map<String, Object> replay(@RequestParam List<OrderEventType> events) {
        Order order = new Order("ORD-REPLAY");
        List<TransitionResult> transitions = events.stream().map(order::handle).toList();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("transitions", transitions);
        result.put("finalState", order.state().name());
        result.put("history", order.history());
        return result;
    }
}
