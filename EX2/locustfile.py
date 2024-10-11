from locust import HttpUser, task, between

class ServiceAUser(HttpUser):
    # Указываем базовый URL для теста
    host = "http://127.0.0.1:62462"

    # Время ожидания между запросами от одного пользователя
    wait_time = between(1, 5)

    # Определяем задачу, которую будет выполнять каждый виртуальный пользователь
    @task
    def test_service_a(self):
        # Отправляем GET-запрос на /service-a
        self.client.get("/service-a")
