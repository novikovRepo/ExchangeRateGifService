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

    private final ExchangeClient exchangeClient;
    private final SimpleDateFormat date;
    private final SimpleDateFormat time;
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

    /**
     * Обновляет курсы и сравнивает коэффициенты
     * @param code трехзначный код валюты, с которым будет сравниваться базовая валюта приложения
     * @return результат сравнения текущего и предыдущего коэффициентов
     * Если по каким то причинам отсутствуют курсы или коэффициенты, возвращает ошибочный код
     */
    @Override
    public int getKeyForTag(String code) {
        this.refresh();
        Double prevCoefficient = this.getCoefficient(this.previous, code);
        Double currentCoefficient = this.getCoefficient(this.current, code);
        if (prevCoefficient != null && currentCoefficient != null)
            return Double.compare(currentCoefficient, prevCoefficient);
        return -101;
    }

    /**
     * Обновляет все курсы
     */
    @Override
    public void refresh() {
        long currentTime = System.currentTimeMillis();
        this.refreshCurrent(currentTime);
        this.refreshPrevious(currentTime);
    }

    /**
     * Обновляет текущие курсы
     * @param t время в миллисекундах
     */
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

    /**
     * Обновление вчерашних курсов
     * @param t время в миллисекундах
     */
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

    /**
     * Поскольку база при запросе может быть только USD, c помощью формулы находим отношение валюты,
     * для которой хотим получить гиф, к базовой валюте приложения
     * @param rates курсы валют
     * @param code трехзначный код валюты
     * @return коэффициент, выражающий стоимость одной единицы базовой валюты приложения, в валюте,
     * для которой получаем гифку
     */
    private Double getCoefficient(ExchangeModel rates, String code) {
        Double result = null;
        Double targetRate = null;
        Double appBaseRate = null;
        Double defaultBaseRate = null;
        if (rates != null && rates.getRates() != null) {
            Map<String, Double> map = rates.getRates();
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
