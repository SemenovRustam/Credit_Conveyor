
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=semenovrustam_Credit_Conveyor&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=semenovrustam_Credit_Conveyor)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=semenovrustam_Credit_Conveyor&metric=bugs)](https://sonarcloud.io/summary/new_code?id=semenovrustam_Credit_Conveyor)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=semenovrustam_Credit_Conveyor&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=semenovrustam_Credit_Conveyor)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=semenovrustam_Credit_Conveyor&metric=coverage)](https://sonarcloud.io/summary/new_code?id=semenovrustam_Credit_Conveyor)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=semenovrustam_Credit_Conveyor&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=semenovrustam_Credit_Conveyor)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=semenovrustam_Credit_Conveyor&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=semenovrustam_Credit_Conveyor)
[![codecov](https://codecov.io/gh/SemenovRustam/Credit_Conveyor/branch/develop/graph/badge.svg?token=AA23Z1DBXM)](https://codecov.io/gh/SemenovRustam/Credit_Conveyor)

# Credit conveyor

Пользователь отправляет заявку на кредит.
МС Application осуществляет прескоринг заявки и если прескоринг проходит,
то заявка сохраняется в МС Deal и отправляется в Conveyor.
Conveyor возвращает через МС Deal пользователю 4 предложения (сущность "LoanOffer") по кредиту с 
разными условиями (например без страховки, со страховкой, с зарплатным клиентом, со страховкой и зарплатным клиентом) или отказ.
Пользователь выбирает одно из предложений, отправляется запрос в МС Application, а оттуда в МС Deal,
где заявка на кредит и сам кредит сохраняются в базу.
МС Deal отправляет клиенту письмо с текстом "Ваша заявка предварительно одобрена, завершите оформление".
Клиент отправляет запрос в МС Deal со всеми своими полными данными о работодателе и прописке.
Происходит скоринг данных в Conveyor, Conveyor рассчитывает все данные по кредиту (ПСК, график платежей и тд),
МС Deal сохраняет обновленную заявку и сущность кредит сделанную на основе CreditDTO полученного из Conveyor со статусом CALCULATED в БД.
После валидации МС Deal отправляет письмо на почту клиенту с одобрением или отказом.
Если кредит одобрен, то в письме присутствует ссылка на запрос "Сформировать документы"
Клиент отправляет запрос на формирование документов в МС Сделка,
МС Сделка отправляет клиенту на почту документы для подписания и ссылку на запрос на согласие с условиями.
Клиент может отказаться от условий или согласиться. Если согласился - МС Deal на почту отправляет код и ссылку на подписание документов,
куда клиент должен отправить полученный код в МС Deal.
Если полученный код совпадает с отправленным, МС Deal выдает кредит (меняет статус сущности "Кредит" на ISSUED, а статус заявки на CREDIT_ISSUED)

![model](https://user-images.githubusercontent.com/88612028/180473667-e918a643-3cfa-4c8e-90ba-d5c5e80d30d7.jpg)
