import os
print(f'login: {os.getlogin()}')
print(f'os sysname: {os.uname().sysname}')
print(f'os version: {os.uname().version}')
print(os.listdir(path='../..'))