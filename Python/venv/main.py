import os
import Stocks
from DBInterface import SQLInterface

# get data

manager = Stocks.StockManager('stock_files/stocks.json')
for stock in manager.stocks:
    stock.analyze()



