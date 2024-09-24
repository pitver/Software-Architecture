### Увеличил количество эпох для увеличения намерений 
```
- name: DIETClassifier                    # Классификатор на базе DIET
    epochs: 100                              # Минимальное количество эпох обучения
    transformer_size: 128                   # Минимальный размер слоя трансформера
    number_of_transformer_layers: 1         # Один слой трансформера
    use_masked_language_model: false        # Отключено для ускорения
    batch_strategy: "balanced"
    hidden_layers_sizes:
      text: [32]                            # Минимальные скрытые слои

  - name: EntitySynonymMapper               # Маппер синонимов сущностей
  - name: ResponseSelector                  # Минимальный выбор ответа
    epochs: 100                               # Минимальное количество эпох обучения
```
### Доработал функции для кастомных действий. Так же для выполнения необходимо настроить endpoint в endpoints.yml + отдельно запустить сервер кастомных действий
```
rasa run actions

```
### Пример кастомной функции
```
class ActionTechInfo(Action):

    def __init__(self):
        # Инициализация морфологического анализатора
        self.morph = pymorphy2.MorphAnalyzer()

    def name(self) -> Text:
        return "action_tech_info"

    def normalize(self, text: Text) -> List[Text]:
        """
        Приводит все слова в предложении к их начальной форме (лемматизация).
        """
        words = text.split()
        normalized_words = [self.morph.parse(word)[0].normal_form for word in words]
        return normalized_words

    def run(self, dispatcher: CollectingDispatcher,
            tracker: Tracker,
            domain: Dict[Text, Any]) -> List[Dict[Text, Any]]:

        # Получаем последнее сообщение от пользователя
        user_message = tracker.latest_message.get('text')
        normalized_message = self.normalize(user_message)  # Лемматизация

        # Примеры ответов на различные типы вопросов
        if "технология" in normalized_message and "стек" in normalized_message:
            tech_info = "Для выбора стека технологий важно учитывать требования проекта. Например, для веб-разработки популярны стеки MERN или LAMP."
        elif "микросервис" in normalized_message:
            tech_info = "Для микросервисов обычно используют Docker, Kubernetes, а также языки, такие как Go и Java."
        elif "ci/cd" in normalized_message:
            tech_info = "Для CI/CD лучше использовать инструменты, такие как Jenkins, GitLab CI, или CircleCI."
        elif "веб-разработка" in normalized_message:
            tech_info = "Для веб-разработки подходят такие технологии, как React, Angular, Node.js и Django."
        elif "облачный" in normalized_message or "решение" in normalized_message:
            tech_info = "Лучшие технологии для облачных решений включают AWS, Microsoft Azure, и Google Cloud."
        elif "язык" in normalized_message and "программирование" in normalized_message:
            tech_info = "Выбор языка программирования зависит от задач проекта. Например, для высокопроизводительных систем часто используют C++, для веб-разработки — JavaScript или Python."
        elif "база" in normalized_message and "данные" in normalized_message:
            tech_info = "Для масштабируемости часто выбирают базы данных, такие как PostgreSQL, MongoDB или Cassandra."
        elif "devops" in normalized_message:
            tech_info = "Для DevOps-практик используются инструменты, такие как Terraform, Ansible, и Docker."
        elif "автоматизация" in normalized_message:
            tech_info = "Для автоматизации лучше всего подходят технологии, такие как Selenium, Ansible, и Jenkins."
        elif "масштабируемость" in normalized_message:
            tech_info = "Для масштабируемых систем рекомендуются такие технологии, как Kubernetes, Apache Kafka и базы данных, такие как Cassandra."
        else:
            tech_info = "Для выбора технологий важно учитывать специфику вашего проекта. Могу предложить различные варианты стека технологий в зависимости от ваших требований."

        # Отправляем ответ пользователю
        dispatcher.utter_message(text=tech_info)

        return []
```
