from DBInterface import SQLInterface


db = SQLInterface()
res = db('show tables')
print(res)
print(db.select(table='test_table', value='*'))
