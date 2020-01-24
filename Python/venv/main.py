import os
import Stocks
from DBInterface import SQLInterface
import StockReader
# get data
newData = StockReader.StockCSVGetter("stock_files/stock_csv/").get_new_data()
dataBase = SQLInterface()
for investmentAccount in dataBase.select(table='account', condition='is_investment'):
    id, name, investment, locked, release = investmentAccount
    manager = Stocks.StockManager(f"stock_files/{name}_stocks.json")
    for stock in manager.stocks:
        prices = newData[stock.name]
        if prices:
            change = stock.analyze(prices[manager.day], manager.day)
            if change:
                dataBase.insert(table='account_transactions', columns=('account_a_id'), values=(id))
                type = 'Deposit' if change > 0 else 'Withdrawal'
                dataBase.insert(table='transactions', columns=('t_id', 'amount', 'title', 'type'),
                                values=(dataBase.cursor.lastrowid, abs(change), stock.name, type))
    manager.save()
    # update database




# # scratch area
# manager = Stocks.StockManager("stock_files/test_stocks.json")
# newData = StockReader.StockCSVGetter("stock_files/stock_csv/")
# manager.save()
# for stock in manager.stocks:
#     stock.analyze(123.2, 4)

