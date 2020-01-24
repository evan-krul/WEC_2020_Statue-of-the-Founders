import csv
import os


class StockCSVGetter:
    def __init__(self, stockFolder):
        self.files = os.listdir(stockFolder)

    def get_old_data(self):
        stocks = {}
        for fileName in self.files:
            matcher = re.match(r'^(P<stock name>\w+)_OLD\.csv$', fileName)
            if matcher:
                with open(filename) as newFile:
                    csv_reader = csv.DictReader(newFile)
                    stocks[matcher.group('stock name')] = csv_reader
        return stocks

    def get_new_data(self):
        stocks = {}
        for fileName in files:
            matcher = re.match(r'^(P<stock name>\w+)_NEW\.csv$', fileName)
            if matcher:
                with open(filename) as newFile:
                    csv_reader = csv.DictReader(newFile)
                    stocks[matcher.group('stock name')] = csv_reader
        return stocks
