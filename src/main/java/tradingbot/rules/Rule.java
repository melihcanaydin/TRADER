package tradingbot.rules;

import tradingbot.model.CoinData;

public interface Rule {

    boolean check(CoinData coinData);
}
