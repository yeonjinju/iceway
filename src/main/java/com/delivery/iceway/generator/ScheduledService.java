package com.delivery.iceway.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.springframework.stereotype.Component;

@Component
public class ScheduledService {

    private List<ScheduledFuture<?>> list = new ArrayList<>();

    public void addSF(ScheduledFuture<?> sf) {
        list.add(sf);
    }

    public void stopAll() {
        for (ScheduledFuture<?> tmp : list) {
            tmp.cancel(true);
        }
    }
}
