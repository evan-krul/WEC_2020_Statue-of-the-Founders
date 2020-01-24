import pymysql


class SQLInterface(object):
    """
    provides an interface to a SQL database
    """

    def __init__(self, host='wec2020-sql.mysql.database.azure.com', user='wec@wec2020-sql', password='J7L89E8EQFJansAG', database='wec2020'):
        """
        Constructor
        """
        self.db = pymysql.connect(host, user, password, database)
        self.cursor = self.db.cursor()
        self.cursor.execute('SELECT VERSION()')
        print(self.cursor.fetchone())

    def __call__(self, command):
        self.cursor.execute(command)
        return self.cursor.fetchall()

    def insert(self, table: str, values: tuple, columns: tuple):
        self.cursor.execute(f'INSERT INTO {table} ({columns}) VALUES ({values})')

    def select(self, table='', value='*', condition='TRUE'):
        self.cursor.execute(f'SELECT {value} FROM {table} WHERE {condition}')
        return self.cursor.fetchall()
