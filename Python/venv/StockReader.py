import csv
import os
import re


class StockCSVGetter:
    def __init__(self, stock_folder):
        self.files = os.listdir(stock_folder)
        self.stockFolder = stock_folder

    def get_old_data(self):
        stocks = {}
        for fileName in self.files:
            if '_OLD.csv' in fileName:
                with open(os.path.join(self.stockFolder, fileName)) as newFile:
                    csv_reader = csv.DictReader(newFile)
                    stocks[fileName[:-8]] = [float(line.get('Close')) for line in csv_reader if line.get('Close')
                                             and line.get('Close').replace('.', '', 1).isdigit()]
        return stocks

    def get_new_data(self):
        stocks = {}
        for fileName in self.files:
            if '_NEW.csv' in fileName:
                with open(os.path.join(self.stockFolder, fileName)) as newFile:
                    csv_reader = csv.DictReader(newFile)
                    stocks[fileName[:-8]] = [float(line.get('Close')) for line in csv_reader if line.get('Close')
                                             and line.get('Close').replace('.', '', 1).isdigit()]
        return stocks
