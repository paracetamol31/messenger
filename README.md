# messenger
Это консольная версия чата, в которой реализован почи весь основной фунукционал из задания (версия с GUI включает в себя все остальные пункты). Плюсом добавил от себя пароли для пользователей и консольные команды (добавил только две: "\BAN имя_пользователя" , "\GIVEADMIN имя_пользователя", но легко добавить еще). Так же в чат может писать сервер, в начале чата только у него есть права администратора. История сообщений и список пользвателей храняться в файлах. Чтобы запустить чат, сначала надо запустить MainServer, после этого можно подключать клиентов, запуская MainClient. Чтобы настроить порт и ip для подключения, надо зайти в src/java/com/client/Client и прописать  параметры в константах (в версии с GUI реализовал возмоность указать ip и порт через интерфейс перед запуском чата и авторизацией). Аналогично с сервером - чтобы указать порт, надо зайти в  src/java/com/server/Server и прописать порт в константе. Наверное лучше сразу смотреть версюи с GUI, консольную отпрвил на вский случай, вдруг со сборкой проекта с javaFX будут проблемы.
