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
        if file_name:
            self.load(filename)

    def save(self, filename):
        with open(filename, 'w') as save_file:
            json.dump(
                {
                    'stocks': [stock.formatJSON() for stock in self.stocks],
                    'budget': self.budget
                },
                save_file, indent=4)

    def load(self, filename):
        if os.path.exists(filename):
            with open(filename, 'r') as load_file:
                json_data = json.load(load_file)
                self.budget = json_data['budget']
                for stock in json_data['stocks']:
                    self.stocks.append(Stock(name=stock['name'], buy=stock['buy'], counter15=stock['counter15'],
                                             amount_purchased=stock['amountPurchased'], stock_price=stock['stockPrice'],
                                             stock_price_at_purchase=stock['stockPriceAtPurchase'], fell15=stock['fell15'],
                                             in20=stock['in20'], price15=stock['price15'], price20=stock['price20'],
                                             money_made=stock['moneyMade'], seven_days=stock['sevenDays'],
                                             thirty_days=stock['thirtyDays'], two_days=stock['twoDays'],
                                             stock_price_start_month=stock['stockPriceStartMonth'],
                                             fifteen_sp=stock['fifteenSP'], sell_ag=stock['sellAG'], agsp=stock['AGSP'],
                                             yesterday_pri=stock['yesterdayPri']
                                             ))
        else:
            self.setup('stock_csv/')

    def setup(self, csv_folder):
        reader = StockCSVGetter(csv_folder)


class Stock:
    def __init__(self, name='', buy=False, counter15=0, amount_purchased=0, stock_price=0, stock_price_at_purchase=0,
                 fell15=0, in20=0, price15=0, price20=0, money_made=0, seven_days=[], thirty_days=[], two_days=[],
                 stock_price_start_month=0, fifteen_sp=0, sell_ag=0, agsp=0, yesterday_pri=0):
        self.name = name
        self.buy = buy
        self.counter15 = counter15
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
        self.stockPriceStartMonth= stock_price_start_month
        self.fifteenSP = fifteen_sp
        self.sellAG = sell_ag
        self.AGSP = agsp
        self.yesterdayPri = yesterday_pri

    def format_json(self):
        """
        converts the object into a dict
        :return: res : dict - key value pairs where the key is the member name and the value is the member value
        """
        return {
            'name': self.name,
            'buy': self.buy,
            'counter15': self.counter15,
            'amountPurchased': self.amountPurchased,
            'stockPrice': self.stockPrice,
            'fell15': self.fell15,
            'in20': self.in20,
            'price15': self.price15,
            'price20': self.price20,
            'moneyMade': self.moneyMade,
            'sevenDays': self.sevenDays,
            'thirtyDays': self.thirtyDays,
            'twoDays': self.twoDays,
            'stockPriceStartMonth': self.stockPriceStartMonth,
            'fifteenSP': self.fifteenSP,
            'sellAG': self.sellAG,
            'AGSP': self.AGSP,
            'yesterdayPri': self.yesterdayPri
        }

    def analyze(self, stock_price, day):
        if day % 30 == 0 or day >= 30:
            self.stockPriceStartMonth = stock_price
            self.buyS(self.stockPriceStartMonth, day)
        if self.buy:
            self.sevenDays.append(stock_price)
            self.twoDays.append(stock_price)
            self.thirtyDays.append(stock_price)
        self.yesterdayPri = stock_price

    def start(self, oldSP):
        for i in range(30):
            self.thirtyDays.append(oldSP.get(i))
        curCounter = 30
        start = 0
        while oldSP.size != curCounter:
            self.analyze(oldSP.get(curCounter), start)

    def sell(self, day, stock_price_curr):
        pass

    def buyS(self, day, stock_price_curr):
        pass
