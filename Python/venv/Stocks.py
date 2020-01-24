from StockReader import StockCSVGetter
import json
import os


class StockManager:
    """
    a list of Stock objects along with methods to save and load to json file, and a method to setup from csv file
    """
    def __init__(self, filename="", budget=0):
        self.stocks = []
        self.budget = budget
        self.filename = filename
        self.day = 0
        if filename:
            self.load(filename)

    def save(self, filename=""):
        if not filename:
            filename = self.filename
        with open(filename, 'w') as save_file:
            json.dump(
                {
                    'stocks': [stock.format_json() for stock in self.stocks],
                    'budget': self.budget,
                    'day': self.day
                },
                save_file, indent=4)

    def load(self, filename):
        if os.path.exists(filename):
            with open(filename, 'r') as load_file:
                json_data = json.load(load_file)
                self.budget = json_data['budget']
                self.day = json_data['day'] + 1
                for stock in json_data['stocks']:
                    self.stocks.append(Stock(name=stock['name'], buy=stock['buy'], counter15=stock['counter15'],
                                             amount_purchased=stock['amountPurchased'], stock_price=stock['stockPrice'],
                                             stock_price_at_purchase=stock['stockPriceAtPurchase'],fell15=stock['fell15'],
                                             in20=stock['in20'], price15=stock['price15'], price20=stock['price20'],
                                             money_made=stock['moneyMade'], seven_days=stock['sevenDays'],
                                             thirty_days=stock['thirtyDays'], two_days=stock['twoDays'],
                                             stock_price_start_month=stock['stockPriceStartMonth'],
                                             fifteen_sp=stock['fifteenSP'], sell_ag=stock['sellAG'], agsp=stock['AGSP'],
                                             yesterday_pri=stock['yesterdayPri'], starting=stock['starting'],
                                             year_pro=stock['yearPro'], max_cap=stock['maxCap'], trough=stock['trough'],
                                             buying_num_stock=stock['buyingNumStock'], counter20=stock['counter20']
                                             ))
        else:
            self.setup('stock_files/stock_csv/')

    def setup(self, csv_folder):
        reader = StockCSVGetter(csv_folder)
        old_data = reader.get_old_data()
        for stock_name, past_prices in old_data.items():
            if past_prices:
                new_stock = Stock(name=stock_name, price15=past_prices[14], price20=past_prices[19],
                                  seven_days=past_prices[:6], thirty_days=past_prices[:29], two_days=past_prices[:1])
            else:
                new_stock = Stock(name=stock_name)
            new_stock.start(past_prices)
            self.stocks.append(new_stock)


class Stock:
    def __init__(self, name='', buy=False, counter15=0, amount_purchased=1, stock_price=0, stock_price_at_purchase=0,
                 fell15=0, in20=0, price15=0, price20=0, money_made=0, seven_days=[], thirty_days=[], two_days=[],
                 stock_price_start_month=0, fifteen_sp=0, sell_ag=0, agsp=0, yesterday_pri=0, starting=True,
                 year_pro=0, max_cap=0, trough=0, buying_num_stock=0, counter20=0):
        self.name = name
        self.buy = buy
        self.counter15 = counter15
        self.counter20 = counter20
        self.amountPurchased = amount_purchased
        self.stockPrice = stock_price
        self.stockPriceAtPurchase = stock_price_at_purchase
        self.fell15 = fell15
        self.in20 = in20
        self.price15 = price15
        self.price20 = price20
        self.moneyMade = money_made
        self.sevenDays = seven_days
        self.thirtyDays = thirty_days
        self.twoDays = two_days
        self.stockPriceStartMonth = stock_price_start_month
        self.fifteenSP = fifteen_sp
        self.sellAG = sell_ag
        self.AGSP = agsp
        self.yesterdayPri = yesterday_pri
        self.starting = starting
        self.yearPro = year_pro
        self.maxCap = max_cap
        self.trough = trough
        self.buyingNumStock = buying_num_stock


    def format_json(self):
        return self.__dict__

    def set_buying_num(self, num_to):
        self.buyingNumStock = int(num_to)

    def analyze(self, stock_price, day):
        tot = 0
        self.thirtyDays.append(stock_price)
        if day % 30 == 0 or day >= 30:
            tot -= self.buyS(stock_price, day)
        if self.buy:
            self.sevenDays.append(stock_price)
            self.twoDays.append(stock_price)
            tot += self.sell(stock_price, day)
        self.yesterdayPri = stock_price

    def start(self, old_sp):
        starting = []
        start = 0
        while len(old_sp) != start:
            self.analyze(old_sp[start], start)
            start += 1
        self.thirtyDays.clear()

    def getMM(self):
        return self.moneyMade

    def sell(self, day, stock_price_curr):
        total = 0
        num = int(day - 2) if day > 2 else 0
        trough = stock_price_curr / self.stockPriceAtPurchase if self.stockPriceAtPurchase else 0
        if day >= 2 and not self.fell15:
            total = sum(self.twoDays[num:])
            if total * 1.2 > self.twoDays[num]:
                self.fell15 = True
                self.counter15 = 0
                self.fifteenSP = stock_price_curr
        elif self.fell15 and self.counter15 != 2:
            if self.fifteenSP < stock_price_curr or 15 * 1.05 < stock_price_curr:
                return sellStock(stock_price_curr)

        if not self.sellAG:
            if self.yesterdayPri * 0.85 > stock_price_curr:
                self.sellAG = True
                self.AGSP = self.yesterdayPri
                self.counter20 = 0
        elif self.sellAG:
            if self.AGSP * 0.925 > stock_price_curr and self.counter20 != 7:
                self.sellAG = false
                self.counter20 += 1
            elif self.counter20 == 7:
                return self.sellStock(stock_price_curr)

    def sellStock(self, stock_price_now):
        self.buy = False
        self.moneyMade += self.stockPriceAtPurchase * self.amountPurchased - stock_price_now * self.amountPurchased
        return self.moneyMade

    def buyS(self, day, stock_price_curr):
        avg = sum(float(i) for i in self.thirtyDays)/30
        if stock_price_curr * 1.15 > avg and not self.buy:
            self.buy = True
            self.stockPriceAtPurchase = stock_price_curr
            if self.starting:
                self.amountPurchased = 1000 // stock_price_curr
            else:
                self.amountPurchased = int(self.buyingNumStock)
        return self.amountPurchased * stock_price_curr

    def fBuy(self, stockP):
        self.buy = 0
        self.stockPriceAtPurchase = stockP
        return self.amountPurchased * stockP
