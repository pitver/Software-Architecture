import jwt

# Заголовок
header = {"alg": "HS256", "typ": "JWT"}

# Полезная нагрузка
payload = {"iss": "user"}

# Секретный ключ
secret = "123"

# Создание токена
token = jwt.encode(payload, secret, algorithm="HS256", headers=header)

print(token)