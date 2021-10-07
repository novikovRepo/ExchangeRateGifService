package com.example.exchangerategifservice.services.implementations;

import com.example.exchangerategifservice.client.ExchangeClient;
import com.example.exchangerategifservice.model.ExchangeModel;
import com.example.exchangerategifservice.services.interfaces.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExchangeServiceImp implements ExchangeService {
    private ExchangeModel previous;
    private ExchangeModel current;

    private ExchangeClient exchangeClient;
    private SimpleDateFormat date;
    private SimpleDateFormat time;
    @Value("${openexchangerates.app.id}")
    private String appId;
    @Value("${openexchangerates.base}")
    private String base;

    @Autowired
    public ExchangeServiceImp(
            ExchangeClient exchangeClient,
            @Qualifier("date") SimpleDateFormat date,
            @Qualifier("time") SimpleDateFormat time
    ) {
        this.exchangeClient = exchangeClient;
        this.date = date;
        this.time = time;
    }

    @Override
    public int getKeyForTag(String code) {
        this.refresh();
        Double prevCoefficient = this.getCoefficient(this.previous, code);
        Double currentCoefficient = this.getCoefficient(this.current, code);
        return prevCoefficient != null && currentCoefficient != null
                ? Double.compare(currentCoefficient, prevCoefficient)
                : -101;
    }

    @Override
    public void refresh() {
        long currentTime = System.currentTimeMillis();
        this.refreshCurrent(currentTime);
        this.refreshPrevious(currentTime);
    }

    private void refreshCurrent(long t)
    {
        if (
                this.current == null ||
                        !time.format(Long.valueOf(this.current.getTimestamp()) * 1000)
                                .equals(time.format(t))
        ) {
            this.current = exchangeClient.getLatest(this.appId);
        }
    }

    private void refreshPrevious(long t) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Calendar prevCalendar = Calendar.getInstance();
        prevCalendar.setTimeInMillis(t);

        Date date1 = prevCalendar.getTime();
        String currentDate = format1.format(date1);

        prevCalendar.add(Calendar.DAY_OF_YEAR, -1);

        Date date2 = prevCalendar.getTime();
        String newPrevDate = format1.format(date2);
        if (
                this.previous == null
                        || (
                        !date.format(Long.valueOf(this.previous.getTimestamp()) * 1000)
                                .equals(newPrevDate)
                                && !date.format(Long.valueOf(this.previous.getTimestamp()) * 1000)
                                .equals(currentDate)
                )
        ) {
            this.previous = exchangeClient.getPrevious(newPrevDate, appId);
        }
    }

    private Double getCoefficient(ExchangeModel rates, String code) {
        Double result = null;
        Double targetRate = null;
        Double appBaseRate = null;
        Double defaultBaseRate = null;
        Map<String, Double> map = null;
        if (rates != null && rates.getRates() != null) {
            map = rates.getRates();
            targetRate = map.get(code);
            appBaseRate = map.get(this.base);
            defaultBaseRate = map.get(rates.getBase());
        }
        if (targetRate != null && appBaseRate != null && defaultBaseRate != null) {
            result = new BigDecimal(
                    (defaultBaseRate / appBaseRate) * targetRate
            )
                    .setScale(4, RoundingMode.UP)
                    .doubleValue();
        }
        return result;
    }
}
